package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class SecurityOrderDao extends JdbcCrudDao<SecurityOrder> {

  private static final Logger logger = LoggerFactory.getLogger(SecurityOrderDao.class);
  private static final String TABLE_NAME = "security_order";
  private static final String ID_COLUMN = "id";
  private JdbcTemplate jdbcTemplate;
  private SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public SecurityOrderDao(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
        .withTableName(TABLE_NAME)
        .usingGeneratedKeyColumns(ID_COLUMN);
  }


  @Override
  public JdbcTemplate getJdbcTemplate() {
    return this.jdbcTemplate;
  }

  @Override
  public SimpleJdbcInsert getSimpleJdbcInsert() {
    return this.simpleJdbcInsert;
  }

  @Override
  public String getTableName() {
    return TABLE_NAME;
  }

  @Override
  public String getIdColumnName() {
    return ID_COLUMN;
  }

  @Override
  Class<SecurityOrder> getEntityClass() {
    return SecurityOrder.class;
  }

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

  public void deleteByAccountId(Integer accountId) {
    String deleteSql = "DELETE FROM " + getTableName()
        + " WHERE account_id=?";

    try {
      getJdbcTemplate().update(deleteSql, accountId);
    } catch (DataAccessException e) {
      logger.debug("Error deleting by account id: " + accountId, e);
    }
  }

  @Override
  public void delete(SecurityOrder securityOrder) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends SecurityOrder> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
