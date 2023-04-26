package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Entity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Abstract class JdbcCrudDao implements the CrudRepository with a generic T that extends the Entity
 * DTO, meaning any DAO that extends the JdbcCrudDao must have a DTO that extends the Entity
 * interface.
 *
 * @param <T> Generic that extends the Entity interface meaning the DTOs must extend this
 *            interface.
 */
public abstract class JdbcCrudDao<T extends Entity<Integer>> implements CrudRepository<T, Integer> {

  private static final Logger logger = LoggerFactory.getLogger(JdbcCrudDao.class);

  public abstract JdbcTemplate getJdbcTemplate();

  public abstract SimpleJdbcInsert getSimpleJdbcInsert();

  public abstract String getTableName();

  public abstract String getIdColumnName();

  abstract Class<T> getEntityClass();

  /**
   * If the entity/record exists within the database (utilizing existsById) we update the record,
   * otherwise we add a record to the database.
   *
   * @param entity entity representing the DTO.
   * @param <S>    S extends the generic T which extends the Entity interface.
   * @return returns the DTO that is saved/updated.
   */
  @Override
  public <S extends T> S save(S entity) {
    if (existsById(entity.getId())) {
      if (updateOne(entity) != 1) {
        throw new DataRetrievalFailureException("Unable to update");
      }
    } else {
      addOne(entity);
    }

    return entity;
  }

  /**
   * Helper method that saves the object to the database as a record using the SimpleJdbcInsert
   * retrieved from getSimpleJdbcInsert method with a SqlParameterSource initialized by a
   * BeanPropertySqlParameterSource.
   *
   * @param entity Entity representing the DTO.
   * @param <S>    S extends the generic T which extends the Entity interface.
   */
  private <S extends T> void addOne(S entity) {
    SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(entity);

    Number newId = getSimpleJdbcInsert().executeAndReturnKey(sqlParameterSource);
    entity.setId(newId.intValue());
  }

  /**
   * Abstract helper method that will be implemented by the specific DAO (update methods will be
   * different).
   *
   * @param entity Entity representing the DTO.
   * @return returns an integer for how many records were updated (expects 1).
   */
  public abstract int updateOne(T entity);

  /**
   * saveAll method takes an iterable S (representing the DTO) and for each object within the
   * iterable, it will save this to the database. The method also returns the list of saved/updated
   * DTOs.
   *
   * @param iterable Iterable of DTO.
   * @param <S>      S extends the generic T which extends the Entity interface.
   * @return returns a List of DTOs that were saved/updated.
   */
  @Override
  public <S extends T> List<S> saveAll(Iterable<S> iterable) {
    Iterator<S> iterator = iterable.iterator();
    List<S> entityList = new ArrayList<>();

    while (iterator.hasNext()) {
      entityList.add(save(iterator.next()));
    }

    return entityList;
  }

  /**
   * Finds the DTO by the ID (integer), and returns an optional of the DTO which can either be empty
   * or contain the object.
   *
   * @param id Integer ID representing the ID of the DTO within the database.
   * @return returns an Optional of the DTO (can be empty or contain the object).
   */
  @Override
  public Optional<T> findById(Integer id) {
    Optional<T> entity = Optional.empty();
    String selectSql = "SELECT * FROM " + getTableName()
        + " WHERE " + getIdColumnName() + "=?";

    try {
      entity = Optional.ofNullable(getJdbcTemplate()
          .queryForObject(
              selectSql,
              BeanPropertyRowMapper.newInstance(getEntityClass()),
              id
          ));
    } catch (IncorrectResultSizeDataAccessException e) {
      logger.debug("Can't find with id: " + id, e);
    }

    return entity;
  }

  /**
   * Checks if the DTO exists within the database by getting the count by ID (expects 1).
   *
   * @param id ID for the DTO within the database.
   * @return returns a boolean if the object exists within the database.
   */
  @Override
  public boolean existsById(Integer id) {
    String existSql = "SELECT count(*) FROM " + getTableName()
        + " WHERE " + getIdColumnName() + "=?";

    Integer count = getJdbcTemplate().queryForObject(existSql, Integer.class, id);

    if (count == null) {
      return false;
    } else {
      return count == 1;
    }
  }

  /**
   * Retrieves a List of the DTOs within the database.
   *
   * @return returns the list of the DTOs within the database.
   */
  @Override
  public List<T> findAll() {
    String findAllSql = "SELECT * FROM " + getTableName();
    List<T> entityList = new ArrayList<>();

    try {
      entityList = getJdbcTemplate().query(
          findAllSql,
          BeanPropertyRowMapper.newInstance(getEntityClass())
      );
    } catch (IncorrectResultSizeDataAccessException e) {
      logger.debug("error finding all");
    }

    return entityList;
  }

  /**
   * Get a list of all the records within the database by given ids.
   *
   * @param ids Iterable of Integers representing the Ids for the DTOs within the database.
   * @return returns a List of DTOs.
   * @throws IllegalArgumentException If an ID within the Iterable is not found in the database.
   */
  @Override
  public List<T> findAllById(Iterable<Integer> ids) throws IllegalArgumentException {
    List<T> entityList = new ArrayList<>();

    for (Integer id : ids) {
      entityList.add(findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Could not find with id" + id)));
    }

    return entityList;
  }

  /**
   * Gets the count of all the records for the table within the database.
   *
   * @return long of the count for records within the table in the database.
   */
  @Override
  public long count() {
    String countSql = "SELECT count(*) FROM " + getTableName();

    Long count = getJdbcTemplate().queryForObject(countSql, Long.class);

    if (count == null) {
      throw new DataRetrievalFailureException("Error retrieving count");
    } else {
      return count;
    }
  }

  /**
   * Deletes a record from the database given the ID if it exists within the database. If it doesn't
   * exist throw an IllegalArgumentException, and if the integer returned from the JdbcTemplate
   * update is not 1 (meaning it wasn't deleted) throw an DataRetrievalFailureException.
   *
   * @param id Integer representing the ID of the DTO in the database.
   */
  @Override
  public void deleteById(Integer id) {
    if (existsById(id)) {
      String deleteSql = "DELETE FROM " + getTableName()
          + " WHERE " + getIdColumnName() + "=?";

      int rowNum = getJdbcTemplate().update(deleteSql, id);

      if (rowNum != 1) {
        throw new DataRetrievalFailureException("Unable to delete from the table "
            + getTableName() + " with the ID: " + id);
      }

    } else {
      throw new IllegalArgumentException("Table: " + getTableName()
          + " does not have a record with the ID: " + id);
    }
  }

  /**
   * Delete all records from the table in the database.
   */
  @Override
  public void deleteAll() {
    String deleteSql = "DELETE FROM " + getTableName();
    getJdbcTemplate().update(deleteSql);
  }
}
