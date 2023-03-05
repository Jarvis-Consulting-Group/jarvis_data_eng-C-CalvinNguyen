package ca.jrvs.apps.twitter.service;

import static jdk.internal.dynalink.support.Guards.isNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.JsonUtil;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.v2.TweetV2;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceV2UnitTest {

  private static final Logger logger = LoggerFactory.getLogger(TwitterServiceV2UnitTest.class);

  @Mock
  CrdDao dao;

  @InjectMocks
  TwitterServiceV2 twitterServiceV2;

  @Test
  public void postTweet() {
    TweetV2 tempTweet = new TweetV2();
    tempTweet.setText("test");

    TwitterServiceV2 spyService = Mockito.spy(twitterServiceV2);

    doReturn(tempTweet).when(spyService).postTweet(any());
    TweetV2 tweetV2 = spyService.postTweet(tempTweet);

    logger.info(tweetV2.toString());
    assertNotNull(tweetV2);
    assertEquals(tempTweet.getText(), tweetV2.getText());
  }

  @Test
  public void showTweet() throws Exception {

    TwitterServiceV2 spyService = Mockito.spy(twitterServiceV2);
    TweetV2 expectedTweet = JsonUtil.toObjectFromJson(testStr2, TweetV2.class);

    doReturn(expectedTweet).when(spyService).showTweet(any(), any());
    TweetV2 tweet = spyService.showTweet("1629865830337990656", null);

    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }

  @Test
  public void deleteTweets() {
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