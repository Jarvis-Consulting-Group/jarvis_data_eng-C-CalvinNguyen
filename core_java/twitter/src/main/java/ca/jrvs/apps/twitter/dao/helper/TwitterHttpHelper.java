package ca.jrvs.apps.twitter.dao.helper;

import ca.jrvs.apps.twitter.example.TwitterApiTest;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class TwitterHttpHelper implements HttpHelper {

  private static final Logger logger = LoggerFactory.getLogger(TwitterApiTest.class);
  private OAuthConsumer oAuthConsumer;
  private HttpClient httpClient;

  public TwitterHttpHelper(String consumerKey, String consumerSecret,
      String accessToken, String tokenSecret) {
    oAuthConsumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
    oAuthConsumer.setTokenWithSecret(accessToken, tokenSecret);

    httpClient = new DefaultHttpClient();
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

    oAuthConsumer.sign(httpUriRequest);
    return httpClient.execute(httpUriRequest);
  }
}
