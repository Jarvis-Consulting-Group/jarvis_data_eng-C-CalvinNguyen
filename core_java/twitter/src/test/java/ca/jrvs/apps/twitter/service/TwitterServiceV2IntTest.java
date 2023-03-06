package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDaoV2;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.v2.Data;
import ca.jrvs.apps.twitter.model.v2.TweetV2;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterServiceV2IntTest {

  private CrdDao dao;

  private TwitterServiceV2 twitterServiceV2;

  @Before
  public void setUp() throws Exception {
    String CONSUMER_KEY = System.getenv("consumerKey");
    String CONSUMER_SECRET = System.getenv("consumerSecret");
    String ACCESS_TOKEN = System.getenv("accessToken");
    String TOKEN_SECRET = System.getenv("tokenSecret");

    HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN,
        TOKEN_SECRET);

    dao = new TwitterDaoV2(httpHelper);
    twitterServiceV2 = new TwitterServiceV2(dao);
  }

  // Combined posting and deletion of tweets because Twitter does not allow duplicate texts.
  @Test
  public void postAndDeleteTweet() {
    TweetV2 tweetV2 = new TweetV2();

    //tweetV2.setText("gwaavsujiryzlbrzrjccxpeycssfxwnutaplibpgiiszizfkvqnfoqifbkzgrdeuslxixcwzatckz"
    //    + "yoxlmnukitohyzryokndwbtanuxmhxqpmcwbikkkwszuilfiuvjmbflcwrtvqigwekpuyytkbwoqysypbinbosm"
    //    + "avwxrlpzhpblfhmrxxmasbuqjslcurjuxydcwpvugizodruotjouvzhvnilzrkhckowczaphwqflgczbppwqcbw"
    //    + "ajyxrntcrvhfsnnmaetjxiibfqbmpoxamsajxjejaruoe");

    Data data = new Data();

    data.setText("Hello World! Test Tweet 1");
    tweetV2.setData(data);

    TweetV2 responseTweet = twitterServiceV2.postTweet(tweetV2);
    assertEquals(tweetV2.getData().getText(), responseTweet.getData().getText());

    String[] tempArr = {responseTweet.getData().getId()};

    List<TweetV2> deleteList = twitterServiceV2.deleteTweets(tempArr);
    assertNotNull(deleteList);
  }

  @Test
  public void showTweet() {
    String id1 = "1629865830337990656";
    String failId1 = "1629865830337990A56";

    String text =
        "@HotForMoot Hey @HotForMoot \uD83D\uDC4B, we've been hard at work developing our new free &amp; basic API tiers. We'll get back to you following the launch. \n"
            + "\n"
            + "Hint: it's coming very soon!";

    String[] fields = {"id", "text"};
    //String[] failFields = {"id", "tadwad"};

    TweetV2 responseTweet1 = twitterServiceV2.showTweet(id1, fields);
    assertEquals(text, responseTweet1.getData().getText());
  }
}