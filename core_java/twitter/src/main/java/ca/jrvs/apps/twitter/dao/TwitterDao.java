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
 */
public class TwitterDao implements CrdDao<Tweet, String> {

  // URI Constants
  private static final String API_BASE_URI = "https://api.twitter.com";
  // API v1.1
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy";
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
}
