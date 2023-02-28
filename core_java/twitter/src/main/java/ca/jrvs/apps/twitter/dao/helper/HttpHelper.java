package ca.jrvs.apps.twitter.dao.helper;

import java.net.URI;
import org.apache.http.HttpResponse;

public interface HttpHelper {

  HttpResponse httpPost(URI uri);

  HttpResponse httpPostV2(URI uri, String s);

  HttpResponse httpGet(URI uri);

  HttpResponse httpDelete(URI uri);

  HttpResponse httpDeleteV2(URI uri);
}
