package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * QuoteService contains a QuoteDao and MarketDataDao, and uses them to perform operations on the
 * IEX REST API or PostgresSQL database.
 */
@Transactional
@Service
public class QuoteService {

  private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);
  private final QuoteDao quoteDao;
  private final MarketDataDao marketDataDao;

  /**
   * Constructor for the QuoteService Bean, the dependencies lifecycles will be managed by Spring
   * IoC.
   *
   * @param quoteDao      QuoteDao dependency.
   * @param marketDataDao MarketDataDao dependency.
   */
  @Autowired
  public QuoteService(QuoteDao quoteDao, MarketDataDao marketDataDao) {
    this.quoteDao = quoteDao;
    this.marketDataDao = marketDataDao;
  }

  /**
   * Calls the marketDataDao and tells it to get an IexQuote with the unique ID/symbol/ticker.
   *
   * @param symbol String representing the unique ID/symbol/ticker.
   * @return returns an IexQuote Object.
   * @throws IllegalArgumentException throws an error if the Optional IexQuote object returned is
   *                                  empty meaning the ID/symbol/ticker is invalid.
   */
  public IexQuote findIexQuoteBySymbol(String symbol) {
    return marketDataDao.findById(symbol)
        .orElseThrow(() -> new IllegalArgumentException("QuoteService Error: "
            + "invalid symbol/ID/ticker - " + symbol));
  }

  /**
   * findAllQuotes method returns a list of all the quotes within the database.
   *
   * @return returns a LIst of all quotes within the database.
   */
  public List<Quote> findAllQuotes() {
    return quoteDao.findAll();
  }

  /**
   * Helper method that saves a quote object to the database using the QuoteDao.
   *
   * @param quote takes a quote object that will be saved into the database.
   * @return returns the saved Quote object.
   */
  public Quote saveQuote(Quote quote) {
    return quoteDao.save(quote);
  }

  /**
   * Method that takes a symbol/ticker, finds the IEXQuote using the MarketDataDao and adds that
   * Quote to the database using the QuoteDao. - Finds IEXQuote using the symbol/ticker. - Maps the
   * IEXQuote to a new Quote object using the buildQuoteFromIexQuote method. - Saves the new Quote
   * object using the QuoteDao.
   *
   * @param ticker String representing the Quote symbol/ticker.
   * @return returns the saved Quote object.
   */
  public Quote saveQuote(String ticker) {
    IexQuote iexQuote = findIexQuoteBySymbol(ticker);
    Quote quote = buildQuoteFromIexQuote(iexQuote);
    return quoteDao.save(quote);
  }

  /**
   * Updates the all the quotes within the database with the information from the IEX REST API. -
   * Calls the findAllQuotes method to get a list of all the quotes. - For each quote within the
   * database, retrieve the appropriate IEXQuote from the IEX REST API. - Calls the
   * buildQuoteFromIexQuote to get the updated Quote object and insert it into a list. - Calls the
   * QuoteDao saveAll method wit hthe new updated Quote objects list.
   *
   * @return returns a List of the updated Quote objects.
   */
  public List<Quote> updateMarketData() {
    List<Quote> quoteList = findAllQuotes();
    List<Quote> updatedList = new ArrayList<>();

    quoteList.forEach(quote -> {
      IexQuote iexQuote = findIexQuoteBySymbol(quote.getId());
      updatedList.add(buildQuoteFromIexQuote(iexQuote));
    });

    return quoteDao.saveAll(updatedList);
  }

  /**
   * Creates a Quote object and populates the states/properties with information from the IexQuote.
   *
   * @param iexQuote IexQuote used to retrieve data and insert into the Quote object.
   * @return returns the Quote object with the populated data.
   */
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

  /**
   * Given a list of string representing quote symbol/tickers, find all IexQuotes, build a Quote
   * from each IexQuote, and add each Quote into the database.
   *
   * @param tickers List of Strings representing IexQuote symbols/tickers.
   * @return returns List of Quotes saved to the database.
   */
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
