package ca.jrvs.apps.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class when starting the application, it implements the CommandLineRunner and utilizes Spring
 * for handling component lifecycles/dependencies configurations specified in the TwitterCliBean.
 */
@SpringBootApplication(scanBasePackages = "ca.jrvs.apps.twitter")
public class TwitterCliSpringBoot implements CommandLineRunner {

  private TwitterCliApp twitterCliApp;

  /**
   * Constructor @Autowired that takes a TwitterCLiApp bean. Spring will handle the lifecycle.
   * @param twitterCliApp TwitterCliApp bean.
   */
  @Autowired
  public TwitterCliSpringBoot(TwitterCliApp twitterCliApp) {
    this.twitterCliApp = twitterCliApp;
  }

  /**
   * Main method and entry point into the application, it sets up the spring application with
   * configurations from TwitterCliBean,
   * @param args String array command arguments.
   */
  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(TwitterCliSpringBoot.class);

    springApplication.setWebApplicationType(WebApplicationType.NONE);
    springApplication.run(args);
  }

  /**
   * Overridden/implemented CommandLineRunner run method.
   * @param args command arguments passed from main method
   * @throws Exception Exception can be thrown.
   */
  @Override
  public void run(String... args) throws Exception {
    twitterCliApp.run(args);
  }
}
