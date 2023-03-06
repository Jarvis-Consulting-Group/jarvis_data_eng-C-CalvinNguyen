package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.v2.TweetV2;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The implementation of the service interface that sets the generic type as the TweetV2 object.
 */
@Component
public class TwitterServiceV2 implements Service<TweetV2> {

  private CrdDao dao;

  /**
   * Constructor that takes the DAO object dependency.
   * @param dao DAO object dependency that will be used.
   */
  @Autowired
  public TwitterServiceV2(CrdDao dao) {
    this.dao = dao;
  }

  @Override
  public TweetV2 postTweet(TweetV2 tweet) {
    // Business logic aka validate tweet text length
    validatePostTweet(tweet);

    // Use DAO to create tweet
    return (TweetV2) dao.create(tweet);
  }

  /**
   * Validates the TweetV2 object from the postTweet arguments, it checks if the String is less or
   * equals to 140 characters.
   * @param tweet the TweetV2 object passed from the postTweet object.
   */
  private void validatePostTweet(TweetV2 tweet) {

    if (tweet.getText().length() > 140) {
      throw new IllegalArgumentException("Tweet text length exceeds 140 characters.");
    }
  }

  @Override
  public TweetV2 showTweet(String id, String[] fields) {

    validateShowTweet(id, fields);

    TweetV2 responseTweet = (TweetV2) dao.findById(id);
    TweetV2 returnTweet = new TweetV2();

    if (fields == null) {
      returnTweet = responseTweet;
    } else {
      for (String field : fields) {
        switch (field) {
          case "id":
            returnTweet.setId(responseTweet.getId());
            break;
          case "text":
            returnTweet.setText(responseTweet.getText());
            break;
          case "entities":
            returnTweet.setEntities(responseTweet.getEntities());
            break;
          case "public_metrics":
            returnTweet.setPublicMetrics(responseTweet.getPublicMetrics());
            break;
          default:
            break;
        }
      }
    }

    return returnTweet;
  }

  /**
   * Validates the inputs from the showTweet method, it checks if the ID is valid and if the passed
   * String array fields is valid.
   * @param id the passed ID of a Tweet as a String, it can only be numerical and a 64-bit positive
   * integer.
   * @param fields the passed String array of fields, validates if they match the appropriate cases.
   */
  private void validateShowTweet(String id, String[] fields) {

    // Validate id
    checkId(id);


    if (fields != null) {

      // checks if each field is valid.
      for (String field : fields) {
        switch (field) {
          case "id":
            break;
          case "text":
            break;
          case "entities":
            break;
          case "public_metrics":
            break;
          default:
            throw new IllegalArgumentException(
                "Invalid field passed, can only be id, text, entities,"
                    + " or public_metrics");
        }
      }
    }
  }

  /**
   * Checks if the passed tweet ID as a String is numerical and a 64-bit positive integer.
   * @param id ID string passed from the validateShowTweet or validateDeleteTweet.
   */
  private void checkId(String id) {

    // checks if ID is all digits using regex
    if (!id.matches("[0-9]+")) {
      throw new IllegalArgumentException("Invalid ID: Twitter IDs are unique "
          + "64-bit unsigned Integers");
    }

    // checks if ID is 64-bit integer (Long in Java) that isn't negative
    try {
      long longId = Long.parseLong(id);
      if (longId < 0) {
        throw new IllegalArgumentException("Invalid ID (Must be positive)");
      }

    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid ID: Twitter IDs are unique "
          + "64-bit unsigned Integers", e);
    }
    
  }

  @Override
  public List<TweetV2> deleteTweets(String[] ids) {

    // Validate each tweet
    validateDeleteTweet(ids);

    // Return List of Deleted Tweets
    return deleteTweetList(ids);
    
  }

  /**
   * Calls to delete each TweetV2 object using the DAO.
   * @param ids String array of IDs.
   * @return returns the list of deleted tweets objects.
   */
  private List<TweetV2> deleteTweetList(String[] ids) {

    List<TweetV2> tweetV2List = new ArrayList<>();

    for (String id : ids) {
      TweetV2 returnTweet = (TweetV2) dao.deleteById(id);
      returnTweet.setId(id);
      tweetV2List.add(returnTweet);
    }

    return tweetV2List;
  }

  /**
   * Validates whether each ID within the string array ids is valid (numerical and 64-bit positive
   * integer).
   * @param ids the passed String array of ids.
   */
  private void validateDeleteTweet(String[] ids) {
    for (String id : ids) {
      // For each tweet call checkId(id), if it isn't valid throws IllegalArgumentException
      checkId(id);
    }
  }

}
