package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
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

  //private QuoteDao quoteDao;
  private MarketDataDao marketDataDao;

  /*
  @Autowired
  public QuoteService(QuoteDao quoteDao, MarketDataDao marketDataDao) {
    this.quoteDao = quoteDao;
    this.marketDataDao = marketDataDao;
  }
   */

  @Autowired
  public QuoteService(MarketDataDao marketDataDao) {
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
}
