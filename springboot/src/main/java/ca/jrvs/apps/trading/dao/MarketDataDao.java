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
   * Temporary Message
   * @param httpClientConnectionManager Temp
   * @param marketDataConfig Temp
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
   * Temporary Message
   * @param symbol Temp
   * @return Temp
   * @throws IllegalArgumentException Temp
   * @throws DataRetrievalFailureException Temp
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
          + "Unexpected Error: number of quotes.");
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
   * Temporary Message
   * @param symbols Temp
   * @return Temp
   * @throws IllegalArgumentException Temp
   * @throws DataRetrievalFailureException Temp
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
            + "Error: Invalid Symbol/Id/Ticker"));

    JSONObject iexQuotesJson = new JSONObject(responseBody);

    if (iexQuotesJson.length() == 0) {
      throw new IllegalArgumentException(""
          + "Error: Invalid Symbol/Id/Ticker");
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
   * Temporary Message
   * @param url temp
   * @return temp
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
        throw new DataRetrievalFailureException("Unexpected Error: "
            + responseCode + " - " + responsePhrase);

      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return stringOptional;
  }

  /**
   * Temporary Message
   * @return temp
   */
  private CloseableHttpClient getHttpClient() {
    return HttpClients.custom()
        .setConnectionManager(httpClientConnectionManager)
        .setConnectionManagerShared(true)
        .build();
  }

  /**
   * Temporary Message
   * @param jsonObject temp
   * @param iterator temp
   * @return temp
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
        throw new IllegalArgumentException("Unexpected Error: "
            + "Invalid Symbol/ID/Ticker: " + e.getMessage(), e);
      }
    }

    return iexQuoteList;
  }
}
