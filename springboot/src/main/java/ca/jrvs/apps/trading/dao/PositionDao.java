package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * PositionDao utilized for working with the position view within the database, and this DAO
 * implements the CrudRepository.
 */
@Repository
public class PositionDao implements CrudRepository<Position, Integer> {

  private static final Logger logger = LoggerFactory.getLogger(PositionDao.class);
  private static final String TABLE_NAME = "position";
  private static final String ID_COLUMN = "account_id";
  private final JdbcTemplate jdbcTemplate;

  /**
   * Constructor that takes a DataSource and initializes the JdbcTemplate using this DataSource.
   * Spring IoC manages the lifecycle of this component and all dependencies. The JdbcTemplate is
   * used to get the data from the position view.
   *
   * @param dataSource DataSource used to initialize the JdbcTemplate.
   */
  @Autowired
  public PositionDao(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  /**
   * Finds a position given the accountId and the quote symbol/ticker.
   *
   * @param accountId Integer for the accountId in the position.
   * @param ticker    String symbol/ticker for the position.
   * @return returns an optional object which could be empty or contain the position object.
   */
  public Optional<Position> findByAccountIdAndTicker(Integer accountId, String ticker) {
    String findSql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COLUMN + "=? AND ticker=?";
    Optional<Position> position = Optional.empty();

    Object[] searchValues = {
        accountId,
        ticker
    };

    try {
      position = Optional.ofNullable(this.jdbcTemplate
          .queryForObject(
              findSql,
              BeanPropertyRowMapper.newInstance(Position.class),
              searchValues
          ));
    } catch (EmptyResultDataAccessException e) {
      logger.debug("Can't find with account id " + accountId + " and ticker: " + ticker, e);
    }

    return position;
  }

  /**
   * Gets a List of positions within the database given the iterable integer for account id (should
   * be a list of 1 id).
   *
   * @param iterable Iterable for account id and should only contain 1 ID.
   * @return returns the list of positions related to the accountId.
   */
  @Override
  public List<Position> findAllById(Iterable<Integer> iterable) {
    List<Position> positionList = new ArrayList<>();
    Integer accountId = iterable.iterator().next();
    String findAccountSql = "SELECT * FROM " + TABLE_NAME
        + " WHERE " + ID_COLUMN + "=?";

    try {
      positionList = this.jdbcTemplate.query(
          findAccountSql,
          BeanPropertyRowMapper.newInstance(Position.class),
          accountId);
    } catch (IncorrectResultSizeDataAccessException e) {
      logger.debug("Can't find positions with account id: " + accountId, e);
    }

    return positionList;
  }

  /**
   * Throws an UnsupportedOperationException since it isn't implemented.
   *
   * @param s   s extends position, however this method is not implemented.
   * @param <S> S extends Position.
   * @return this method is not implemented.
   */
  @Override
  public <S extends Position> S save(S s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Throws an UnsupportedOperationException since it isn't implemented.
   *
   * @param iterable Iterable of positions, however this method is not implemented.
   * @param <S>      S extends position.
   * @return this method is not implemented.
   */
  @Override
  public <S extends Position> Iterable<S> saveAll(Iterable<S> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Throws an UnsupportedOperationException since it isn't implemented.
   *
   * @param integer Integer representing the position ID, however position is a view and this method
   *                isn't implemented.
   * @return this method isn't implemented.
   */
  @Override
  public Optional<Position> findById(Integer integer) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Throws an UnsupportedOperationException since it isn't implemented.
   *
   * @param integer Integer representing position ID, however position is a view and this method
   *                isn't implemented.
   * @return this method isn't implemented.
   */
  @Override
  public boolean existsById(Integer integer) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Throws an UnsupportedOperationException since it isn't implemented.
   *
   * @return this method isn't implemented.
   */
  @Override
  public Iterable<Position> findAll() {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Throws an UnsupportedOperationException since it isn't implemented.
   *
   * @return this method isn't implemented.
   */
  @Override
  public long count() {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Throws an UnsupportedOperationException since it isn't implemented.
   *
   * @param integer this method isn't implemented.
   */
  @Override
  public void deleteById(Integer integer) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Throws an UnsupportedOperationException since it isn't implemented.
   *
   * @param position this method isn't implemented.
   */
  @Override
  public void delete(Position position) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Throws an UnsupportedOperationException since it isn't implemented.
   *
   * @param iterable this method isn't implemented.
   */
  @Override
  public void deleteAll(Iterable<? extends Position> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Throws an UnsupportedOperationException since it isn't implemented.
   */
  @Override
  public void deleteAll() {
    throw new UnsupportedOperationException("Not implemented");
  }
}
