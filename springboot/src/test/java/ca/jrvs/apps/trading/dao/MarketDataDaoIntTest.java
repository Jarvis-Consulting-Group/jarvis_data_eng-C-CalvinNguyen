package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Before;
import org.junit.Test;

public class MarketDataDaoIntTest {

  private MarketDataDao dao;

  /**
   * Sets up connection to the IEX REST API before each test
   *
   * @throws Exception exceptions thrown if unable to create connection, etc.
   */
  @Before
  public void setUp() throws Exception {
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(50);
    connectionManager.setDefaultMaxPerRoute(50);
    MarketDataConfig marketDataConfig = new MarketDataConfig();
    marketDataConfig.setHost("https://cloud.iexapis.com/v1");
    marketDataConfig.setToken(System.getenv("IEX_PUB_TOKEN"));

    dao = new MarketDataDao(connectionManager, marketDataConfig);
  }

  /**
   * Tests if MarketDataDao findById method works.
   */
  @Test
  public void findById() {
    String symbol = "AAPL";
    Optional<IexQuote> quoteOptional = dao.findById(symbol);

    if (quoteOptional.isPresent()) {
      IexQuote iexQuote = quoteOptional.get();
      assertEquals(symbol, iexQuote.getSymbol());
    } else {
      fail();
    }
  }

  /**
   * Tests if MarketDataDao findAllById works, and also tests if the method throws an
   * IllegalArgumentException when an invalid symbol/ticker is passed.
   *
   * @throws IOException May throw IOException.
   */
  @Test
  public void findAllById() throws IOException {
    List<IexQuote> iexQuoteList = dao.findAllById(Arrays.asList("AAPL", "FB"));
    assertEquals(2, iexQuoteList.size());
    assertEquals("AAPL", iexQuoteList.get(0).getSymbol());

    try {
      dao.findAllById(Arrays.asList("AAPL", "FB2"));
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
  }

  /**
   * Test to see if the MarketDataDao findById method throws an IllegalArgumentException when an
   * invalid symbol/ticker is passed.
   */
  @Test(expected = IllegalArgumentException.class)
  public void failFindById() {
    dao.findById("AAPL2");
  }
}