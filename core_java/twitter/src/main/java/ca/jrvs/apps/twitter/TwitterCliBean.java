package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.controller.Controller;
import ca.jrvs.apps.twitter.controller.TwitterControllerV2;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDaoV2;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterServiceV2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwitterCliBean {

  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(TwitterCliBean.class);
    TwitterCliApp twitterCliApp = context.getBean(TwitterCliApp.class);
    twitterCliApp.run(args);
  }

  @Bean
  public TwitterCliApp twitterCliApp(Controller controller) {
    return new TwitterCliApp(controller);
  }

  @Bean
  public Controller controller(Service service) {
    return new TwitterControllerV2(service);
  }

  @Bean
  public Service service(CrdDao dao) {
    return new TwitterServiceV2(dao);
  }

  @Bean
  public CrdDao dao(HttpHelper httpHelper) {
    return new TwitterDaoV2(httpHelper);
  }

  @Bean
  HttpHelper helper() {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    return new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
  }

}
