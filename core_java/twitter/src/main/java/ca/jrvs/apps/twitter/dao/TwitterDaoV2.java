package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.JsonUtil;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
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
 * TwitterDaoV2 is the implementation of CrdDao for TweetV2 (specific to API V2) and Strings,
 * it will create, get, delete TweetV2 objects from the API using the HttpHelper (TwitterHttpHelper).
 */
public class TwitterDaoV2 implements CrdDao<TweetV2, String> {

  // URI Constants
  private static final String API_BASE_URI = "https://api.twitter.com";
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
  public TwitterDaoV2(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
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

  /**
   * createV2 calls the HttpHelper to execute a POST method at Twitter API V2 endpoint
   * that takes a json string for the text.
   * @param tweetV2 The TweetV2 that only contains text.
   * @return returns the TweetV2 object that is given in the response from HttpResponse.
   */
  @Override
  public TweetV2 create(TweetV2 tweetV2) {
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

  /**
   * Uses a string as tweet ID and creates a URI (Twitter API V2) which is executed by the HttpHelper
   * to get information for that tweet, it then returns a HttpResponse where we read from the response
   * body, to populate an object using the json properties.
   * @param s the String should be the ID for the Tweet.
   * @return it returns a TweetV2 object populated with the get request body json properties.
   */
  @Override
  public TweetV2 findById(String s) {
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

  /**
   * Uses Twitter API V2 endpoint, creates a URI given the id as a string. Calls the HTTPHelper
   * to execute the URI, and unforuntately API V2 doesn't return the deleted tweet, it only returns
   * a json looking like {data: {deleted: true}}.
   * @param s the id given as a string.
   * @return it is supposed to return a TweetV2 object but it returns null atm.
   */
  @Override
  public TweetV2 deleteById(String s) {
    TweetV2 tweetV2 = null;

    try {
      PercentEscaper percentEscaper = new PercentEscaper("", false);
      URI uriv2 = new URI(API_BASE_URI
          + TWEET_PATH_V2 + "/"
          + percentEscaper.escape(s));

      // Does not return deleted tweet object, returns {data: {deleted:true}}
      HttpResponse httpResponse = httpHelper.httpDeleteV2(uriv2);

      tweetV2 = checkResponseV2(httpResponse, HTTP_OK);
      return tweetV2;

    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
