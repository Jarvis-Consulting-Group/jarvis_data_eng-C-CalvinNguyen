package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDaoV2;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.v2.TweetV2;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterServiceV2;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterControllerV2IntTest {

  private static final Logger logger = LoggerFactory.getLogger(TwitterControllerV2IntTest.class);

  private Service service;

  private Controller twitterController;

  @Before
  public void setUp() throws Exception {
    String CONSUMER_KEY = System.getenv("consumerKey");
    String CONSUMER_SECRET = System.getenv("consumerSecret");
    String ACCESS_TOKEN = System.getenv("accessToken");
    String TOKEN_SECRET = System.getenv("tokenSecret");

    HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN,
        TOKEN_SECRET);
    CrdDao dao = new TwitterDaoV2(httpHelper);
    service = new TwitterServiceV2(dao);
    twitterController = new TwitterControllerV2(service);

  }

  @Test
  public void postDeleteTweet() {
    String text = "Hello World! 4";

    String[] args = {"post", text};

    TweetV2 tweet = (TweetV2) twitterController.postTweet(args);
    logger.info("Posted Tweet: \n" + tweet.toString());
    String tempId = tweet.getId();
    assertEquals(text, tweet.getText());

    String[] args2 = {"delete", tempId};

    List<TweetV2> tweetV2List = twitterController.deleteTweet(args2);
    logger.info(tweetV2List.toString());
    tweetV2List.stream().forEach(tweet2 -> {
      assertEquals(tempId, tweet2.getId());
      assertTrue(tweet2.isDeleted());
    });
  }

  /**
   * Expects an IllegalArgumentException because the text is beyond 140 characters.
   */
  @Test(expected = IllegalArgumentException.class)
  public void postTweetException() {
    String text =
        "gwaavsujiryzlbrzrjccxpeycssfxwnutaplibpgiiszizfkvqnfoqifbkzgrdeuslxixcwzatckz\"\n"
            + "        + \"yoxlmnukitohyzryokndwbtanuxmhxqpmcwbikkkwszuilfiuvjmbflcwrtvqigwekpuyytkbwoqysypbinbosm\"\n"
            + "        + \"avwxrlpzhpblfhmrxxmasbuqjslcurjuxydcwpvugizodruotjouvzhvnilzrkhckowczaphwqflgczbppwqcbw\"\n"
            + "        + \"ajyxrntcrvhfsnnmaetjxiibfqbmpoxamsajxjejaruoe";

    String[] args = {"post", text};
    TweetV2 tweet = (TweetV2) twitterController.postTweet(args);
  }

  @Test
  public void showTweet() {
    String id1 = "1629865830337990656";
    String text =
        "@HotForMoot Hey @HotForMoot \uD83D\uDC4B, we've been hard at work developing our new free &amp; basic API tiers. We'll get back to you following the launch. \n"
            + "\n"
            + "Hint: it's coming very soon!";

    String[] args = {"show", id1, null};

    TweetV2 tweet = (TweetV2) twitterController.showTweet(args);
    logger.info("Returned Tweet: \n" + tweet.toString());
    assertEquals(id1, tweet.getId());
    assertEquals(text, tweet.getText());
  }

  /**
   * Expects IllegalArgumentException because the ID is invalid, either it is empty, not numerical,
   * or not a positive 64-bit number.
   */
  @Test(expected = IllegalArgumentException.class)
  public void showTweetException() {

    // tests for invalid id
    // empty id
    String id1 = "";
    String[] args = {"show", id1, null};

    TweetV2 tweet = (TweetV2) twitterController.showTweet(args);

    /*
    // Contains alphabetical character
    String id2 = "163136450239334A8125";
    String[] args2 = {"show", id2, null};

    TweetV2 tweet1 = (TweetV2) twitterController.showTweet(args2);

    // Not a positive 64-bit Numerical
    String id3 = "-1631364502393348125";
    String[] args3 = {"show", id3, null};

    TweetV2 tweet2 = (TweetV2) twitterController.showTweet(args3);

     */
  }

  /**
   * Expects IllegalArgumentException because the fields are invalid.
   */
  @Test(expected = IllegalArgumentException.class)
  public void showTweetFieldException() {
    // tests for invalid fields
    String id4 = "1629865830337990656";
    String[] args4 = {"show", id4, "id,textWa"};

    TweetV2 tweet3 = (TweetV2) twitterController.showTweet(args4);
  }

  /**
   * Expects an IllegalArgumentException because the id is not numerical or not a positive 64-bit
   * number..
   */
  @Test(expected = IllegalArgumentException.class)
  public void deleteTweetException() {
    // Contains alphabetical character
    String id1 = "163136450239334A8125";
    String[] args = {"delete", id1};

    List<TweetV2> tweetV2List = twitterController.deleteTweet(args);

    /*
    // Not a positive 64-bit Numerical
    String id2 = "-1631364502393348125";
    String[] args2 = {"delete", id2};

    List<TweetV2> tweetV2List1 = twitterController.deleteTweet(args2);

     */
  }
}