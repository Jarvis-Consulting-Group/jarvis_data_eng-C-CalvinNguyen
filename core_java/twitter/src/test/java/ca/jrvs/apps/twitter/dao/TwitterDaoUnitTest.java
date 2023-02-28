package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.example.TwitterApiTest;
import ca.jrvs.apps.twitter.model.v2.Data;
import ca.jrvs.apps.twitter.model.v2.Tweet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    Tweet tweet = twitterDao.findById("1629865830337990656");
    logger.info(tweet.toString());

  }

  @Test
  public void create() {
  }

  @Test
  public void deleteById() {
  }

  private static final String testStr = "{\n"
      + " \"data\": [\n"
      + "   {\n"
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
      + "   }\n"
      + " ]\n"
      + "}";
}