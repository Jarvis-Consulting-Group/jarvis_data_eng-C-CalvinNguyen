package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
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
public class TraderDaoIntTest {

  @Autowired
  private TraderDao traderDao;

  private Trader savedTrader;

  @Before
  public void setUp() throws Exception {
    savedTrader = new Trader();
    savedTrader.setFirstName("Calvin");
    savedTrader.setLastName("Nguyen");
    savedTrader.setEmail("test@email.com");
    savedTrader.setCountry("Canada");
    savedTrader.setDob(new Date(1996, 9, 1));
    traderDao.save(savedTrader);
  }

  @After
  public void tearDown() throws Exception {
    traderDao.deleteById(savedTrader.getId());
  }

  @Test
  public void findAllById() {
    List<Trader> traderList = Lists
        .newArrayList(traderDao.findAllById(Collections.singletonList(savedTrader.getId())));
    assertEquals(1, traderList.size());
    assertEquals(savedTrader.getCountry(), traderList.get(0).getCountry());
  }
}