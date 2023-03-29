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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
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

  private Account findAccountByTraderId(Integer traderId) {
    return accountDao.findByTraderId(traderId)
        .orElseThrow(() ->  new IllegalArgumentException("Invalid traderId"));
  }
}
