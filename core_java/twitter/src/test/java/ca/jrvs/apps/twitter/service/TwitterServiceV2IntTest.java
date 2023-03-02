package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDaoV2;
import ca.jrvs.apps.twitter.dao.TwitterDaoV2IntTest;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.v1.Tweet;
import ca.jrvs.apps.twitter.model.v2.TweetV2;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterServiceV2IntTest {

  private static final Logger logger = LoggerFactory.getLogger(TwitterServiceV2IntTest.class);

  private CrdDao dao;

  private TwitterServiceV2 twitterServiceV2;

  @Before
  public void setUp() throws Exception {
    String CONSUMER_KEY = System.getenv("consumerKey");
    String CONSUMER_SECRET = System.getenv("consumerSecret");
    String ACCESS_TOKEN = System.getenv("accessToken");
    String TOKEN_SECRET = System.getenv("tokenSecret");

    HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);

    dao = new TwitterDaoV2(httpHelper);
    twitterServiceV2 = new TwitterServiceV2(dao);
  }

  @Test
  public void postTweet() {
    TweetV2 tweetV2 = new TweetV2();

    /*
    tweetV2.setText("gwaavsujiryzlbrzrjccxpeycssfxwnutaplibpgiiszizfkvqnfoqifbkzgrdeuslxixcwzatckz"
        + "yoxlmnukitohyzryokndwbtanuxmhxqpmcwbikkkwszuilfiuvjmbflcwrtvqigwekpuyytkbwoqysypbinbosm"
        + "avwxrlpzhpblfhmrxxmasbuqjslcurjuxydcwpvugizodruotjouvzhvnilzrkhckowczaphwqflgczbppwqcbw"
        + "ajyxrntcrvhfsnnmaetjxiibfqbmpoxamsajxjejaruoe");
     */

    tweetV2.setText("Hello World!");

    TweetV2 responseTweet = twitterServiceV2.postTweet(tweetV2);
    logger.info(responseTweet.toString());
    assertEquals(tweetV2.getText(), responseTweet.getText());
  }

  @Test
  public void showTweet() {
    String id1 = "1629865830337990656";
    String failId1 = "1629865830337990A56";

    String text = "@HotForMoot Hey @HotForMoot \uD83D\uDC4B, we've been hard at work developing our new free &amp; basic API tiers. We'll get back to you following the launch. \n"
        + "\n"
        + "Hint: it's coming very soon!";

    String[] fields = {"id", "text"};
    //String[] failFields = {"id", "tadwad"};

    TweetV2 responseTweet1 = twitterServiceV2.showTweet(failId1, fields);
    logger.info(responseTweet1.toString());
    assertEquals(text, responseTweet1.getText());

  }

  @Test
  public void deleteTweets() {
  }
}