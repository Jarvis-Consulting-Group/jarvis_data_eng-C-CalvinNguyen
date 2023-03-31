package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import java.util.Properties;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration file, tells Spring to manage the lifecycles of dependencies MarketDataConfig and
 * HttpClientConnectionManager.
 */
@Configuration
@EnableJpaRepositories(basePackages = "ca.jrvs.apps.trading.dao")
@EnableTransactionManagement
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
  /*
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

   */

  @Bean
  public DataSource dataSource() {
    String databaseUrl = "jdbc:postgresql://"
        + System.getenv("PSQL_HOST") + ":"
        + System.getenv("PSQL_PORT") + "/"
        + System.getenv("PSQL_DB");
    String databaseUser = System.getenv("PSQL_USER");
    String databasePassword = System.getenv("PSQL_PASSWORD");

    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName("org.postgresql.Driver");
    dataSource.setUrl(databaseUrl);
    dataSource.setUsername(databaseUser);
    dataSource.setPassword(databasePassword);

    return dataSource;
  }


  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean entityManager =
        new LocalContainerEntityManagerFactoryBean();
    entityManager.setDataSource(dataSource());
    entityManager.setPackagesToScan("ca.jrvs.apps.trading.model.domain");

    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    entityManager.setJpaVendorAdapter(vendorAdapter);

    Properties properties = new Properties();
    properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
    properties.put("hibernate.ddl-auto", "update");
    properties.put("hibernate.show_sql", "true");
    entityManager.setJpaProperties(properties);

    return entityManager;
  }

  @Bean
  public PlatformTransactionManager transactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

    return transactionManager;
  }
}
