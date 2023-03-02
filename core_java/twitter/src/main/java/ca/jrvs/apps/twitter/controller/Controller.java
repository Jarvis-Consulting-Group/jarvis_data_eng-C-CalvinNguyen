package ca.jrvs.apps.twitter.controller;

import java.util.List;

public interface Controller<T> {

  T postTweet(String[] args);

  T showTweet(String[] args);

  List<T> deleteTweet(String[] args);
}
