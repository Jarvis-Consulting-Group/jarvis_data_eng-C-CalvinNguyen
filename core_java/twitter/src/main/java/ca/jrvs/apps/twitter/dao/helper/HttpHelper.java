package ca.jrvs.apps.twitter.dao.helper;

import java.net.URI;
import org.apache.http.HttpResponse;

public interface HttpHelper {

  HttpResponse httpPost(URI uri);

  HttpResponse httpGet(URI uri);

}
