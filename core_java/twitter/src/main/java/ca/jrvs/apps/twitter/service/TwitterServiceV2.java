package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.v2.TweetV2;
import java.util.ArrayList;
import java.util.List;

public class TwitterServiceV2 implements Service<TweetV2> {

  private CrdDao dao;

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
            returnTweet.setPublic_metrics(responseTweet.getPublic_metrics());
            break;
          default:
            break;
        }
      }
    }

    return returnTweet;
  }

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

  private List<TweetV2> deleteTweetList(String[] ids) {

    List<TweetV2> tweetV2List = new ArrayList<>();

    for (String id : ids) {
      TweetV2 returnTweet = (TweetV2) dao.deleteById(id);
      returnTweet.setId(id);
      tweetV2List.add(returnTweet);
    }

    return tweetV2List;
  }

  private void validateDeleteTweet(String[] ids) {
    for (String id : ids) {
      // For each tweet call checkId(id), if it isn't valid throws IllegalArgumentException
      checkId(id);
    }
  }

}
