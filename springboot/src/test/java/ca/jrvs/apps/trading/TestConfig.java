package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import javax.sql.DataSource;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"ca.jrvs.apps.trading.dao", "ca.jrvs.apps.trading.service"})
public class TestConfig {

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

  @Bean
  public HttpClientConnectionManager httpClientConnectionManager() {
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(50);
    connectionManager.setDefaultMaxPerRoute(50);

    return connectionManager;
  }

  @Bean
  public MarketDataConfig marketDataConfig() {
    MarketDataConfig config = new MarketDataConfig();
    config.setHost("https://cloud.iexapis.com/v1");
    config.setToken(System.getenv("IEX_PUB_TOKEN"));

    return config;
  }
}
