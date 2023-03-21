package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Quote;
import com.sun.org.apache.xpath.internal.operations.Quo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class QuoteDao implements CrudRepository<Quote, String> {

  private static final String TABLE_NAME = "quote";
  private static final String ID_NAME = "ticker";

  private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);
  private JdbcTemplate jdbcTemplate;
  private SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public QuoteDao(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME);
  }

  @Override
  public <S extends Quote> S save(S s) {
    if (existsById(s.getId())) {
      int updatedRowNum = updateOneQuote(s);
      if (updatedRowNum != 1) {
        throw new DataRetrievalFailureException("Quote exists however unable to "
            + "update the quote record");
      }
    } else {
      addOneQuote(s);
    }

    return s;
  }

  private int updateOneQuote(Quote quote) {
    String updateSql = "UPDATE " + TABLE_NAME + " SET last_price=?, bid_price=?, "
        + "bid_size=?, ask_price=?, ask_size=? WHERE " + ID_NAME + "=?";

    Object[] updatedValues = {
        quote.getLastPrice(),
        quote.getBidPrice(),
        quote.getBidSize(),
        quote.getAskPrice(),
        quote.getAskSize(),
        quote.getId()
    };

    return this.jdbcTemplate.update(updateSql, updatedValues);
  }

  private void addOneQuote(Quote quote) {
    SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(quote);
    int rowNum = simpleJdbcInsert.execute(sqlParameterSource);

    if (rowNum != 1) {
      throw new IncorrectResultSizeDataAccessException("Failed to insert into quote table",
          1, rowNum);
    }
  }

  @Override
  public <S extends Quote> List<S> saveAll(Iterable<S> iterable) {
    Iterator<S> iterator = iterable.iterator();
    List<S> quoteList = new ArrayList<>();

    while (iterator.hasNext()) {
      quoteList.add(save(iterator.next()));
    }

    return quoteList;
  }

  @Override
  public Optional<Quote> findById(String s) {
    String findSql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_NAME + "=?";

    try {
      Quote quote = this.jdbcTemplate.queryForObject(
          findSql,
          (resultSet, rowNum) -> {
            Quote tempQuote = new Quote();
            tempQuote.setId(resultSet.getString("ticker"));
            tempQuote.setAskPrice(resultSet.getDouble("ask_price"));
            tempQuote.setAskSize(resultSet.getInt("ask_size"));
            tempQuote.setBidPrice(resultSet.getDouble("bid_price"));
            tempQuote.setBidSize(resultSet.getInt("bid_size"));
            tempQuote.setLastPrice(resultSet.getDouble("last_price"));

            return tempQuote;
          },
          s);

      if (quote == null) {
        return Optional.empty();
      } else {
        return Optional.of(quote);
      }

    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public boolean existsById(String s) {
    String countSql = "SELECT count(*) FROM " + TABLE_NAME + " WHERE " + ID_NAME + "=?";
    int count = this.jdbcTemplate.queryForObject(countSql, Integer.class, s);

    return count == 1;
  }

  @Override
  public List<Quote> findAll() {
    String findSql = "SELECT * FROM " + TABLE_NAME;

    try {
      return this.jdbcTemplate.query(
          findSql,
          (resultSet, rowNum) -> {
            Quote tempQuote = new Quote();
            tempQuote.setId(resultSet.getString("ticker"));
            tempQuote.setAskPrice(resultSet.getDouble("ask_price"));
            tempQuote.setAskSize(resultSet.getInt("ask_size"));
            tempQuote.setBidPrice(resultSet.getDouble("bid_price"));
            tempQuote.setBidSize(resultSet.getInt("bid_size"));
            tempQuote.setLastPrice(resultSet.getDouble("last_price"));

            return tempQuote;
          });
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  @Override
  public long count() {
    String countSql = "SELECT count(*) FROM " + TABLE_NAME;
    long count = this.jdbcTemplate.queryForObject(countSql, Long.class);

    return count;
  }

  @Override
  public void deleteById(String s) {
    if (existsById(s)) {
      String deleteSql = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_NAME + "=?";
      int rowNum = this.jdbcTemplate.update(deleteSql, s);

      if (rowNum != 1) {
        throw new DataRetrievalFailureException("Quote exists however unable to "
            + "delete the quote record");
      }
    } else {
      throw new IllegalArgumentException("Quote by ticker: " + s + " doesn't exist.");
    }
  }

  @Override
  public void deleteAll() {
    String deleteSql = "DELETE FROM " + TABLE_NAME;
    this.jdbcTemplate.update(deleteSql);
  }

  @Override
  public Iterable<Quote> findAllById(Iterable<String> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void delete(Quote quote) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends Quote> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
