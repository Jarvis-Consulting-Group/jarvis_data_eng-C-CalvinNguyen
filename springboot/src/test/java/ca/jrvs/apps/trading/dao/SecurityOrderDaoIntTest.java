package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.assertj.core.util.Lists;
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
public class SecurityOrderDaoIntTest {

  @Autowired
  private QuoteDao quoteDao;

  @Autowired
  private AccountDao accountDao;

  @Autowired
  private TraderDao traderDao;

  @Autowired
  private SecurityOrderDao securityOrderDao;

  private Quote savedQuote;
  private Account savedAccount;
  private Trader savedTrader;
  private SecurityOrder savedSecurityOrder;

  @Before
  public void setUp() throws Exception {
    quoteDao.deleteAll();
    traderDao.deleteAll();
    accountDao.deleteAll();
    securityOrderDao.deleteAll();

    savedQuote = new Quote();
    savedQuote.setAskPrice(10d);
    savedQuote.setAskSize(10);
    savedQuote.setBidPrice(10.2d);
    savedQuote.setBidSize(10);
    savedQuote.setId("AAPL");
    savedQuote.setLastPrice(10.1d);
    quoteDao.save(savedQuote);

    savedTrader = new Trader();
    savedTrader.setFirstName("Calvin");
    savedTrader.setLastName("Nguyen");
    savedTrader.setEmail("test@email.com");
    savedTrader.setCountry("Canada");
    savedTrader.setDob(new Date(1996, 9, 1));
    traderDao.save(savedTrader);

    savedAccount = new Account();
    savedAccount.setTraderId(1);
    savedAccount.setAmount(0d);
    accountDao.save(savedAccount);

    savedSecurityOrder = new SecurityOrder();
    savedSecurityOrder.setAccountId(1);
    savedSecurityOrder.setNotes("Temporary Notes");
    savedSecurityOrder.setPrice(10);
    savedSecurityOrder.setSize(10);
    savedSecurityOrder.setTicker("AAPL");
    savedSecurityOrder.setStatus("PENDING");
    securityOrderDao.save(savedSecurityOrder);
  }

  @After
  public void tearDown() throws Exception {
    securityOrderDao.deleteById(savedSecurityOrder.getId());
    accountDao.deleteById(savedAccount.getId());
    traderDao.deleteById(savedTrader.getId());
    quoteDao.deleteById(savedQuote.getId());
  }

  @Test
  public void findAllById() {
    List<SecurityOrder> securityOrderList = Lists
        .newArrayList(securityOrderDao.findAllById(
            Collections.singletonList(savedSecurityOrder.getId())));

    assertEquals(1, securityOrderList.size());
    assertEquals(savedSecurityOrder.getPrice().doubleValue(), securityOrderList.get(0).getPrice());
    assertEquals(savedSecurityOrder.getId(), securityOrderList.get(0).getId());
  }
}