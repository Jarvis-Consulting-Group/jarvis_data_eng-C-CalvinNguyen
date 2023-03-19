package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration file, tells Spring to manage the lifecycles of dependencies MarketDataConfig and
 * HttpClientConnectionManager.
 */
@Configuration
public class AppConfig {

  private Logger logger = LoggerFactory.getLogger(AppConfig.class);

  /**
   * Spring initializes the MarketDataConfig object with values, it is a dependency for the
   * MarketDataDao.
   * @return returns the MarketDataConfig with initialized values.
   */
  @Bean
  public MarketDataConfig marketDataConfig() {
    MarketDataConfig config = new MarketDataConfig();
    config.setHost("https://cloud.iexapis.com/v1");
    config.setToken(System.getenv("IEX_PUB_TOKEN"));

    return config;
  }

  /**
   * Spring initializes the HttpClientConnectionManager object with values, it is a dependency for
   * the MarketDataDao.
   * @return returns the HttpClientConnectionManager with initialized values.
   */
  @Bean
  public HttpClientConnectionManager httpClientConnectionManager() {
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(50);
    connectionManager.setDefaultMaxPerRoute(50);

    return connectionManager;
  }
}
