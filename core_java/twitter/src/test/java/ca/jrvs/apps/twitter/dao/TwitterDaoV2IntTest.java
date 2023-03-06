package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.v2.Data;
import ca.jrvs.apps.twitter.model.v2.TweetV2;
import org.junit.Before;
import org.junit.Test;

public class TwitterDaoV2IntTest {

  private TwitterDaoV2 twitterDaoV2;

  @Before
  public void setUp() throws Exception {
    String CONSUMER_KEY = System.getenv("consumerKey");
    String CONSUMER_SECRET = System.getenv("consumerSecret");
    String ACCESS_TOKEN = System.getenv("accessToken");
    String TOKEN_SECRET = System.getenv("tokenSecret");

    HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
    twitterDaoV2 = new TwitterDaoV2(httpHelper);
  }

  // Combined the creation and deletion of tweets because Tweets cannot contain duplicate texts?
  @Test
  public void createAndDeleteById() {
    TweetV2 tweetV2 = new TweetV2();
    Data data = new Data();
    data.setText("Hello World! Temporary Test Tweet");
    tweetV2.setData(data);

    TweetV2 responseTweetV2 = twitterDaoV2.create(tweetV2);
    assertEquals(tweetV2.getData().getText(), responseTweetV2.getData().getText());

    TweetV2 deleteTweet = twitterDaoV2.deleteById(responseTweetV2.getData().getId());
    assertNotNull(deleteTweet);
  }

  @Test
  public void findById() {
    String text = "@HotForMoot Hey @HotForMoot \uD83D\uDC4B, we've been hard at work developing our new free &amp; basic API tiers. We'll get back to you following the launch. \n"
        + "\n"
        + "Hint: it's coming very soon!";
    String id = "1629865830337990656";

    TweetV2 tweetV2 = twitterDaoV2.findById(id);
    assertEquals(text, tweetV2.getData().getText());
    assertEquals(id, tweetV2.getData().getId());
  }
}