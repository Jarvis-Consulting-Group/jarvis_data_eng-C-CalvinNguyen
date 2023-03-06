package ca.jrvs.apps.twitter.example;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import com.google.gdata.util.common.base.PercentEscaper;
import java.net.URI;
import java.util.Arrays;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterApiTest {

  private static final Logger logger = LoggerFactory.getLogger(TwitterApiTest.class);
  private static String CONSUMER_KEY = System.getenv("consumerKey");
  private static String CONSUMER_SECRET = System.getenv("consumerSecret");
  private static String ACCESS_TOKEN = System.getenv("accessToken");
  private static String TOKEN_SECRET = System.getenv("tokenSecret");

  public static void main(String[] args) throws Exception {

    // Setup authentication
    OAuthConsumer oAuthConsumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
    oAuthConsumer.setTokenWithSecret(ACCESS_TOKEN, TOKEN_SECRET);

    // Create/Setup HTTP GET Request
    String userId = "2244994945";
    PercentEscaper percentEscaper = new PercentEscaper("", false);
    HttpGet httpGetRequest = new HttpGet(
        "https://api.twitter.com/2/users/"
            + percentEscaper.escape(userId)
            + "/tweets?tweet.fields=created_at,entities,public_metrics,geo&expansions=geo.place_id"
    );

    // sign/add headers to request
    oAuthConsumer.sign(httpGetRequest);

    System.out.println("Http Request Headers:");
    Arrays.stream(httpGetRequest.getAllHeaders()).forEach(System.out::println);

    // Send http GET request
    HttpClient httpClient = HttpClientBuilder.create().build();
    HttpResponse httpResponse = httpClient.execute(httpGetRequest);
    System.out.println(EntityUtils.toString(httpResponse.getEntity()));

  }

}
