package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.controller.Controller;
import ca.jrvs.apps.twitter.controller.TwitterControllerV2;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDaoV2;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterServiceV2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class, defines the dependencies/objects that Spring will manage such as
 * TwitterCliApp, TwitterController, TwitterService, TwitterDao, and TwitterHttpHelper.
 */
@Configuration
public class TwitterCliBean {

  /*
  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(TwitterCliBean.class);
    TwitterCliApp twitterCliApp = context.getBean(TwitterCliApp.class);
    twitterCliApp.run(args);
  }
   */

  /**
   * Returns a Spring TwitterCliApp bean that has a controller dependency.
   *
   * @param controller Spring controller bean dependency
   * @return returns the new instantiated TwitterCliApp.
   */
  @Bean
  public TwitterCliApp twitterCliApp(Controller controller) {
    return new TwitterCliApp(controller);
  }

  /**
   * Returns a Spring Controller bean that has a service dependency.
   *
   * @param service Spring service bean dependency.
   * @return returns the new instantiated TwitterControllerV2
   */
  @Bean
  public Controller controller(Service service) {
    return new TwitterControllerV2(service);
  }

  /**
   * Returns a Spring Service bean that has a DAO dependency.
   *
   * @param dao Spring DAO bean dependency.
   * @return returns the new instantiated TwitterServiceV2.
   */
  @Bean
  public Service service(CrdDao dao) {
    return new TwitterServiceV2(dao);
  }

  /**
   * Returns a Spring DAO bean that has a HttpHelper dependency.
   *
   * @param httpHelper Spring HttpHelper bean dependency.
   * @return returns the new instantiated TwitterDaoV2 dependency.
   */
  @Bean
  public CrdDao dao(HttpHelper httpHelper) {
    return new TwitterDaoV2(httpHelper);
  }

  /**
   * Returns a Spring HttpHelper bean that is set up using the environment variables.
   *
   * @return returns the new instantiated TwitterHttpHelper.
   */
  @Bean
  HttpHelper helper() {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    return new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
  }

}
