package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.controller.Controller;
import ca.jrvs.apps.twitter.controller.TwitterControllerV2;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDaoV2;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.v2.TweetV2;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterServiceV2;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public class TwitterCLIApp {

  private Controller controller;

  public TwitterCLIApp(Controller controller) {
    this.controller = controller;
  }

  public void run(String[] args) {
    switch (args[0]) {
      case "post":
        TweetV2 tweet = (TweetV2) controller.postTweet(args);
        printJson(tweet, true);
        break;
      case "show":
        TweetV2 tweet2 = (TweetV2) controller.showTweet(args);
          printJson(tweet2, args.length == 2);
        break;
      case "delete":
        List<TweetV2> tweetV2List = controller.deleteTweet(args);
        tweetV2List.forEach(tweet3 -> printJson(tweet3, false));
        break;
      default:
        throw new IllegalArgumentException("USAGE: TwitterApp post \"text\" \n"
            + "TwitterApp show \"tweet_id\" [fields1,fields2] \n"
            + "TwitterApp delete [tweet_id1,tweet_id2]");
    }
  }

  private void printJson(TweetV2 tweetV2, boolean nullVal) {
    try {
      System.out.println(JsonUtil.toJson(tweetV2, true, nullVal));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) {
    if (args.length > 3 || args.length <= 1) {
      throw new IllegalArgumentException("USAGE: TwitterApp post \"text\" \n"
          + "TwitterApp show \"tweet_id\" [fields1,fields2] \n"
          + "TwitterApp delete [tweet_id1,tweet_id2]");
    }

    String CONSUMER_KEY = System.getenv("consumerKey");
    String CONSUMER_SECRET = System.getenv("consumerSecret");
    String ACCESS_TOKEN = System.getenv("accessToken");
    String TOKEN_SECRET = System.getenv("tokenSecret");

    HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET,
        ACCESS_TOKEN, TOKEN_SECRET);
    CrdDao dao = new TwitterDaoV2(httpHelper);
    Service service = new TwitterServiceV2(dao);
    Controller controller = new TwitterControllerV2(service);
    TwitterCLIApp twitterCLIApp = new TwitterCLIApp(controller);

    twitterCLIApp.run(args);
  }

}
