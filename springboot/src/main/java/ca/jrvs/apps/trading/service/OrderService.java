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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The OrderService manages the transactions of Market Orders and Security Orders using the
 * AccountDao, SecurityOrderDao, PositionDao, and QuoteDao.
 */
@Service
@Transactional
public class OrderService {

  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

  private final AccountDao accountDao;
  private final SecurityOrderDao securityOrderDao;
  private final PositionDao positionDao;
  private final QuoteDao quoteDao;

  /**
   * Constructor that takes an AccountDao, SecurityOrderDao, PositionDao and QuoteDao. Spring IoC
   * will manage the lifecycles of this component and the dependencies.
   *
   * @param accountDao       AccountDao for working with the Account Table.
   * @param securityOrderDao SecurityOrderDao for working with the Security_Order table.
   * @param positionDao      PositionDao for working with the Position View.
   * @param quoteDao         QuoteDao for working with the Quote table.
   */
  @Autowired
  public OrderService(AccountDao accountDao, SecurityOrderDao securityOrderDao,
      PositionDao positionDao, QuoteDao quoteDao) {
    this.accountDao = accountDao;
    this.securityOrderDao = securityOrderDao;
    this.positionDao = positionDao;
    this.quoteDao = quoteDao;
  }

  /**
   * Executes a market order given the market order object, if size is negative it means the user
   * wishes to sell, if it is positive then the user wishes to buy. - checks if the inputs are valid
   * - gets the appropriate quote from the quoteDao - gets the account from the accountDao - create
   * the market order and save it into the database.
   *
   * @param marketOrder MarketOrder that will be saved into the database.
   * @return returns a SecurityOrder containing information in the market order, quote, and account.
   * @throws IllegalArgumentException thrown if the input is invalid.
   */
  public SecurityOrder executeMarketOrder(MarketOrder marketOrder) throws IllegalArgumentException {
    checkInputs(marketOrder);

    Quote quote = quoteDao.findById(marketOrder.getTicker())
        .orElseThrow(() -> new DataRetrievalFailureException("Unable to find Quote with ticker: "
            + marketOrder.getTicker()));
    Account account = accountDao.findById(marketOrder.getAccountId())
        .orElseThrow(() -> new DataRetrievalFailureException("Unable to find Account with ID: "
            + marketOrder.getAccountId()));
    SecurityOrder securityOrder = new SecurityOrder();

    if (marketOrder.getSize() > 0) {
      securityOrder.setPrice(quote.getAskPrice());

      if ((marketOrder.getSize() * quote.getAskPrice()) > account.getAmount().doubleValue()) {
        securityOrder.setNotes("Insufficient funds for order");
        securityOrder.setStatus("CANCELED");
      } else {
        securityOrder.setNotes("fulfilled order.");
        securityOrder.setStatus("FILLED");
      }
      handleBuyMarketOrder(marketOrder, securityOrder);
    }

    if (marketOrder.getSize() < 0) {
      securityOrder.setPrice(quote.getBidPrice());
      handleSellMarketOrder(marketOrder, securityOrder);
    }

    return securityOrder;
  }

  /**
   * if the user is buying, the properties are set and the securityOrderDao is called to save the
   * security order into the database.
   *
   * @param marketOrder   market order with information for the security order.
   * @param securityOrder security order created and saved into the database.
   */
  protected void handleBuyMarketOrder(MarketOrder marketOrder, SecurityOrder securityOrder) {
    securityOrder.setAccountId(marketOrder.getAccountId());
    securityOrder.setTicker(marketOrder.getTicker());
    securityOrder.setSize(marketOrder.getSize());

    securityOrder = securityOrderDao.save(securityOrder);
  }

  /**
   * If the user is selling, first the accounts position must be checked to verify if they possess a
   * share/stock, then the security order is saved into the database regardless if it was successful
   * or not.
   *
   * @param marketOrder   market order with information for the security order.
   * @param securityOrder security order created and saved into the database.
   * @throws IllegalArgumentException Thrown if the position doesn't exist.
   */
  protected void handleSellMarketOrder(MarketOrder marketOrder,
      SecurityOrder securityOrder) throws IllegalArgumentException {

    Position position = positionDao.findByAccountIdAndTicker(marketOrder.getAccountId(),
            marketOrder.getTicker())
        .orElseThrow(() -> new IllegalArgumentException("Unable to find any positions "
            + " with ticker symbol: " + marketOrder.getTicker()));

    securityOrder.setAccountId(marketOrder.getAccountId());
    securityOrder.setSize(marketOrder.getSize());
    securityOrder.setTicker(marketOrder.getTicker());
    int total = position.getPosition() + marketOrder.getSize();

    if (total < 0) {
      securityOrder.setNotes("Insufficient positions with ticker symbol: "
          + marketOrder.getTicker()
          + ", currently only possess: " + position.getPosition());
      securityOrder.setStatus("CANCELED");
    } else {
      securityOrder.setNotes("Fulfilled: remaining positions with ticker symbol "
          + marketOrder.getTicker() + " is: " + total);
      securityOrder.setStatus("FILLED");
    }

    securityOrder = securityOrderDao.save(securityOrder);
  }

  /**
   * If the market order or any of its properties are null an exception is thrown.
   *
   * @param marketOrder market order to be validated before creation.
   * @throws IllegalArgumentException checks if any properties or object itself is null, if it is
   *                                  this exception is thrown.
   */
  private void checkInputs(MarketOrder marketOrder) throws IllegalArgumentException {
    if (marketOrder == null) {
      throw new IllegalArgumentException("Invalid: marketOrder is null");
    }
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
