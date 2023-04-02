package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the abstract class JdbcCrudDao with the DTO being the SecurityOrder class.
 */
@Repository
public class SecurityOrderDao extends JdbcCrudDao<SecurityOrder> {

  private static final Logger logger = LoggerFactory.getLogger(SecurityOrderDao.class);
  private static final String TABLE_NAME = "security_order";
  private static final String ID_COLUMN = "id";
  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  /**
   * Constructor that takes a DataSource and initializes the JdbcTemplate and SimpleJdbcInsert using
   * this DataSource. Spring IoC manages the lifecycle of this component and all dependencies.
   *
   * @param dataSource DataSource used to initialize the JdbcTemplate and SimpleJdbcInsert.
   */
  @Autowired
  public SecurityOrderDao(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
        .withTableName(TABLE_NAME)
        .usingGeneratedKeyColumns(ID_COLUMN);
  }

  /**
   * Gets the JdbcTemplate.
   *
   * @return returns the JdbcTemplate initialized by the SecurityOrderDao.
   */
  @Override
  public JdbcTemplate getJdbcTemplate() {
    return this.jdbcTemplate;
  }

  /**
   * Gets the SimpleJdbcInsert.
   *
   * @return returns the SimpleJdbcInsert initialized by the SecurityOrderDao.
   */
  @Override
  public SimpleJdbcInsert getSimpleJdbcInsert() {
    return this.simpleJdbcInsert;
  }

  /**
   * Gets the TABLE_NAME static variable.
   *
   * @return returns the TABLE_NAME static variable.
   */
  @Override
  public String getTableName() {
    return TABLE_NAME;
  }

  /**
   * Gets the ID_COLUMN static variable.
   *
   * @return returns the ID_COLUMN static variable for the table.
   */
  @Override
  public String getIdColumnName() {
    return ID_COLUMN;
  }

  /**
   * Get the DTO entity class type.
   *
   * @return returns the class type for the DTO.
   */
  @Override
  Class<SecurityOrder> getEntityClass() {
    return SecurityOrder.class;
  }

  /**
   * Updates one record within the database given the DTO SecurityOrder.
   *
   * @param entity Entity representing the DTO.
   * @return returns an integer representing the number of records updated (expects 1).
   */
  @Override
  public int updateOne(SecurityOrder entity) {
    String updateSql = "UPDATE " + getTableName()
        + " SET notes=?, price=?, size=?, status=? WHERE " + getIdColumnName() + "=?";

    Object[] updatedValues = {
        entity.getNotes(),
        entity.getPrice(),
        entity.getSize(),
        entity.getStatus(),
        entity.getId()
    };

    return getJdbcTemplate().update(updateSql, updatedValues);
  }

  /**
   * Deletes all the records within the security_order table with a specific accountId.
   *
   * @param accountId accountId that owns/is related to specific security_order records.
   */
  public void deleteByAccountId(Integer accountId) {
    String deleteSql = "DELETE FROM " + getTableName()
        + " WHERE account_id=?";

    try {
      getJdbcTemplate().update(deleteSql, accountId);
    } catch (DataAccessException e) {
      logger.debug("Error deleting by account id: " + accountId, e);
    }
  }

  /**
   * Throws an UnsupportedOperationException since it isn't implemented.
   *
   * @param securityOrder DTO, however this method isn't implemented.
   */
  @Override
  public void delete(SecurityOrder securityOrder) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Throws an UnsupportedOperationException since it isn't implemented.
   *
   * @param iterable Iterable of the DTOs (SecurityOrder), however this method isn't implemented.
   */
  @Override
  public void deleteAll(Iterable<? extends SecurityOrder> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
