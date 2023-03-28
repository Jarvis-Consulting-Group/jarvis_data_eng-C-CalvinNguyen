package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Account;
import java.util.Optional;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDao extends JdbcCrudDao<Account> {

  private static final Logger logger = LoggerFactory.getLogger(AccountDao.class);
  private static final String TABLE_NAME = "account";
  private static final String ID_COLUMN = "id";
  private JdbcTemplate jdbcTemplate;
  private SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public AccountDao(DataSource dataSource) {
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
  Class<Account> getEntityClass() {
    return Account.class;
  }

  @Override
  public int updateOne(Account entity) {
    String updateSql = "UPDATE " + getTableName()
        + " SET amount=? WHERE " + getIdColumnName() + "=?";

    Object[] updatedValues = {
        entity.getAmount(),
        entity.getId()
    };

    return getJdbcTemplate().update(updateSql, updatedValues);
  }

  public Optional<Account> findByTraderId(Integer traderId) {
    Optional<Account> account = Optional.empty();
    String traderSql = "SELECT * FROM " + getTableName() + " WHERE trader_id=?";

    try {
      account = Optional.ofNullable((Account)  getJdbcTemplate()
          .queryForObject(
              traderSql,
              BeanPropertyRowMapper.newInstance(getEntityClass()),
              traderId));
    } catch (IncorrectResultSizeDataAccessException e) {
      logger.debug("Can't find account with trader id: " + traderId, e);
    }

    return account;
  }

  @Override
  public void delete(Account account) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends Account> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
