package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.JsonUtil;
import ca.jrvs.apps.twitter.model.v2.TweetV2;
import ca.jrvs.apps.twitter.service.Service;
import java.util.ArrayList;
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
public class TwitterControllerV2UnitTest {

  private static final Logger logger = LoggerFactory.getLogger(TwitterControllerV2UnitTest.class);

  @Mock
  Service service;

  @InjectMocks
  TwitterControllerV2 twitterControllerV2;

  @Test
  public void postTweet() {
    String[] testArr = {"post", "test", null};
    TweetV2 tempTweet = new TweetV2();
    tempTweet.setText("test");
    TwitterControllerV2 spyController = Mockito.spy(twitterControllerV2);
    doReturn(tempTweet).when(spyController).postTweet(any());
    TweetV2 tweetV2 = spyController.postTweet(testArr);
    logger.info(tweetV2.toString());
  }

  @Test
  public void showTweet() throws Exception{
    String[] testArr = {"show", "1629865830337990656", null};
    TweetV2 tempTweet = JsonUtil.toObjectFromJson(testStr2, TweetV2.class);
    TwitterControllerV2 spyController = Mockito.spy(twitterControllerV2);
    doReturn(tempTweet).when(spyController).showTweet(any());
    TweetV2 tweetV2 = spyController.showTweet(testArr);
    logger.info(tweetV2.toString());
  }

  @Test
  public void deleteTweet() {
    String[] testArr = {"delete", "1629865830337990656"};
    TweetV2 tempTweet = new TweetV2();
    tempTweet.setId("1629865830337990656");
    tempTweet.setDeleted(true);
    List<TweetV2> tempList = new ArrayList<TweetV2>();
    tempList.add(tempTweet);
    TwitterControllerV2 spyController = Mockito.spy(twitterControllerV2);
    doReturn(tempList).when(spyController).deleteTweet(any());
    List<TweetV2> tweetV2List = spyController.deleteTweet(testArr);
    logger.info(tweetV2List.toString());
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