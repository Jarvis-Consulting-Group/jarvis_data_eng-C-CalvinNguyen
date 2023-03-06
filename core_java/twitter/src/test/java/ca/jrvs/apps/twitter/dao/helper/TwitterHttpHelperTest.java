package ca.jrvs.apps.twitter.dao.helper;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.example.TwitterApiTest;
import com.google.gdata.util.common.base.PercentEscaper;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterHttpHelperTest {

  private static final Logger logger = LoggerFactory.getLogger(TwitterApiTest.class);
  private static String CONSUMER_KEY = System.getenv("consumerKey");
  private static String CONSUMER_SECRET = System.getenv("consumerSecret");
  private static String ACCESS_TOKEN = System.getenv("accessToken");
  private static String TOKEN_SECRET = System.getenv("tokenSecret");

  @Test
  public void httpPost() throws Exception{
    HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN,
        TOKEN_SECRET);

    String status = "Hello World!";
    PercentEscaper percentEscaper = new PercentEscaper("", false);
    // Temporary URI
    URI uri = new URI("https://api.twitter.com/1.1/statuses/update.json?status="
        + percentEscaper.escape(status));

    HttpResponse httpResponse = httpHelper.httpPost(uri);
    logger.info(EntityUtils.toString(httpResponse.getEntity()));

  }

  // Worked 1630595194751000578
  @Test
  public void httpPostV2() throws Exception{
    HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN,
        TOKEN_SECRET);

    // Temporary URI
    URI uri = new URI("https://api.twitter.com/2/tweets");

    String textJson = "{\n"
        + "\"text\": \"Hello World!\"\n"
        + "}";

    HttpResponse httpResponse = httpHelper.httpPostV2(uri, textJson);
    logger.info(EntityUtils.toString(httpResponse.getEntity()));

  }

  @Test
  public void httpGet() throws Exception{
    HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN,
        TOKEN_SECRET);

    String tweetId = "1629865830337990656";
    PercentEscaper percentEscaper = new PercentEscaper("", false);

    String url = "https://api.twitter.com/1.1/statuses/show.json?id="
        + percentEscaper.escape(tweetId);

    URI uri = new URI(url);

    HttpResponse httpResponse = httpHelper.httpGet(uri);
    logger.info(EntityUtils.toString(httpResponse.getEntity()));
  }

  @Test
  public void httpGetV2() throws Exception{
    HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN,
        TOKEN_SECRET);

    String tweetId = "1629865830337990656";
    PercentEscaper percentEscaper = new PercentEscaper("", false);
    URI uri = new URI("https://api.twitter.com/2/tweets/"
        + percentEscaper.escape(tweetId)
        + "?tweet.fields=created_at,entities,public_metrics,geo&expansions=geo.place_id");

    HttpResponse httpResponse = httpHelper.httpGet(uri);
    logger.info(EntityUtils.toString(httpResponse.getEntity()));
  }

  @Test
  public void httpDelete() throws Exception{
    HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN,
        TOKEN_SECRET);

    String tweetId = "";
    PercentEscaper percentEscaper = new PercentEscaper("", false);
    // Temporary URI
    URI uri = new URI("https://api.twitter.com/1.1/statuses/destroy"
        +  percentEscaper.escape(tweetId)
        + ".json");

    HttpResponse httpResponse = httpHelper.httpDelete(uri);
    logger.info(EntityUtils.toString(httpResponse.getEntity()));
  }

  // Working
  @Test
  public void httpDeleteV2() throws Exception{
    HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN,
        TOKEN_SECRET);

    String tweetId = "1630595194751000578";
    PercentEscaper percentEscaper = new PercentEscaper("", false);
    // Temporary URI
    URI uri = new URI("https://api.twitter.com/2/tweets/" + percentEscaper.escape(tweetId));

    HttpResponse httpResponse = httpHelper.httpDeleteV2(uri);
    logger.info(EntityUtils.toString(httpResponse.getEntity()));
  }
}