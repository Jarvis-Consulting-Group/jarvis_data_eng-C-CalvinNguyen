package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.v1.Tweet;
import ca.jrvs.apps.twitter.model.v2.TweetV2;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterDaoIntTest {

  private static final Logger logger = LoggerFactory.getLogger(TwitterDaoIntTest.class);
  private TwitterDao twitterDao;
  @Before
  public void setUp() throws Exception {
    String CONSUMER_KEY = System.getenv("consumerKey");
    String CONSUMER_SECRET = System.getenv("consumerSecret");
    String ACCESS_TOKEN = System.getenv("accessToken");
    String TOKEN_SECRET = System.getenv("tokenSecret");

    HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
    twitterDao = new TwitterDao(httpHelper);
  }

  @Test
  public void create() {
    TweetV2 tweetV2 = new TweetV2();

    tweetV2.setText("Hello World!");

    TweetV2 responseTweetV2 = twitterDao.createV2(tweetV2);
    logger.info(responseTweetV2.toString());
    assertEquals(tweetV2.getText(), responseTweetV2.getText());
  }

  // 201 Created 1630599940597489664
  @Test
  public void createV2() {
    TweetV2 tweetV2 = new TweetV2();

    tweetV2.setText("Hello World!");

    TweetV2 responseTweetV2 = twitterDao.createV2(tweetV2);
    logger.info(responseTweetV2.toString());
    assertEquals(tweetV2.getText(), responseTweetV2.getText());
  }

  @Test
  public void findById() {

    String text = "@HotForMoot Hey @HotForMoot \uD83D\uDC4B, we've been hard at work developing our new free &amp; basic API tiers. We'll get back to you following the launch. \n"
        + "\n"
        + "Hint: it's coming very soon!";
    String id = "1629865830337990656";

    Tweet tweet = twitterDao.findById(id);
    logger.info(tweet.toString());
    assertEquals(text, tweet.getText());
    assertEquals(id, tweet.getId_str());
  }

  @Test
  public void findByIdV2() {

    String text = "@HotForMoot Hey @HotForMoot \uD83D\uDC4B, we've been hard at work developing our new free &amp; basic API tiers. We'll get back to you following the launch. \n"
        + "\n"
        + "Hint: it's coming very soon!";
    String id = "1629865830337990656";

    TweetV2 tweetV2 = twitterDao.findByIdV2(id);
    logger.info(tweetV2.toString());
    assertEquals(text, tweetV2.getText());
    assertEquals(id, tweetV2.getId());
  }

  @Test
  public void deleteById() {
    String id = "1630608185227608066";

    Tweet tweet = twitterDao.deleteById(id);
    assertEquals("Hello World!", tweet.getText());
    assertEquals(id, tweet.getId_str());
  }

  @Test
  public void deleteByIdV2() {
    String id = "1630608185227608066";

    TweetV2 tweetV2 = twitterDao.deleteByIdV2(id);
  }
}