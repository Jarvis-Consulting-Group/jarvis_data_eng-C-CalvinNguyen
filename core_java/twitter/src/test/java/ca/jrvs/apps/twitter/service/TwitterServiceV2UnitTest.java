package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDaoUnitTest;
import ca.jrvs.apps.twitter.model.v2.TweetV2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    when(dao.create(any())).thenReturn(new TweetV2());
    TweetV2 tweetV2 = twitterServiceV2.postTweet(tempTweet);
    logger.info(tweetV2.toString());
    assertNotNull(tweetV2);
    assertEquals(tempTweet.getText(), tweetV2.getText());
  }

  @Test
  public void showTweet() {
  }

  @Test
  public void deleteTweets() {
  }
}