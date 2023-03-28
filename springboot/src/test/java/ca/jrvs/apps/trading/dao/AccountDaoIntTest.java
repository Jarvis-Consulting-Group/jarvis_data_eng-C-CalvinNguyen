package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.swing.Spring;
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
public class AccountDaoIntTest {

  @Autowired
  private AccountDao accountDao;

  @Autowired
  private TraderDao traderDao;

  private Trader savedTrader;
  private Account savedAccount;

  @Before
  public void setUp() throws Exception {
    savedTrader = new Trader();
    savedTrader.setFirstName("Calvin");
    savedTrader.setLastName("Nguyen");
    savedTrader.setEmail("test@email.com");
    savedTrader.setCountry("Canada");
    savedTrader.setDob(new Date(1996, 9, 1));
    traderDao.save(savedTrader);
    savedAccount = new Account();
    savedAccount.setTraderId(1);
    savedAccount.setAmount(0);
    accountDao.save(savedAccount);
  }

  @After
  public void tearDown() throws Exception {
    accountDao.deleteById(savedAccount.getId());
    traderDao.deleteById(savedTrader.getId());
  }

  @Test
  public void findAllById() {
    List<Account> accountList = Lists
        .newArrayList(accountDao.findAllById(Arrays.asList(savedAccount.getId(), -1)));

    assertEquals(1, accountList.size());
    assertEquals(savedAccount.getAmount().doubleValue(), accountList.get(0).getAmount());
    assertEquals(savedAccount.getTraderId(), accountList.get(0).getTraderId());
  }
}