package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.PortfolioView;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.domain.TraderAccountView;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DashboardService {

  private TraderDao traderDao;
  private AccountDao accountDao;
  private PositionDao positionDao;
  private QuoteDao quoteDao;

  @Autowired
  public DashboardService(TraderDao traderDao, AccountDao accountDao, PositionDao positionDao,
      QuoteDao quoteDao) {
    this.traderDao = traderDao;
    this.accountDao = accountDao;
    this.positionDao = positionDao;
    this.quoteDao = quoteDao;
  }

  public TraderAccountView getTraderAccount(Integer traderId) {
    Trader trader = traderDao.findById(traderId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid traderId"));

    Account account = findAccountByTraderId(traderId);

    TraderAccountView traderAccountView = new TraderAccountView();
    traderAccountView.setTrader(trader);
    traderAccountView.setAccount(account);

    return traderAccountView;
  }

  public PortfolioView getProfileViewByTraderId(Integer traderId) {
    return null;
  }

  private Account findAccountByTraderId(Integer traderId) {
    return accountDao.findByTraderId(traderId)
        .orElseThrow(() ->  new IllegalArgumentException("Invalid traderId"));
  }
}
