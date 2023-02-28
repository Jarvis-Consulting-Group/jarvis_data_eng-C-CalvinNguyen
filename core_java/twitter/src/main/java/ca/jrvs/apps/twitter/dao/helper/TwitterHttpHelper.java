package ca.jrvs.apps.twitter.dao.helper;

import ca.jrvs.apps.twitter.example.TwitterApiTest;
import java.io.IOException;
import java.net.URI;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TwitterHttpHelper is an implementation of HttpHelper that is used to execute the http methods
 * given a URI. For example the httpGet makes a HttpUriRequest with the URI endpoint and uses the
 * executeRequest method to execute the HttpUriRequest and then return the HttpResponse.
 */
public class TwitterHttpHelper implements HttpHelper {

  private static final Logger logger = LoggerFactory.getLogger(TwitterApiTest.class);
  private final OAuthConsumer oauthConsumer;
  private final HttpClient httpClient;

  /**
   * Constructor for setiing up the consumer key, consumer secret, access token and token secret for
   * the OAuthConsumer object.
   * @param consumerKey consumer key. API consumer key
   * @param consumerSecret consumer secret. API consumer secret
   * @param accessToken access token. API access token
   * @param tokenSecret token secret. API token secret
   */
  public TwitterHttpHelper(String consumerKey, String consumerSecret,
      String accessToken, String tokenSecret) {
    oauthConsumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
    oauthConsumer.setTokenWithSecret(accessToken, tokenSecret);

    httpClient = HttpClients.createDefault();
  }

  @Override
  public HttpResponse httpPost(URI uri) {
    try {
      HttpPost httpPost = new HttpPost(uri);
      httpPost.setEntity(null);

      return httpRequestExecutor(httpPost);
    } catch (OAuthException | IOException e) {
      throw new RuntimeException("Exception", e);
    }
  }

  @Override
  public HttpResponse httpPostV2(URI uri, String s) {
    try {
      HttpPost httpPost = new HttpPost(uri);

      // Setup StringEntity and Headers
      StringEntity stringEntity = new StringEntity(s);
      httpPost.setHeader("content-type", "application/json");
      httpPost.setEntity(stringEntity);

      return httpRequestExecutor(httpPost);
    } catch (OAuthException | IOException e) {
      throw new RuntimeException("Exception", e);
    }
  }

  @Override
  public HttpResponse httpGet(URI uri) {
    try {
      HttpGet httpGet = new HttpGet(uri);

      return httpRequestExecutor(httpGet);
    } catch (OAuthException | IOException e) {
      throw new RuntimeException("Exception", e);
    }
  }

  @Override
  public HttpResponse httpDelete(URI uri) {
    try {
      HttpPost httpPost = new HttpPost(uri);

      return httpRequestExecutor(httpPost);
    } catch (OAuthException | IOException e) {
      throw new RuntimeException("Exception", e);
    }
  }

  @Override
  public HttpResponse httpDeleteV2(URI uri) {
    try {
      HttpDelete httpDelete = new HttpDelete(uri);

      return httpRequestExecutor(httpDelete);
    } catch (OAuthException | IOException e) {
      throw new RuntimeException("Exception", e);
    }
  }

  private HttpResponse httpRequestExecutor(HttpUriRequest httpUriRequest)
      throws IOException, OAuthException {

    oauthConsumer.sign(httpUriRequest);
    return httpClient.execute(httpUriRequest);
  }
}
