package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.v2.TweetV2;
import ca.jrvs.apps.twitter.service.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of the Controller class that sets the generic type as the TweetV2 objects, it
 * takes the command arguments passed and calls the appropriate service methods.
 */
@Component
public class TwitterControllerV2 implements Controller<TweetV2> {

  private static final String COORD_SEP = ":";
  private static final String COMMA = ",";
  private final Service service;

  /**
   * Constructor with the passed service object/layer dependency.
   * @param service service dependency passed (it will most likely be TwitterServiceV2 object).
   */
  @Autowired
  public TwitterControllerV2(Service service) {
    this.service = service;
  }

  @Override
  public TweetV2 postTweet(String[] args) {
    TweetV2 tempTweet = new TweetV2();
    tempTweet.setText(args[1]);

    //String[] geoArr = args[2].split(COORD_SEP);

    return (TweetV2) service.postTweet(tempTweet);
  }

  @Override
  public TweetV2 showTweet(String[] args) {
    String[] fieldArr = null;

    if (args.length == 3) {
      if (args[2] != null) {
        fieldArr = args[2].split(COMMA);
      }
    }

    return (TweetV2) service.showTweet(args[1], fieldArr);
  }

  @Override
  public List<TweetV2> deleteTweet(String[] args) {
    String[] idArr = args[1].split(COMMA);

    return service.deleteTweets(idArr);
  }
}
