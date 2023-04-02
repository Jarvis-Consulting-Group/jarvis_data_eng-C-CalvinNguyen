package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteDaoIntTest {

  @Autowired
  private QuoteDao quoteDao;

  private Quote savedQuote;

  /**
   * Creates a record within the database before each test.
   */
  @Before
  public void insertOne() {
    savedQuote = new Quote();
    savedQuote.setAskPrice(10d);
    savedQuote.setAskSize(10);
    savedQuote.setBidPrice(10.2d);
    savedQuote.setBidSize(10);
    savedQuote.setId("AAPL");
    savedQuote.setLastPrice(10.1d);
    quoteDao.save(savedQuote);
  }

  /**
   * Deletes all records from the database after each test.
   */
  @After
  public void deleteOne() {
    quoteDao.deleteById(savedQuote.getTicker());
    quoteDao.deleteAll();
  }

  /**
   * Saves a new record and updates a record.
   */
  @Test
  public void save() {
    Double expectedBidPrice = 10.5d;
    savedQuote.setBidPrice(expectedBidPrice);

    Quote quote = quoteDao.save(savedQuote);
    assertEquals(expectedBidPrice, quote.getBidPrice());

    Quote tempQuote = new Quote();
    tempQuote.setAskPrice(10d);
    tempQuote.setAskSize(10);
    tempQuote.setBidPrice(10.2d);
    tempQuote.setBidSize(10);
    tempQuote.setId("AAPL");
    tempQuote.setLastPrice(10.1d);
    Quote quote1 = quoteDao.save(tempQuote);
    assertEquals(tempQuote.getBidPrice(), quote1.getBidPrice());
  }

  /**
   * Updates a list of one quote using the QuoteDao saveAll method.
   */
  @Test
  public void saveAll() {
    List<Quote> updateList = new ArrayList<>();
    Double expectedBidPrice = 10.5d;
    savedQuote.setBidPrice(expectedBidPrice);
    updateList.add(savedQuote);

    List<Quote> quoteList = quoteDao.saveAll(updateList);
    assertEquals(updateList.get(0).getId(), quoteList.get(0).getId());

  }

  /**
   * Finds a quote by ID, if it isn't found then it will throw a runtime exception.
   * the catch clause fails this test if the runtime exception is thrown.
   */
  @Test
  public void findById() {
    try {
      Quote quote = quoteDao.findById("AAPL")
          .orElseThrow(() -> new RuntimeException(""));
      assertEquals(savedQuote.getId(), quote.getId());
    } catch (RuntimeException e) {
      fail();
    }
  }

  /**
   * Checks if the quote with the ticker AAPL exists within the database,
   * and asserts if the boolean is true.
   */
  @Test
  public void existsById() {
    boolean check = quoteDao.existsById("AAPL");
    assertTrue(check);
  }

  /**
   * Returns a list of all the quotes in the database
   * (there should only be one within the database in the tests).
   */
  @Test
  public void findAll() {
    List<Quote> quoteList = quoteDao.findAll();
    assertEquals(savedQuote.getId(), quoteList.get(0).getId());
  }

  /**
   * Returns an integer of the count of quotes within the database (it should be 1).
   */
  @Test
  public void count() {
    Long count = quoteDao.count();
    assertEquals(new Long(1), count);
  }

  /**
   * Not implemented, assert true.
   */
  @Test
  public void deleteById() {
    assertTrue(true);
  }

  /**
   * not implemented, assert true.
   */
  @Test
  public void deleteAll() {
    assertTrue(true);
  }
}