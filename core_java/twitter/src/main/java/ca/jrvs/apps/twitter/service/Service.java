package ca.jrvs.apps.twitter.service;

import java.util.List;

/**
 * Service to be implemented that contains the business logic, it will use the TwitterDao(V2) to
 * perform its actions. It also verifies if the inputs are acceptable, and if not throws exceptions.
 * @param <T> Generic type, can be implemented as Tweet or TweetV2.
 */
public interface Service<T> {

  /**
   * Validate the user's input and then post the tweet.
   * @param tweet object to be created/posted.
   * @return returns the created tweet from API.
   *
   * @throws IllegalArgumentException it will throw an exception if the number of characters exceeds
   * the maximum allowed.
   */
  T postTweet(T tweet);

  /**
   * Search/Find a tweet by the tweets ID.
   * @param id the ID of the tweet.
   * @param fields tweet fields to be returned.
   * @return TweetV2 object populated with information from Twitter API
   *
   * @throws IllegalArgumentException throws an exception if ID or Fields is invalid.
   */
  T showTweet(String id, String[] fields);

  /**
   * Delete one or more tweets by the id(s) in the array.
   * @param ids array of tweet IDs to be deleted.
   * @return return a list of tweets that were deleted.
   *
   * @throws IllegalArgumentException throws an exception if one of the IDs is invalid.
   */
  List<T> deleteTweets(String[] ids);
}
