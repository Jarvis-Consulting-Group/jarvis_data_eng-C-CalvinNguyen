package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.JsonUtil;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDaoV2;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.v1.Tweet;
import ca.jrvs.apps.twitter.model.v2.TweetV2;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterServiceV2;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TwitterControllerV2 implements Controller<TweetV2> {

  private static final Logger logger = LoggerFactory.getLogger(TwitterControllerV2.class);
  private static final String COORD_SEP = ":";
  private static final String COMMA = ",";

  private Service service;

  //@Autowired
  public TwitterControllerV2(Service service) {
    this.service = service;
  }

  @Override
  public TweetV2 postTweet(String[] args) {
    TweetV2 tempTweet = new TweetV2();
    tempTweet.setText(args[1]);

    //String[] geoArr = args[2].split(COORD_SEP);

    TweetV2 returnTweet = (TweetV2) service.postTweet(tempTweet);

    return returnTweet;
  }

  @Override
  public TweetV2 showTweet(String[] args) {
    String[] fieldArr = null;

    if (args.length == 3) {
      if (args[2] != null) {
        fieldArr = args[2].split(COMMA);
      }
    }

    TweetV2 returnTweet = (TweetV2) service.showTweet(args[1], fieldArr);

    return returnTweet;
  }

  @Override
  public List<TweetV2> deleteTweet(String[] args) {
    String[] idArr = args[1].split(COMMA);

    List<TweetV2> returnList = service.deleteTweets(idArr);

    return returnList;
  }
}
