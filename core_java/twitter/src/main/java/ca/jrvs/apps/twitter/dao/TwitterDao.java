package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.v2.Tweet;
import ca.jrvs.apps.twitter.model.v2.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TwitterDao implements CrdDao<Tweet, String> {

  private static final Logger logger = LoggerFactory.getLogger(TwitterDao.class);

  // URI
  // constants
  private static final String API_BASE_URI = "https://api.twitter.com";
  /*
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "1.1/statuses/show.json";
  private static final String DELETE_PATH = "1.1/statuses/destroy";
   */
  private static final String SHOW_PATH = "/2/tweets/";

  // symbols
  private static final String QUERY_SYM = "?";
  private static final String AMPERSAND = "&";
  private static final String EQUAL = "=";

  // Response code
  private static final int HTTP_OK = 200;

  private HttpHelper httpHelper;

  //@Autowired
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  @Override
  public Tweet create(Tweet entity) {
    return null;
  }


  public static <T> T toObjectFromJson(String json, Class clazz) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    return (T) objectMapper.readValue(json, clazz);
  }

  private Tweet parseResponse(HttpResponse httpResponse) {
    Tweet tweet = null;

    int statusCode = httpResponse.getStatusLine().getStatusCode();
    if (statusCode != HTTP_OK) {
      try {
        logger.debug(EntityUtils.toString(httpResponse.getEntity()));
      } catch (IOException e) {
        throw new RuntimeException("Exception: ", e);
      }
      throw new RuntimeException("Error: HTTP status code: " + statusCode);
    }

    if (httpResponse.getEntity() == null) {
      throw new RuntimeException("No response body.");
    }

    String jsonStr;
    try {
      jsonStr = EntityUtils.toString(httpResponse.getEntity());
    } catch (IOException e) {
      throw new RuntimeException("Failed to convert entity to String", e);
    }

    try {
      Data data = toObjectFromJson(jsonStr, Data.class);
      tweet = data.getTweet();
    } catch (IOException e) {
      throw new RuntimeException("Could not convert JSON str to Object", e);
    }

    return tweet;
  }

  @Override
  public Tweet findById(String s) {
    try {
      URI uri = new URI(API_BASE_URI
          + SHOW_PATH
          + s
          + QUERY_SYM + "tweet.fields" + EQUAL + "created_at,entities,public_metrics");

      HttpResponse httpResponse = httpHelper.httpGet(uri);

      return parseResponse(httpResponse);
    } catch (URISyntaxException e) {
      throw new RuntimeException("Exception", e);
    }
  }

  @Override
  public Tweet deleteById(String s) {
    return null;
  }
}
