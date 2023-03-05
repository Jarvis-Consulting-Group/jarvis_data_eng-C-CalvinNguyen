package ca.jrvs.apps.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ca.jrvs.apps.twitter")
public class TwitterCliSpringBoot implements CommandLineRunner {

  private TwitterCliApp twitterCliApp;

  @Autowired
  public TwitterCliSpringBoot(TwitterCliApp twitterCliApp) {
    this.twitterCliApp = twitterCliApp;
  }

  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(TwitterCliSpringBoot.class);

    springApplication.setWebApplicationType(WebApplicationType.NONE);
    springApplication.run(args);
  }


  @Override
  public void run(String... args) throws Exception {
    twitterCliApp.run(args);
  }
}
