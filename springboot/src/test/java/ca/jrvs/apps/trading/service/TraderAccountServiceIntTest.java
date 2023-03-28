package ca.jrvs.apps.trading.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.domain.TraderAccountView;
import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class TraderAccountServiceIntTest {

  @Autowired
  private TraderAccountService traderAccountService;
  @Autowired
  private TraderDao traderDao;
  @Autowired
  private AccountDao accountDao;
  @Autowired
  private PositionDao positionDao;
  @Autowired
  private SecurityOrderDao securityOrderDao;

  private TraderAccountView savedView;

  @Before
  public void setUp() throws Exception {
    Trader trader = new Trader();
    trader.setFirstName("Calvin");
    trader.setLastName("Nguyen");
    trader.setEmail("test@email.com");
    trader.setCountry("Canada");
    trader.setDob(new Date(1996, 9, 1));

    savedView = traderAccountService.createTraderAndAccount(trader);
  }

  @After
  public void tearDown() throws Exception {
    Account account = savedView.getAccount();
    account.setAmount(0.0);
    accountDao.updateOne(account);

    securityOrderDao.deleteByAccountId(account.getId());
    traderAccountService.deleteTraderById(savedView.getTrader().getId());
  }

  @Test
  public void createDeleteTraderAndAccount() {
    Trader trader = new Trader();
    trader.setFirstName("Calvin2");
    trader.setLastName("Nguyen2");
    trader.setEmail("test2@email.com");
    trader.setCountry("Canada");
    trader.setDob(new java.util.Date(1996, 9, 1));

    TraderAccountView newView = traderAccountService.createTraderAndAccount(trader);
    assertEquals(trader.getFirstName(), newView.getTrader().getFirstName());
    assertEquals(
        Double.valueOf(0.0),
        Double.valueOf(newView.getAccount().getAmount().doubleValue()));

    traderAccountService.deleteTraderById(newView.getTrader().getId());
  }

  @Test
  public void depositAndWithdraw() {
    Account account = traderAccountService.deposit(savedView.getTrader().getId(), 1000d);
    savedView.setAccount(account);

    assertEquals(
        Double.valueOf(1000d),
        Double.valueOf(account.getAmount().doubleValue()));

    Account account1 = traderAccountService.withdraw(savedView.getTrader().getId(), 1000d);
    savedView.setAccount(account1);

    assertEquals(
        Double.valueOf(0d),
        Double.valueOf(account1.getAmount().doubleValue())
    );
  }
}