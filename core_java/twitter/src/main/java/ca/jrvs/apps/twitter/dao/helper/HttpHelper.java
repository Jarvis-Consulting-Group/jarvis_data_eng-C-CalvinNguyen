package ca.jrvs.apps.twitter.dao.helper;

import java.net.URI;
import org.apache.http.HttpResponse;

/**
 * HttpHelper interface implementations are used for executing http methods given a URI.
 */
public interface HttpHelper {

  /**
   * creates an httpPost method given the Twitter api uri.
   * @param uri the uri contains the status query.
   * @return returns an HttpResponse after executing the request.
   */
  HttpResponse httpPost(URI uri);

  /**
   * creates an httpPost method given the Twitter api v2 uri.
   * @param uri the uri to the Twitter api v2 endpoint.
   * @param s the text json as a string "{text: temp} that is then set as StringEntity"
   * @return returns an HttpResponse after executing the request.
   */
  HttpResponse httpPostV2(URI uri, String s);

  /**
   * creates an httpGet method given the Twitter api uri (v2 or v1.1)
   * @param uri the uri to the get endpoint (v2 or v1.1)
   * @return returns an HttpResponse after executing the request.
   */
  HttpResponse httpGet(URI uri);

  /**
   * creates an httpPost method that deletes given the Twitter v1.1 api
   * @param uri the uri to the post endpoint (v1.1)
   * @return returns an HttpResponse after executing the request.
   */
  HttpResponse httpDelete(URI uri);

  /**
   * creates an httpDelete method that deletes given the Twitter v2 api
   * @param uri the uri to the delete endpoint (v2)
   * @return returns an HttpResponse after executing the request.
   */
  HttpResponse httpDeleteV2(URI uri);

}
