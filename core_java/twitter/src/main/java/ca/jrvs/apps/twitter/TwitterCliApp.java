package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.controller.Controller;
import ca.jrvs.apps.twitter.model.v2.TweetV2;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

/**
 * Main class used when calling the application, it takes the arguments and contains the controller
 * as a dependency. IT will pass the arguments to the appropriate controller arguments.
 */
public class TwitterCliApp {

  private final Controller controller;

  // Message to be presented if the arguments are illegal.
  private static final String USAGE = "USAGE: \n"
      + "TwitterApp post \"text\" \n"
      + "TwitterApp show \"tweet_id\" [fields1,fields2] \n"
      + "TwitterApp delete [tweet_id1,tweet_id2]";

  /**
   * Constructor that takes the controller object/dependency used by the application.
   *
   * @param controller Controller dependency passed in the constructor.
   */
  public TwitterCliApp(Controller controller) {
    this.controller = controller;
  }

  /**
   * Calls a specific method from the controller based on the arguments passed or throws an
   * exception.
   *
   * @param args String array of arguments passed when calling the application.
   */
  public void run(String[] args) {
    if (args.length > 3 || args.length <= 1) {
      throw new IllegalArgumentException(USAGE);
    }

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

        throw new IllegalArgumentException(USAGE);
    }
  }

  /**
   * prints the returned Tweet object as a JSON string by calling the JsonUtil.toJson static
   * method.
   *
   * @param tweetV2 the TweetV2 object returned from the controller.
   * @param nullVal boolean value on whether to include the null values or not.
   */
  private void printJson(TweetV2 tweetV2, boolean nullVal) {
    try {
      System.out.println(JsonUtil.toJson(tweetV2, true, nullVal));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  /*
  public static void main(String[] args) {
    if (args.length > 3 || args.length <= 1) {
      throw new IllegalArgumentException(USAGE);
    }

    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");

    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret,
        accessToken, tokenSecret);
    CrdDao dao = new TwitterDaoV2(httpHelper);
    Service service = new TwitterServiceV2(dao);
    Controller controller = new TwitterControllerV2(service);
    TwitterCliApp twitterCliApp = new TwitterCliApp(controller);

    twitterCliApp.run(args);
  }
   */

}
