package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.JsonUtil;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.v1.Tweet;
import ca.jrvs.apps.twitter.model.v2.TweetV2;
import ca.jrvs.apps.twitter.model.v2.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TwitterDao implements CrdDao<Tweet, String> {

  // URI Constants
  private static final String API_BASE_URI = "https://api.twitter.com";
  // API v1.1
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy";
  // API v2
  private static final String TWEET_PATH_V2 = "/2/tweets";
  // Symbols
  // symbols
  private static final String QUERY_SYM = "?";
  private static final String AMPERSAND = "&";
  private static final String EQUAL = "=";
  // OK Response Code
  private static final int HTTP_OK = 200;

  private HttpHelper httpHelper;
  private static final Logger logger = LoggerFactory.getLogger(TwitterDao.class);

  //@Autowired
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  public Tweet checkResponse(HttpResponse httpResponse, int statusCode) throws RuntimeException,
      IOException {

    if (httpResponse.getStatusLine().getStatusCode() != statusCode) {
      logger.debug(EntityUtils.toString(httpResponse.getEntity()));
      throw new RuntimeException("Error HTTP Status Code: "
          + httpResponse.getStatusLine().getStatusCode()
          + " "
          + httpResponse.getStatusLine().getReasonPhrase());
    }

    if (httpResponse.getEntity() == null) {
      throw new RuntimeException("Empty response body");
    }

    Tweet tweet = JsonUtil.toObjectFromJson(EntityUtils.toString(httpResponse.getEntity()), Tweet.class);
    return tweet;

  }

  public TweetV2 checkResponseV2(HttpResponse httpResponse, int statusCode) throws RuntimeException,
      IOException {

    if (httpResponse.getStatusLine().getStatusCode() != statusCode) {
      logger.debug(EntityUtils.toString(httpResponse.getEntity()));
      throw new RuntimeException("Error HTTP Status Code: "
          + httpResponse.getStatusLine().getStatusCode()
          + " "
          + httpResponse.getStatusLine().getReasonPhrase());
    }

    if (httpResponse.getEntity() == null) {
      throw new RuntimeException("Empty response body");
    }

    Data data = JsonUtil.toObjectFromJson(EntityUtils.toString(httpResponse.getEntity()), Data.class);
    return data.getTweet();

  }

  @Override
  public Tweet create(Tweet tweet) {
    Tweet responseTweet = null;
    try {
      PercentEscaper percentEscaper = new PercentEscaper("", false);
      URI uriV1 = new URI(API_BASE_URI
          + POST_PATH
          + QUERY_SYM + "status" + EQUAL + percentEscaper.escape(tweet.getText()));

      HttpResponse httpResponseV1 = httpHelper.httpPost(uriV1);
      responseTweet = checkResponse(httpResponseV1, 201);
      return responseTweet;

    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public TweetV2 createV2(TweetV2 tweetV2) {
    TweetV2 responseTweet = null;
    try {
      URI uriV2 = new URI(API_BASE_URI
          + TWEET_PATH_V2);

      String s = "{\n"
          + "\"text\":\"" + tweetV2.getText() + "\"\n"
          + "}";

      HttpResponse httpResponseV2 = httpHelper.httpPostV2(uriV2, s);

      responseTweet = checkResponseV2(httpResponseV2, 201);
      return responseTweet;

    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Tweet findById(String s) {
    Tweet tweet = null;

    try {
      PercentEscaper percentEscaper = new PercentEscaper("", false);
      URI uriV1 = new URI(API_BASE_URI
          + SHOW_PATH
          + QUERY_SYM + "id" + EQUAL + percentEscaper.escape(s));

      HttpResponse httpResponse = httpHelper.httpGet(uriV1);

      tweet = checkResponse(httpResponse, HTTP_OK);
      return tweet;

    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public TweetV2 findByIdV2(String s) {
    TweetV2 tweetV2 = null;

    try {
      URI uriV2 = new URI(API_BASE_URI
          + TWEET_PATH_V2 + "/"
          + s
          + QUERY_SYM + "tweet.fields" + EQUAL + "created_at,entities,public_metrics");

      HttpResponse httpResponse = httpHelper.httpGet(uriV2);
      tweetV2 = checkResponseV2(httpResponse, HTTP_OK);
      return tweetV2;

    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Tweet deleteById(String s) {
    Tweet tweet = null;

    try {
      PercentEscaper percentEscaper = new PercentEscaper("", false);
      URI uri = new URI(API_BASE_URI
          + DELETE_PATH + "/"
          + percentEscaper.escape(s) + ".json");

      HttpResponse httpResponse = httpHelper.httpDelete(uri);
      tweet = checkResponse(httpResponse, HTTP_OK);
      return tweet;

    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public TweetV2 deleteByIdV2(String s) {
    TweetV2 tweetV2 = null;

    try {
      PercentEscaper percentEscaper = new PercentEscaper("", false);
      URI uriv2 = new URI(API_BASE_URI
          + TWEET_PATH_V2 + "/"
          + percentEscaper.escape(s));

      // Does not return deleted tweet object, returns {data: {deleted:true}}
      HttpResponse httpResponse = httpHelper.httpDeleteV2(uriv2);
      /*
      logger.debug("Status Code: " + httpResponse.getStatusLine().getStatusCode()
          + " " + httpResponse.getStatusLine().getReasonPhrase());

       */
      //tweetV2 = checkResponseV2(httpResponse, HTTP_OK);
      return tweetV2;

    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
