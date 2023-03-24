package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import com.sun.org.apache.xpath.internal.operations.Quo;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * QuoteService contains a QuoteDao and MarketDataDao, and uses them to call the
 * IEX REST API or database.
 */
@Transactional
@Service
public class QuoteService {

  private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

  private QuoteDao quoteDao;
  private MarketDataDao marketDataDao;

  @Autowired
  public QuoteService(QuoteDao quoteDao, MarketDataDao marketDataDao) {
    this.quoteDao = quoteDao;
    this.marketDataDao = marketDataDao;
  }

  /**
   * Calls the marketDataDao and tells it to get an IexQuote with the unique ID/symbol/ticker.
   * @param symbol String representing the unique ID/symbol/ticker.
   * @return returns an IexQuote Object.
   * @throws IllegalArgumentException throws an error if the Optional IexQuote object returned
   * is empty meaning the ID/symbol/ticker is invalid.
   */
  public IexQuote findIexQuoteBySymbol(String symbol) {
    return marketDataDao.findById(symbol)
        .orElseThrow(() -> new IllegalArgumentException("QuoteService Error: "
            + "invalid symbol/ID/ticker - " + symbol));
  }

  public List<Quote> findAllQuotes() {
    return quoteDao.findAll();
  }

  public Quote saveQuote(Quote quote) {
    return quoteDao.save(quote);
  }

  public Quote saveQuote(String ticker) {
    IexQuote iexQuote = findIexQuoteBySymbol(ticker);
    Quote quote = buildQuoteFromIexQuote(iexQuote);
    Quote returnQuote = quoteDao.save(quote);
    return returnQuote;
  }

  public void updateMarketData() {
    List<Quote> quoteList = findAllQuotes();
    List<Quote> updatedList = new ArrayList<>();

    quoteList.forEach(quote -> {
      IexQuote iexQuote = findIexQuoteBySymbol(quote.getId());
      updatedList.add(buildQuoteFromIexQuote(iexQuote));
    });

    quoteDao.saveAll(updatedList);
  }

  protected static Quote buildQuoteFromIexQuote(IexQuote iexQuote) {
    Quote quote = new Quote();

    quote.setTicker(iexQuote.getSymbol());

    quote.setLastPrice(
        iexQuote.getLatestPrice() == null && !iexQuote.isUsMarketOpen ? Double.valueOf(0.0)
            : iexQuote.getLatestPrice());

    quote.setBidPrice(iexQuote.getIexBidPrice() == null ? Double.valueOf(0.0)
        : Double.valueOf(iexQuote.getIexBidPrice()));

    quote.setBidSize(
        iexQuote.getIexBidSize() == null ? Integer.valueOf(0) : iexQuote.getIexBidSize());

    quote.setAskPrice(iexQuote.getIexAskPrice() == null ? Double.valueOf(0.0)
        : Double.valueOf(iexQuote.getIexAskPrice()));

    quote.setAskSize(
        iexQuote.getIexAskSize() == null ? Integer.valueOf(0) : iexQuote.getIexAskSize());

    return quote;
  }

  public List<Quote> saveQuotes(List<String> tickers) {
    List<Quote> quoteList = new ArrayList<>();

    tickers.forEach(ticker -> {
      IexQuote iexQuote = findIexQuoteBySymbol(ticker);
      Quote quote = saveQuote(buildQuoteFromIexQuote(iexQuote));
      quoteList.add(quote);
    });

    return quoteList;
  }
}
