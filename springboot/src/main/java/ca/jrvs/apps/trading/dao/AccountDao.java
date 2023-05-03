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

/**
 * Implementation of the abstract class JdbcCrudDao with the DTO being the Account class.
 */
@Repository
public class AccountDao extends JdbcCrudDao<Account> {

  private static final Logger logger = LoggerFactory.getLogger(AccountDao.class);
  private static final String TABLE_NAME = "account";
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
  public AccountDao(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
        .withTableName(TABLE_NAME)
        .usingGeneratedKeyColumns(ID_COLUMN);
  }

  /**
   * Gets the JdbcTemplate.
   *
   * @return returns the JdbcTemplate initialized by the AccountDao.
   */
  @Override
  public JdbcTemplate getJdbcTemplate() {
    return this.jdbcTemplate;
  }

  /**
   * Gets the SimpleJdbcInsert.
   *
   * @return returns the SimpleJdbcInsert initialized by the AccountDao.
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
  Class<Account> getEntityClass() {
    return Account.class;
  }

  /**
   * Updates one record within the database given the DTO Account.
   *
   * @param entity Entity representing the DTO.
   * @return returns an integer representing the number of records updated (expects 1).
   */
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

  /**
   * finds an Account given the Integer traderId.
   *
   * @param traderId Integer traderId that owns the account.
   * @return returns an optional of the account (either empty or contains the account).
   */
  public Optional<Account> findByTraderId(Integer traderId) {
    Optional<Account> account = Optional.empty();
    String traderSql = "SELECT * FROM " + getTableName() + " WHERE trader_id=?";

    try {
      account = Optional.ofNullable(getJdbcTemplate()
          .queryForObject(
              traderSql,
              BeanPropertyRowMapper.newInstance(getEntityClass()),
              traderId));
    } catch (IncorrectResultSizeDataAccessException e) {
      logger.debug("Can't find account with trader id: " + traderId, e);
    }

    return account;
  }

  /**
   * Throws an UnsupportedOperationException since it isn't implemented.
   *
   * @param account DTO, however this method isn't implemented.
   */
  @Override
  public void delete(Account account) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Throws an UnsupportedOperationException since it isn't implemented.
   *
   * @param iterable iterable of the DTOs (account), however this method isn't implemented.
   */
  @Override
  public void deleteAll(Iterable<? extends Account> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
