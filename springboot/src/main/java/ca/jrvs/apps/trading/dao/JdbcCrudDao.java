package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Entity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Temporary Message.
 * @param <T> temp.
 */
public abstract class JdbcCrudDao<T extends Entity<Integer>> implements CrudRepository<T, Integer> {

  private static final Logger logger = LoggerFactory.getLogger(JdbcCrudDao.class);

  public abstract JdbcTemplate getJdbcTemplate();

  public abstract SimpleJdbcInsert getSimpleJdbcInsert();

  public abstract String getTableName();

  public abstract String getIdColumnName();

  abstract Class<T> getEntityClass();

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

  private <S extends T> void addOne(S entity) {
    SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(entity);

    Number newId = getSimpleJdbcInsert().executeAndReturnKey(sqlParameterSource);
    entity.setId(newId.intValue());
  }

  public abstract int updateOne(T entity);

  @Override
  public <S extends T> List<S> saveAll(Iterable<S> iterable) {
    Iterator<S> iterator = iterable.iterator();
    List<S> entityList = new ArrayList<>();

    while (iterator.hasNext()) {
      entityList.add(save(iterator.next()));
    }

    return entityList;
  }

  @Override
  public Optional<T> findById(Integer id) {
    Optional<T> entity = Optional.empty();
    String selectSql = "SELECT * FROM " + getTableName()
        + " WHERE " + getIdColumnName() + "=?";

    try {
      entity = Optional.ofNullable((T) getJdbcTemplate()
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

  @Override
  public List<T> findAllById(Iterable<Integer> ids) {
    List<T> entityList = new ArrayList<>();

    for (Integer id : ids) {
      Optional<T> tempEntity = findById(id);
      tempEntity.ifPresent(entityList::add);
    }

    return entityList;
  }

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

  @Override
  public void deleteById(Integer id) {
    String deleteSql = "DELETE FROM " + getTableName()
        + " WHERE " + getIdColumnName() + "=?";

    int rowNum = getJdbcTemplate().update(deleteSql, id);

    if (rowNum != 1) {
      throw new DataRetrievalFailureException("");
    }
  }

  @Override
  public void deleteAll() {
    String deleteSql = "DELETE FROM " + getTableName();
    getJdbcTemplate().update(deleteSql);
  }
}
