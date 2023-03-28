package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.MarketOrder;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

  private AccountDao accountDao;
  private SecurityOrderDao securityOrderDao;
  private PositionDao positionDao;
  private QuoteDao quoteDao;

  @Autowired
  public OrderService(AccountDao accountDao, SecurityOrderDao securityOrderDao,
      PositionDao positionDao, QuoteDao quoteDao) {
    this.accountDao = accountDao;
    this.securityOrderDao = securityOrderDao;
    this.positionDao = positionDao;
    this.quoteDao = quoteDao;
  }

  public SecurityOrder executeMarketOrder(MarketOrder marketOrder) throws IllegalArgumentException {
    checkInputs(marketOrder);

    Optional<Quote> optionalQuote = quoteDao.findById(marketOrder.getTicker());
    if (!optionalQuote.isPresent()) {
      throw new DataRetrievalFailureException("Unable to find Quote with ticker: "
          + marketOrder.getTicker());
    }
    Optional<Account> optionalAccount = accountDao.findById(marketOrder.getAccountId());
    if (!optionalAccount.isPresent()) {
      throw new DataRetrievalFailureException("Unable to find Account with ID: "
          + marketOrder.getAccountId());
    }

    Quote quote = optionalQuote.get();;
    Account account = optionalAccount.get();
    SecurityOrder securityOrder = new SecurityOrder();

    if (marketOrder.getSize() > 0) {
      securityOrder.setPrice(quote.getAskPrice());

      if ((marketOrder.getSize() * quote.getAskPrice()) > account.getAmount().doubleValue()) {
        securityOrder.setNotes("Insufficient funds for order");
        securityOrder.setStatus("CANCELLED");
      } else {
        securityOrder.setNotes("fulfilled order.");
        securityOrder.setStatus("FULFILLED");
      }
      handleBuyMarketOrder(marketOrder, securityOrder);
    }

    if (marketOrder.getSize() < 0) {
      securityOrder.setPrice(quote.getBidPrice());
      handleSellMarketOrder(marketOrder, securityOrder);
    }

    return securityOrder;
  }

  protected void handleBuyMarketOrder(MarketOrder marketOrder, SecurityOrder securityOrder) {
    securityOrder.setAccountId(marketOrder.getAccountId());
    securityOrder.setTicker(marketOrder.getTicker());
    securityOrder.setSize(marketOrder.getSize());

    securityOrder = securityOrderDao.save(securityOrder);
  }

  protected void handleSellMarketOrder(MarketOrder marketOrder, SecurityOrder securityOrder) {
    Optional<Position> optionalPosition = positionDao.findByAccountIdAndTicker(
        marketOrder.getAccountId(), marketOrder.getTicker());
    if (!optionalPosition.isPresent()) {
      throw new DataRetrievalFailureException("Unable to find positions "
          + "to sell with ticker symbol: " + marketOrder.getTicker());
    }

    Position position = optionalPosition.get();
    securityOrder.setAccountId(marketOrder.getAccountId());
    securityOrder.setSize(marketOrder.getSize());
    securityOrder.setTicker(marketOrder.getTicker());
    int total = position.getPosition() - marketOrder.getSize();

    if (total < 0) {
      securityOrder.setNotes("Insufficient positions with ticker symbol: "
          + marketOrder.getTicker()
          + ", currently only possess: " + position.getPosition());
      securityOrder.setStatus("CANCELLED");
    } else {
      securityOrder.setNotes("Fulfilled: remaining positions with ticker symbol "
          + marketOrder.getTicker() + " is: " + total);
      securityOrder.setStatus("FULFILLED");
    }

    securityOrder = securityOrderDao.save(securityOrder);
  }

  private void checkInputs(MarketOrder marketOrder) throws IllegalArgumentException {
    if (marketOrder.getAccountId() == null) {
      throw new IllegalArgumentException("Invalid: Account ID is null.");
    }
    if (marketOrder.getTicker() == null) {
      throw new IllegalArgumentException("Invalid: Ticker symbol is null.");
    }
    if (marketOrder.getSize() == null) {
      throw new IllegalArgumentException("Invalid: Size is null.");
    }
    if (marketOrder.getSize() == 0) {
      throw new IllegalArgumentException("Invalid: Size is 0.");
    }
  }
}
