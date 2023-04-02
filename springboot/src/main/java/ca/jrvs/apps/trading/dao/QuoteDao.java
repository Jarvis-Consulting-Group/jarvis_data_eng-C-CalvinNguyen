package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Quote;
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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * The QuoteDao class implements the CrudRepository interface and performs operations for quote
 * objects and on the quote table within the database.
 */
@Repository
public class QuoteDao implements CrudRepository<Quote, String> {

  private static final String TABLE_NAME = "quote";
  private static final String ID_NAME = "ticker";
  private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);
  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  /**
   * The constructor takes a DataSource and uses it to create a JdbcTemplate and SimpleJdbcInsert to
   * perform operations on the database. The DataSource dependencies lifecycle is managed by the
   * Spring IoC.
   *
   * @param dataSource DataSource used to create new instances of the JdbcTemplate and
   *                   SimpleJdbcInsert
   */
  @Autowired
  public QuoteDao(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME);
  }

  /**
   * Checks if the quote object exists within the database, and if it does exist updates that quote
   * record. If the quote object doesn't exist within the database, it is saved/added it to the
   * database.
   *
   * @param s   Quote object that will be saved/added/updated (s extends Quote)
   * @param <S> S extends Quote.
   * @return returns the Quote object.
   */
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

  /**
   * Helper method used to update a quote within the database using the JdbcTemplate update method,
   * this method executes the SQL statement string with data from the object array updatedValues.
   *
   * @param quote Quote object that will be updated into the database.
   * @return integer from the jdbcTemplate update method representing how many records were updated
   * (1 is expected).
   */
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

  /**
   * Helper method used to add/save a quote into the database using the SimpleJdbcInsert execute
   * method. The execute method takes a SqlParameterSource which is created using the
   * BeanPropertySqlParameterSource with the quote object.
   *
   * @param quote Quote object to be saved/added into the database.
   * @throws IncorrectResultSizeDataAccessException throws this exception if the number of records
   *                                                is not equal to 1, meaning it wasn't
   *                                                added/saved.
   */
  private void addOneQuote(Quote quote) {
    SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(quote);
    int rowNum = simpleJdbcInsert.execute(sqlParameterSource);

    if (rowNum != 1) {
      throw new IncorrectResultSizeDataAccessException("Failed to insert into quote table",
          1, rowNum);
    }
  }

  /**
   * Given an iterable of quotes, the method saves each quote into the database by calling the save
   * method.
   *
   * @param iterable Iterable of quotes where each will be saved into the database.
   * @param <S>      S extends Quote.
   * @return returns a list of quote objects.
   */
  @Override
  public <S extends Quote> List<S> saveAll(Iterable<S> iterable) {
    Iterator<S> iterator = iterable.iterator();
    List<S> quoteList = new ArrayList<>();

    while (iterator.hasNext()) {
      quoteList.add(save(iterator.next()));
    }

    return quoteList;
  }

  /**
   * With the String ID/symbol/ticker, this method gets the quote by calling the JdbcTemplate
   * queryForObject method with the SQL String.
   *
   * @param s String representing the quote id/symbol/ticker.
   * @return returns an optional either containing the quote or is empty.
   */
  @Override
  public Optional<Quote> findById(String s) {
    String findSql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_NAME + "=?";
    Optional<Quote> optionalQuote = Optional.empty();

    try {
      optionalQuote = Optional.ofNullable(this.jdbcTemplate.queryForObject(
          findSql,
          BeanPropertyRowMapper.newInstance(Quote.class),
          s));

    } catch (EmptyResultDataAccessException e) {
      logger.debug("Error getting ticker from database: " + s, e);
    }

    return optionalQuote;
  }

  /**
   * This method takes the quote id/symbol/ticker and checks if it exists within the database by
   * comparing the count within the database, (if it is 1 it exists, else it doesn't).
   *
   * @param s String representing id/symbol/ticker.
   * @return returns a boolean if it exists within the database or not.
   */
  @Override
  public boolean existsById(String s) {
    String countSql = "SELECT count(*) FROM " + TABLE_NAME + " WHERE " + ID_NAME + "=?";
    int count = this.jdbcTemplate.queryForObject(countSql, Integer.class, s);

    return count == 1;
  }

  /**
   * Finds all the quotes within the database.
   *
   * @return returns the list of all the quotes in the database.
   */
  @Override
  public List<Quote> findAll() {
    String findSql = "SELECT * FROM " + TABLE_NAME;

    try {
      return this.jdbcTemplate.query(
          findSql,
          BeanPropertyRowMapper.newInstance(Quote.class));
    } catch (EmptyResultDataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * This method gets the count of all the quotes in the database.
   *
   * @return returns a long value of the count for all the quotes in the database.
   */
  @Override
  public long count() {
    String countSql = "SELECT count(*) FROM " + TABLE_NAME;
    Long count = this.jdbcTemplate.queryForObject(countSql, Long.class);

    if (count == null) {
      throw new DataRetrievalFailureException("Count of all quotes in the database is null.");
    } else {
      return count;
    }
  }

  /**
   * This method uses a String id/symbol/ticker to delete the quote within the database with the
   * JdbcTemplate update method.
   *
   * @param s String representing the id/symbol/ticker.
   * @throws DataRetrievalFailureException throws if the quote exists within the database but the
   *                                       integer returned from the JdbcTemplate update method is
   *                                       not 1 (meaning the quote wasn't deleted).
   * @throws IllegalArgumentException      throws this argument if the quote doesn't exist within
   *                                       the database.
   */
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

  /**
   * Deletes all quotes within the database.
   */
  @Override
  public void deleteAll() {
    String deleteSql = "DELETE FROM " + TABLE_NAME;
    this.jdbcTemplate.update(deleteSql);
  }

  /**
   * Not implemented.
   *
   * @param iterable Not implemented.
   */
  @Override
  public void deleteAll(Iterable<? extends Quote> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Not implemented.
   *
   * @param iterable Not implemented.
   * @return Not implemented.
   */
  @Override
  public Iterable<Quote> findAllById(Iterable<String> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Not implemented.
   *
   * @param quote Not implemented.
   */
  @Override
  public void delete(Quote quote) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
