package ca.jrvs.apps.twitter.controller;

import java.util.List;

/**
 * Controller takes the arguments and calls the appropriate service methods.
 *
 * @param <T> The Generic object will be a tweet object, for this instance we will be implementing
 *            with the TweetV2 model.
 */
public interface Controller<T> {

  /**
   * Calls the service's postTwwet method, and the arguments are the passed command arguments.
   *
   * @param args command arguments string array.
   * @return returns a TweetV2 object.
   */
  T postTweet(String[] args);

  /**
   * Calls the service's showTweet method, and the arguments are the passed command arguments.
   *
   * @param args command arguments string array.
   * @return returns a TweetV2 object.
   */
  T showTweet(String[] args);

  /**
   * Calls the services' deleteTweet method, and the arguments are the passed command arguments.
   *
   * @param args command arguments string array.
   * @return returns a List of TweetV2 objects.
   */
  List<T> deleteTweet(String[] args);
}
