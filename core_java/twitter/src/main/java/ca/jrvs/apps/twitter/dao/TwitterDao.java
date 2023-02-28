package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.JsonUtil;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.v1.Tweet;
import ca.jrvs.apps.twitter.model.v2.Data;
import ca.jrvs.apps.twitter.model.v2.TweetV2;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TwitterDao is the implementation of CrdDao for Tweets and Strings, it will create, get, delete
 * Tweet objects from the API using the HttpHelper (TwitterHttpHelper).
 * There are also createV2, findByIdV2, and deleteByIdV2 methods which call the v2 API endpoints.
 */
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

  /**
   * checkResponse method makes sure that the httpResponse status code matches the expected status
   * code and if it doesn't, the method throws an exception. It also checks if the response body
   * isn't empty and if it is, it throws an exception.
   * If it passes all checks it creates a Tweet object from the response body json using the
   * toObjectFromJson method.
   * @param httpResponse httpResponse returned after executing using HttpHelper.
   * @param statusCode status code expected from the httpResponse status code.
   * @return returns a Tweet object populated by the properties in json.
   * @throws RuntimeException Throws an exception if the status code doesn't match or response body
   * is empty.
   * @throws IOException exception if the toObjectFromJson cannot map the json to the object.
   */
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

    return JsonUtil.toObjectFromJson(EntityUtils.toString(httpResponse.getEntity()), Tweet.class);
  }

  /**
   *  checkResponse method makes sure that the httpResponse status code matches the expected status
   *  code and if it doesn't, the method throws an exception. It also checks if the response body
   *  isn't empty and if it is, it throws an exception.
   *  If it passes all checks it creates a TweetV2 (Twitter API v2) object from the response body
   *  json using the toObjectFromJson method.
   * @param httpResponse httpResponse returned after executing using HttpHelper.
   * @param statusCode status code expected from the httpResponse status code.
   * @return returns a TweetV2 object (Twitter V2 API)
   * @throws RuntimeException Throws an exception if the status code doesn't match or response body
   * is empty.
   * @throws IOException exception if the toObjectFromJson cannot map the json to the object.
   */
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

  /**
   * createV2 calls the HttpHelper to execute a POST method at Twitter API V2 endpoint
   * that takes a json string for the text.
   * @param tweetV2 The TweetV2 that only contains text.
   * @return returns the TweetV2 object that is given in the response from HttpResponse.
   */
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

  /**
   * Uses a string as tweet ID and creates a URI (Twitter API V2) which is executed by the HttpHelper
   * to get information for that tweet, it then returns a HttpResponse where we read from the response
   * body, to populate an object using the json properties.
   * @param s the String should be the ID for the Tweet.
   * @return it returns a TweetV2 object populated with the get request body json properties.
   */
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

  /**
   * Uses Twitter API V2 endpoint, creates a URI given the id as a string. Calls the HTTPHelper
   * to execute the URI, and unforuntately API V2 doesn't return the deleted tweet, it only returns
   * a json looking like {data: {deleted: true}}.
   * @param s the id given as a string.
   * @return it is supposed to return a TweetV2 object but it returns null atm.
   */
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
