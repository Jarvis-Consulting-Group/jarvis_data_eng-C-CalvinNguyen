package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.JsonUtil;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.v1.Tweet;
import ca.jrvs.apps.twitter.model.v2.TweetV2;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {

  private static final Logger logger = LoggerFactory.getLogger(TwitterDaoUnitTest.class);

  @Mock
  HttpHelper mockHelper;

  @InjectMocks
  TwitterDao twitterDao;

  @Test
  public void showTweet() throws Exception {
    when(mockHelper.httpGet(isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      twitterDao.findById("1629865830337990656");
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    }

    when(mockHelper.httpGet(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(twitterDao);
    TweetV2 expectedTweet = JsonUtil.toObjectFromJson(testStr2, TweetV2.class);

    doReturn(expectedTweet).when(spyDao).checkResponse(any(), anyInt());
    Tweet tweet = spyDao.findById("1629865830337990656");
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }

  @Test
  public void postTweet() throws IOException {
    Tweet tempTweet = new Tweet();
    tempTweet.setText("Hello World!");

    when(mockHelper.httpPostV2(isNotNull(), isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      twitterDao.create(tempTweet);
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    }

    when(mockHelper.httpPostV2(isNotNull(), isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(twitterDao);
    Tweet expectedTweet = JsonUtil.toObjectFromJson(testStr2, Tweet.class);
    doReturn(expectedTweet).when(spyDao).checkResponse(any(), anyInt());
    Tweet tweet = spyDao.create(tempTweet);
    logger.info(tweet.toString());
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }

  @Test
  public void deleteTweet() throws IOException {
    String id = "1629865830337990656";

    when(mockHelper.httpDeleteV2(isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      twitterDao.deleteById(id);
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    }

    when(mockHelper.httpDeleteV2(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(twitterDao);
    Tweet expectedTweet = JsonUtil.toObjectFromJson(testStr2, Tweet.class);
    doReturn(expectedTweet).when(spyDao).checkResponse(any(), anyInt());
    Tweet tweet = spyDao.deleteById(id);
    assertNotNull(tweet);
  }

  private static final String testStr2 = "{\n"
      + "     \"id\":\"1629865830337990656\",\n"
      + "     \"text\":\"@HotForMoot Hey @HotForMoot ??, we've been hard at work developing our new free &amp; basic API tiers. We'll get back to you following the launch. \\n\\nHint: it's coming very soon!\",\n"
      + "     \"created_at\":\"2023-02-26T15:27:50.000Z\",\n"
      + "     \"entities\": {\n"
      + "       \"hashtags\": [{\n"
      + "         \"start\": 8,\n"
      + "         \"end\": 13,\n"
      + "         \"tag\": \"test\"\n"
      + "       }],\n"
      + "       \"mentions\": [{\n"
      + "         \"start\": 0,\n"
      + "         \"end\": 11,\n"
      + "         \"username\": \"HotForMoot\",\n"
      + "         \"id\":\"1519594215352668160\"\n"
      + "       },\n"
      + "       {\n"
      + "         \"start\": 16,\n"
      + "         \"end\": 27,\n"
      + "         \"username\": \"HotForMoot\",\n"
      + "         \"id\":\"1519594215352668160\"\n"
      + "       }]\n"
      + "       },\n"
      + "     \"public_metrics\": {\n"
      + "       \"retweet_count\": 1,\n"
      + "       \"reply_count\": 6,\n"
      + "       \"like_count\": 10,\n"
      + "       \"quote_count\": 3,\n"
      + "       \"impression_count\": 3244\n"
      + "     }\n"
      + "   }\n";
}