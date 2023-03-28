package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.domain.TraderAccountView;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import net.bytebuddy.pool.TypePool.Resolution.Illegal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

@Service
public class TraderAccountService {

  private TraderDao traderDao;
  private AccountDao accountDao;
  private PositionDao positionDao;
  private SecurityOrderDao securityOrderDao;

  @Autowired
  public TraderAccountService(TraderDao traderDao, AccountDao accountDao,
      PositionDao positionDao, SecurityOrderDao securityOrderDao) {
    this.traderDao = traderDao;
    this.accountDao = accountDao;
    this.positionDao = positionDao;
    this.securityOrderDao = securityOrderDao;
  }

  public TraderAccountView createTraderAndAccount(Trader trader) throws IllegalArgumentException {
    checkTrader(trader);
    Trader tempTrader = traderDao.save(trader);

    Account account = new Account();
    account.setTraderId(tempTrader.getId());
    account.setAmount(0);
    Account tempAccount = accountDao.save(account);

    TraderAccountView traderAccountView = new TraderAccountView();
    traderAccountView.setTrader(tempTrader);
    traderAccountView.setAccount(tempAccount);

    return traderAccountView;
  }

  public void deleteTraderById(Integer traderId) {
    if (traderId == null) {
      throw new IllegalArgumentException("traderId is null");
    }

    Account account = null;
    Optional<Account> optionalAccount = accountDao.findByTraderId(traderId);
    if (optionalAccount.isPresent()) {
      account = optionalAccount.get();
    }
    if (account == null) {
      throw new IllegalArgumentException("Unable to find Trader by ID: " + traderId);
    }
    if (account.getAmount().doubleValue() != 0.0) {
      throw new IllegalArgumentException("Account balance is not empty");
    }

    List<Position> positionList = positionDao.findAllById(Arrays.asList(account.getId()));
    for (Position position : positionList) {
      if (position.getPosition() != 0) {
        throw new IllegalArgumentException("Positions are not closed");
      }
    }

    securityOrderDao.deleteByAccountId(account.getId());
    accountDao.deleteById(account.getId());
    traderDao.deleteById(traderId);
  }

  public Account deposit(Integer traderId, Double fund) throws IllegalArgumentException {
    checkAccountBalance(traderId, fund);

    Optional<Account> optionalAccount = accountDao.findByTraderId(traderId);

    if (!optionalAccount.isPresent()) {
      throw new IllegalArgumentException("Error retrieving account");
    } else {
      Account account = optionalAccount.get();
      Double balance = account.getAmount().doubleValue() + fund;
      account.setAmount(balance);

      int rowNum = accountDao.updateOne(account);

      if (rowNum == 1) {
        return account;
      } else {
        throw new DataRetrievalFailureException("Error updating account balance");
      }
    }
  }

  public Account withdraw(Integer traderId, Double fund) throws IllegalArgumentException {
    checkAccountBalance(traderId, fund);

    Optional<Account> optionalAccount = accountDao.findByTraderId(traderId);
    if (!optionalAccount.isPresent()) {
      throw new IllegalArgumentException("Error retrieving account");
    } else {
      Account account = optionalAccount.get();

      if (account.getAmount().doubleValue() < fund) {
        throw new IllegalArgumentException("Insufficient funds");
      }

      Double balance = account.getAmount().doubleValue() - fund;
      account.setAmount(balance);
      int rowNum = accountDao.updateOne(account);

      if (rowNum == 1) {
        return account;
      } else {
        throw new DataRetrievalFailureException("Error updating account balance");
      }
    }
  }

  private void checkAccountBalance(Integer traderId, Double fund) throws IllegalArgumentException {
    if (traderId == null) {
      throw new IllegalArgumentException("Trader ID is null");
    }
    if (fund == null) {
      throw new IllegalArgumentException("Deposit fund is null");
    }
    if (fund <= 0) {
      throw new IllegalArgumentException("Deposit fund is not positive");
    }
  }

  private void checkTrader(Trader trader) throws IllegalArgumentException {
    if (trader == null) {
      throw new IllegalArgumentException("Trader is null");
    }
    if (trader.getFirstName() == null) {
      throw new IllegalArgumentException("Null First Name");
    }
    if (trader.getLastName() == null) {
      throw new IllegalArgumentException("Null Last Name");
    }
    if (trader.getEmail() == null) {
      throw new IllegalArgumentException("Null Email");
    }
    if (trader.getCountry() == null) {
      throw new IllegalArgumentException("Null Country");
    }
    if (trader.getDob() == null) {
      throw new IllegalArgumentException("Null Date of Birth");
    }
    if (trader.getId() != null) {
      throw new IllegalArgumentException("ID is auto-generated");
    }
  }
}
