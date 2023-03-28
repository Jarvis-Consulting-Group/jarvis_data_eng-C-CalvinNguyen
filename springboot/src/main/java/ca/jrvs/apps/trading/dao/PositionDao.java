package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Position;
import com.sun.xml.internal.bind.v2.model.core.ID;
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

@Repository
public class PositionDao implements CrudRepository<Position, Integer> {

  private static final Logger logger = LoggerFactory.getLogger(PositionDao.class);
  private static final String TABLE_NAME = "position";
  private static final String ID_COLUMN = "account_id";
  private JdbcTemplate jdbcTemplate;

  @Autowired
  public PositionDao(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public Optional<Position> findByAccountIdAndTicker(Integer accountId, String ticker) {
    String findSql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COLUMN + "=? AND ticker=?";
    Optional<Position> position = Optional.empty();

    Object[] searchValues = {
        accountId,
        ticker
    };

    try {
      position = Optional.ofNullable((Position) this.jdbcTemplate
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

  public boolean existsByAccountIdAndTicker(Integer accountId, String ticker) {
    String existSql = "SELECT count(*) FROM " + TABLE_NAME
        + " WHERE " + ID_COLUMN + "=? AND ticker=?";

    Object[] searchValues = {
        accountId,
        ticker
    };

    Integer rowNum = this.jdbcTemplate.queryForObject(
        existSql,
        Integer.class,
        searchValues);

    if (rowNum == null) {
      return false;
    } else {
      return rowNum == 1;
    }
  }

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

  @Override
  public <S extends Position> S save(S s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public <S extends Position> Iterable<S> saveAll(Iterable<S> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Optional<Position> findById(Integer integer) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public boolean existsById(Integer integer) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Iterable<Position> findAll() {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public long count() {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteById(Integer integer) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void delete(Position position) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends Position> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll() {
    throw new UnsupportedOperationException("Not implemented");
  }
}
