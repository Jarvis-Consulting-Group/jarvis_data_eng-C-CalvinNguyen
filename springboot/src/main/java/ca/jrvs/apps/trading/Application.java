package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * Main Entry point into the spring application, ALl lifecycles of dependencies are managed by
 * Spring, and the WebServlet is run which listens for requests from a client and calls the
 * appropriate Controller based on the path.
 */
@SpringBootApplication(exclude = {JdbcTemplateAutoConfiguration.class,
    DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class Application implements CommandLineRunner {

  private Logger logger = LoggerFactory.getLogger(Application.class);

  @Value("${app.init.dailyList}")
  private String[] initDailyList;

  @Autowired
  private QuoteService quoteService;

  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(Application.class);
    springApplication.run(args);
  }

  @Override
  public void run(String... args) throws Exception {

  }
}
