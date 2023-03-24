package ca.jrvs.apps.trading.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.Arrays;
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
public class QuoteServiceIntTest {

  @Autowired
  private QuoteService quoteService;

  @Autowired
  private QuoteDao quoteDao;

  @Before
  public void setUp() throws Exception {
    quoteDao.deleteAll();
    quoteService.saveQuote("AMD");
  }

  @After
  public void after() throws Exception {
    quoteDao.deleteAll();
  }

  @Test
  public void findIexQuoteBySymbol() {
    String symbol = "AAPL";
    IexQuote iexQuote = quoteService.findIexQuoteBySymbol(symbol);
    assertEquals(symbol, iexQuote.getSymbol());
  }

  @Test
  public void findAllQuotes() {
    List<Quote> quoteList = quoteService.findAllQuotes();
    assertEquals("AMD", quoteList.get(0).getId());
  }

  @Test
  public void saveQuote() {
    Quote quote = null;
    if (quoteDao.findById("AMD").isPresent()) {
      quote = quoteDao.findById("AMD").get();
    } else {
      fail();
    }

    quote.setAskPrice(50.0);
    Quote assertQuote = quoteService.saveQuote(quote);
    assertEquals(Double.valueOf(50.0), Double.valueOf(assertQuote.getAskPrice()));
  }

  @Test
  public void updateMarketData() {
    quoteService.updateMarketData();

    List<Quote> quoteList = quoteService.findAllQuotes();
    assertEquals("AMD", quoteList.get(0).getId());
  }

  @Test
  public void saveQuotes() {
    quoteService.saveQuotes(Arrays.asList("AMD"));

    List<Quote> quoteList = quoteService.findAllQuotes();
    assertEquals("AMD", quoteList.get(0).getId());
  }
}