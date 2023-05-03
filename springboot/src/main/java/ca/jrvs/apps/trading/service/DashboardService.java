package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.PortfolioView;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityRow;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.domain.TraderAccountView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Service
@Transactional
public class DashboardService {

  private final TraderDao traderDao;
  private final AccountDao accountDao;
  private final PositionDao positionDao;
  private final QuoteDao quoteDao;

  /**
   * Constructor that takes a TraderDao, AccountDao, PositionDao, and QuoteDao. Spring IoC will
   * manage the lifecycles of this component and the dependencies.
   *
   * @param traderDao   TraderDao for working with the Trader table.
   * @param accountDao  AccountDao for working with the Account table.
   * @param positionDao PositionDao for working with the Position view.
   * @param quoteDao    QuoteDao for working with the Quote table.
   */
  @Autowired
  public DashboardService(TraderDao traderDao, AccountDao accountDao, PositionDao positionDao,
      QuoteDao quoteDao) {
    this.traderDao = traderDao;
    this.accountDao = accountDao;
    this.positionDao = positionDao;
    this.quoteDao = quoteDao;
  }

  /**
   * Gets the TraderAccountView (contains both Trader and associated Account) given the traderId.
   *
   * @param traderId Integer traderId for getting the Trader and Account.
   * @return returns the TraderAccountView which contains both the Trader and Account.
   */
  public TraderAccountView getTraderAccount(Integer traderId) {
    Trader trader = traderDao.findById(traderId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid traderId"));

    Account account = accountDao.findByTraderId(traderId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid traderId"));

    TraderAccountView traderAccountView = new TraderAccountView();
    traderAccountView.setTrader(trader);
    traderAccountView.setAccount(account);

    return traderAccountView;
  }

  /**
   * Gets the PortfolioView by the traderId, the PortfolioView contains all positions/security rows
   * for the trader.
   *
   * @param traderId traderId used to get data for the portfolio view.
   * @return PortfolioView returned containing security rows of positions, quote ticker etc.
   */
  public PortfolioView getProfileViewByTraderId(Integer traderId) {

    List<Position> positionList = positionDao.findAllById(Collections.singletonList(traderId));
    List<SecurityRow> securityRowList = new ArrayList<>();

    positionList.forEach((position -> {
      SecurityRow securityRow = new SecurityRow();
      securityRow.setPosition(position);
      securityRow.setTicker(position.getTicker());

      Quote quote = quoteDao.findById(position.getTicker()).orElseThrow(() ->
          new DataRetrievalFailureException("Error finding Quote with symbol/ticker: "
              + position.getTicker()));
      securityRow.setQuote(quote);

      securityRowList.add(securityRow);
    }));

    PortfolioView portfolioView = new PortfolioView();
    portfolioView.setSecurityRows(securityRowList);

    return portfolioView;
  }

  /**
   * Returns all the traders within the database.
   *
   * @return returns a list of the traders in the database.
   */
  public List<Trader> getTraders() {
    return traderDao.findAll();
  }
}
