package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Trader;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the abstract class JdbcCrudDao with the DTO being the Trader class.
 */
@Repository
public class TraderDao extends JdbcCrudDao<Trader> {

  private static final Logger logger = LoggerFactory.getLogger(TraderDao.class);
  private static final String TABLE_NAME = "trader";
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
  public TraderDao(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
        .withTableName(TABLE_NAME)
        .usingGeneratedKeyColumns(ID_COLUMN);
  }

  /**
   * Gets the JdbcTemplate.
   *
   * @return returns the JdbcTemplate initialized by the TraderDao.
   */
  @Override
  public JdbcTemplate getJdbcTemplate() {
    return this.jdbcTemplate;
  }

  /**
   * Gets the SimpleJdbcInsert.
   *
   * @return returns the SimpleJdbcInsert initialized by the TraderDao.
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
  Class<Trader> getEntityClass() {
    return Trader.class;
  }

  /**
   * Throws an UnsupportedOperationException since it isn't implemented.
   *
   * @param entity Entity representing the DTO.
   * @return Throws an UnsupportedOperationException since it isn't implemented.
   */
  @Override
  public int updateOne(Trader entity) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Throws an UnsupportedOperationException since it isn't implemented.
   *
   * @param iterable Iterable of the DTO, however this method isn't implemented.
   */
  @Override
  public void deleteAll(Iterable<? extends Trader> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Throws an UnsupportedOperationException since it isn't implemented.
   *
   * @param trader Trader object to be deleted, however this method isn't implemented.
   */
  @Override
  public void delete(Trader trader) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
