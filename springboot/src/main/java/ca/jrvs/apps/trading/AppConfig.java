package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.postgresql.ds.PGConnectionPoolDataSource;
import org.postgresql.ds.PGPoolingDataSource;
import org.postgresql.ds.PGSimpleDataSource;
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

  /**
   * Configures a Bean datasource that will be used as a dependency when initializing JdbcTemplates.
   * @return returns a datasource configured for the postgresql database.
   */
  @Bean
  public DataSource dataSource() {
    String databaseUrl = "jdbc:postgresql://"
        + System.getenv("PSQL_HOST") + ":"
        + System.getenv("PSQL_PORT") + "/"
        + System.getenv("PSQL_DB");
    String databaseUser = System.getenv("PSQL_USER");
    String databasePassword = System.getenv("PSQL_PASSWORD");

    PGSimpleDataSource dataSource = new PGSimpleDataSource();
    dataSource.setUrl(databaseUrl);
    dataSource.setUser(databaseUser);
    dataSource.setPassword(databasePassword);

    return dataSource;
  }
}
