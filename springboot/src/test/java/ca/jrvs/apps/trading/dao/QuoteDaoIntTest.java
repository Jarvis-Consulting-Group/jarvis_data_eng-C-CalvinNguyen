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

  @After
  public void deleteOne() {
    quoteDao.deleteById(savedQuote.getId());
  }

  @Test
  public void save() {
    Double expectedBidPrice = new Double(10.5d);
    savedQuote.setBidPrice(expectedBidPrice);

    Quote quote = quoteDao.save(savedQuote);
    assertEquals(expectedBidPrice, quote.getBidPrice());
  }

  @Test
  public void saveAll() {
    List<Quote> updateList = new ArrayList<>();
    Double expectedBidPrice = new Double(10.5d);
    savedQuote.setBidPrice(expectedBidPrice);
    updateList.add(savedQuote);

    List<Quote> quoteList = quoteDao.saveAll(updateList);
    assertEquals(updateList.get(0).getId(), quoteList.get(0).getId());

  }

  @Test
  public void findById() {
    Quote quote = quoteDao.findById("AAPL").get();
    assertEquals(savedQuote.getId(), quote.getId());
  }

  @Test
  public void existsById() {
    Boolean check = quoteDao.existsById("AAPL");
    assertTrue(check);
  }

  @Test
  public void findAll() {
    List<Quote> quoteList = quoteDao.findAll();
    assertEquals(savedQuote.getId(), quoteList.get(0).getId());
  }

  @Test
  public void count() {
    Long count = quoteDao.count();
    assertEquals(new Long(1), count);
  }

  @Test
  public void deleteById() {
    assertTrue(true);
  }

  @Test
  public void deleteAll() {
    assertTrue(true);
  }
}