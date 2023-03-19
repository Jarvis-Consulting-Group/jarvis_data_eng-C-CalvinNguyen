package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import net.bytebuddy.pool.TypePool.Resolution.Illegal;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Before;
import org.junit.Test;

public class MarketDataDaoIntTest {

  private MarketDataDao dao;

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
}