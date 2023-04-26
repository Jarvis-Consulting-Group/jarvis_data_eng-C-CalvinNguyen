package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "ca.jrvs.apps.trading.dao")
@EnableTransactionManagement
@ComponentScan(basePackages = {"ca.jrvs.apps.trading.dao", "ca.jrvs.apps.trading.service"})
public class TestConfig {

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
