package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.JsonUtil;
import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Retrieves Quote objects (in JSON format) from the IEX REST API.
 */
@Repository
public class MarketDataDao implements CrudRepository<IexQuote, String> {

  private static final String IEX_BATCH_PATH = ""
      + "/stock/market/batch?types=quote&token=";
  private static final String IEX_SYMBOLS = "&symbols=";
  private final String iexBatchUrl;
  private Logger logger = LoggerFactory.getLogger(MarketDataDao.class);
  private HttpClientConnectionManager httpClientConnectionManager;

  /**
   * Constructor for the MarketDataDao, it takes a HttpClientConnection Manager and
   * the MarketDataConfig.
   * @param httpClientConnectionManager gets httpclient connections from the manager.
   * @param marketDataConfig config to get host URL and the API token.
   */
  @Autowired
  public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager,
      MarketDataConfig marketDataConfig) {
    this.httpClientConnectionManager = httpClientConnectionManager;

    iexBatchUrl = marketDataConfig.getHost() + IEX_BATCH_PATH + marketDataConfig.getToken();
  }

  @Override
  public <S extends IexQuote> S save(S s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public <S extends IexQuote> Iterable<S> saveAll(Iterable<S> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Finds a single IexQuote given the unique ID/symbol/ticker in String format. Returns
   * the optional object which could either be empty or contain the IexQuote if found.
   * @param symbol String that contains the unique ID/symbol/ticker for the quote.
   * @return returns an optional object which could be empty or have IexQuote object.
   * @throws IllegalArgumentException Throws if ID/symbol/ticker is invalid
   * @throws DataRetrievalFailureException Throws if the call to findAllById retrieves more than
   * one quote meaning there is some unexpected logical error.
   */
  @Override
  public Optional<IexQuote> findById(String symbol) {
    Optional<IexQuote> iexQuoteOptional;
    List<IexQuote> iexQuoteList = findAllById(Collections.singletonList(symbol));

    if (iexQuoteList.size() == 0) {
      return Optional.empty();
    } else if (iexQuoteList.size() == 1) {
      iexQuoteOptional = Optional.of(iexQuoteList.get(0));
    } else {
      throw new DataRetrievalFailureException(""
          + "Unexpected MarketDataDao Error: number of quotes (2 or more).");
    }

    return iexQuoteOptional;
  }

  @Override
  public boolean existsById(String s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Iterable<IexQuote> findAll() {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Creates a URL containing the token and multiple IDs/symbols/tickers and gets a String http
   * response body that is then converted into a JSONObject and returns a List containing IexQuote
   * objects populated with data from each object in the JSONObject.
   *  calls executeHttpGet to get the http response body
   *  calls mapJsonToIexQuote to get a List of IexQuotes and returns it.
   *
   * @param symbols Iterable String object containing unique IDs/symbols/tickers.
   * @return returns a List of IexQuote objects.
   * @throws IllegalArgumentException throws if ID/symbol/ticker is invalid.
   * @throws DataRetrievalFailureException throws if unexpected error when retrieving data from API.
   */
  @Override
  public List<IexQuote> findAllById(Iterable<String> symbols) {
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<String> stringIterator = symbols.iterator();

    stringBuilder.append(stringIterator.next());
    while (stringIterator.hasNext()) {
      stringBuilder.append(",");
      stringBuilder.append(stringIterator.next());
    }

    String responseBody = executeHttpGet(iexBatchUrl + IEX_SYMBOLS + stringBuilder.toString())
        .orElseThrow(() -> new IllegalArgumentException(""
            + "MarketDataDao Error: Invalid Symbol/Id/Ticker"));

    JSONObject iexQuotesJson = new JSONObject(responseBody);

    if (iexQuotesJson.length() == 0) {
      throw new IllegalArgumentException(""
          + "MarketDataDao Error: Invalid Symbol/Id/Ticker");
    }

    return mapJsonToIexQuote(iexQuotesJson, symbols.iterator());
  }

  @Override
  public long count() {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteById(String s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void delete(IexQuote iexQuote) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends IexQuote> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll() {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * executes an HTTP GET method given the url and gets a HttpClient from the getHttpClient method,
   * gets a Http Response Body, converts the body into a string and returns that string.
   * @param url url containing the token and symbol query values.
   * @return returns an optional object that could either contain a String or be empty.
   */
  private Optional<String> executeHttpGet(String url) {
    Optional<String> stringOptional;
    HttpGet httpGet = new HttpGet(url);

    try (CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
      int responseCode = httpResponse.getStatusLine().getStatusCode();
      String responsePhrase = httpResponse.getStatusLine().getReasonPhrase();

      if (responseCode == HttpStatus.SC_NOT_FOUND) {
        stringOptional = Optional.empty();

      } else if (responseCode == HttpStatus.SC_OK) {
        stringOptional = Optional.of(EntityUtils.toString(httpResponse.getEntity()));

      } else {
        throw new DataRetrievalFailureException("Unexpected MarketDataDao Error: "
            + responseCode + " - " + responsePhrase);

      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return stringOptional;
  }

  /**
   * Gets a CloseableHttpClient from the HttpClientConnectionManager and returns that HttpClient.
   * @return Returns a CLoseableHttpClient.
   */
  private CloseableHttpClient getHttpClient() {
    return HttpClients.custom()
        .setConnectionManager(httpClientConnectionManager)
        .setConnectionManagerShared(true)
        .build();
  }

  /**
   * Given the JsonObject containing one or more quote objects in Json String format, and the
   * String iterator, for each symbol in the String iterator convert the appropriate JsonObject
   * from the key into a String, and use this String to populate the IexQuote.
   * @param jsonObject JSONObject that could contain one or more quotes
   * @param iterator iterator containing all the symbols, used to map each IexQuote.
   * @return returns a List of IexQuotes populated with data from the JsonObject.
   */
  private List<IexQuote> mapJsonToIexQuote(JSONObject jsonObject, Iterator<String> iterator) {
    List<IexQuote> iexQuoteList = new ArrayList<>();

    while (iterator.hasNext()) {
      try {
        String iexQuoteJson = jsonObject.getJSONObject(iterator.next())
            .getJSONObject("quote").toString();
        IexQuote iexQuote = JsonUtil.toObjectFromJson(iexQuoteJson, IexQuote.class);
        iexQuoteList.add(iexQuote);

      } catch (IOException e) {
        throw new RuntimeException(e);
      } catch (JSONException e) {
        throw new IllegalArgumentException("Unexpected MarketDataDao Error: "
            + "Invalid Symbol/ID/Ticker: " + e.getMessage(), e);
      }
    }

    return iexQuoteList;
  }
}
