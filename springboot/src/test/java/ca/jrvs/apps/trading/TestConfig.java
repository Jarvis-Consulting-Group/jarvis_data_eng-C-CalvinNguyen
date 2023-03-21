package ca.jrvs.apps.trading;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

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

}
