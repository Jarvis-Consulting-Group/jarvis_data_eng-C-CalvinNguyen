package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.AccountJpaDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.domain.TraderAccountView;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TraderAccountService contains a TraderDao, AccountDao, PositionDao, and SecurityOrderDao. the
 * main purposes for this service is to create/delete a Trader/Account and deposit/withdraw funds
 * from the account.
 */
@Service
public class TraderAccountJpaService {

  private final TraderDao traderDao;
  private final AccountJpaDao accountDao;
  private final PositionDao positionDao;
  private final SecurityOrderDao securityOrderDao;

  /**
   * Constructor that takes a TraderDao, AccountDao, PositionDao and SecurityOrderDao. Spring IoC
   * will manage the lifecycles of this component and the dependencies.
   *
   * @param traderDao        TraderDao for working with the Trader table.
   * @param accountDao       AccountDao for working with the Account Table.
   * @param positionDao      PositionDao for working with the Position View.
   * @param securityOrderDao SecurityOrderDao for working with the Security_Order table.
   */
  @Autowired
  public TraderAccountJpaService(TraderDao traderDao, AccountJpaDao accountDao,
      PositionDao positionDao, SecurityOrderDao securityOrderDao) {
    this.traderDao = traderDao;
    this.accountDao = accountDao;
    this.positionDao = positionDao;
    this.securityOrderDao = securityOrderDao;
  }

  /**
   * Creates a Trader and Account within the database given the Trader DTO.
   *
   * @param trader Trader DTO that will be saved into the database.
   * @return returns a TraderAccountView object that contains both the created Trader and Account.
   * @throws IllegalArgumentException throws an IllegalArgumentException if the given trader or any
   *                                  of its properties are null
   */
  @Transactional
  public TraderAccountView createTraderAndAccount(Trader trader) throws IllegalArgumentException {
    checkTrader(trader);
    Trader tempTrader = traderDao.save(trader);

    Account account = new Account();
    account.setTraderId(tempTrader.getId());
    account.setAmount(0d);
    Account tempAccount = accountDao.save(account);

    TraderAccountView traderAccountView = new TraderAccountView();
    traderAccountView.setTrader(tempTrader);
    traderAccountView.setAccount(tempAccount);

    return traderAccountView;
  }

  /**
   * Deletes the Trader given the traderId as well as any Security Orders and the Account related to
   * the Trader.
   *
   * @param traderId Integer traderId used to delete the Trader and related Security Orders and
   *                 Account.
   */
  @Transactional
  public void deleteTraderById(Integer traderId) {
    if (traderId == null) {
      throw new IllegalArgumentException("traderId is null");
    }

    Account account = accountDao.getAccountByTraderId(traderId);

    if (account.getAmount().doubleValue() != 0.0) {
      throw new IllegalArgumentException("Account balance is not empty");
    }

    List<Position> positionList = positionDao.findAllById(
        Collections.singletonList(account.getId()));

    for (Position position : positionList) {
      if (position.getPosition() != 0) {
        throw new IllegalArgumentException("Positions are not closed");
      }
    }

    securityOrderDao.deleteByAccountId(account.getId());
    accountDao.findById(account.getId()).ifPresent(d -> accountDao.deleteById(d.getId()));
    /* DataIntegrityException is thrown because the JDBCTemplate transaction occurs at the
    same time or before the accountJpaDao is able to delete the account?
     */
    traderDao.findById(traderId).ifPresent(d -> traderDao.deleteById(d.getId()));
  }

  /**
   * Deposits an amount into the Account related to the trader given the traderId (accountId and
   * traderId should be the same).
   *
   * @param traderId traderId to identify the account the amount should be deposited into.
   * @param fund     The amount that is deposited into the account.
   * @return returns the Account with the deposited amount.
   * @throws IllegalArgumentException throws this if we're unable to find the Trader/Account with
   *                                  the given ID.
   */
  @Transactional
  public Account deposit(Integer traderId, Double fund) throws IllegalArgumentException {
    checkAccountBalance(traderId, fund);

    Account account = accountDao.getAccountByTraderId(traderId);

    Double balance = account.getAmount().doubleValue() + fund;
    account.setAmount(balance);

    account = accountDao.save(account);

    return account;
  }

  /**
   * Withdraws an amount from the Account related to the trader given the traderId (accountId and
   * the traderId should be the same).
   *
   * @param traderId traderId to identify the account the amount should be withdrawn from.
   * @param fund     the amount that is withdrawn from the account.
   * @return returns the account with the funds withdrawn.
   * @throws IllegalArgumentException thrown when there is an error retrieving the account
   *                                  (trader/account doesn't exist), or when there is insufficient
   *                                  funds within the account.
   */
  @Transactional
  public Account withdraw(Integer traderId, Double fund) throws IllegalArgumentException {
    checkAccountBalance(traderId, fund);

    Account account = accountDao.getAccountByTraderId(traderId);

    if (account.getAmount().doubleValue() < fund) {
      throw new IllegalArgumentException("Insufficient funds");
    }

    Double balance = account.getAmount().doubleValue() - fund;
    account.setAmount(balance);

    account = accountDao.save(account);

    return account;
  }

  /**
   * Checks if any of the arguments are null or the fund amount is not above 0.
   *
   * @param traderId traderId for the trader/account.
   * @param fund     amount to be deposited/withdrawn.
   * @throws IllegalArgumentException thrown if null or fund is 0 or below.
   */
  private void checkAccountBalance(Integer traderId, Double fund) throws IllegalArgumentException {
    if (traderId == null) {
      throw new IllegalArgumentException("Trader ID is null");
    }
    if (fund == null) {
      throw new IllegalArgumentException("Deposit fund is null");
    }
    if (fund <= 0) {
      throw new IllegalArgumentException("fund is not positive");
    }
  }

  /**
   * Checks if the arguments are null or the ID isn't null (ID is auto-generated, meaning it should
   * not be filled).
   *
   * @param trader trader object that will be added.
   * @throws IllegalArgumentException thrown if properties are null or if ID is not null.
   */
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
