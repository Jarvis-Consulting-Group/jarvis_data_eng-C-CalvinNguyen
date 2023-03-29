package ca.jrvs.apps.trading.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.PortfolioView;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.domain.TraderAccountView;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataRetrievalFailureException;

@RunWith(MockitoJUnitRunner.class)
public class DashboardServiceUnitTest {

  @Captor
  ArgumentCaptor<TraderAccountView> traderAccountViewArgumentCaptor;

  @Mock
  private TraderDao mockTraderDao;
  @Mock
  private AccountDao mockAccountDao;
  @Mock
  private PositionDao mockPositionDao;
  @Mock
  private QuoteDao mockQuoteDao;

  @InjectMocks
  private DashboardService dashboardService;

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void getTraderAccount() {
    Trader mockTrader = new Trader();
    mockTrader.setId(1);
    mockTrader.setFirstName("John");
    mockTrader.setLastName("Doe");
    mockTrader.setEmail("test@email.com");
    mockTrader.setCountry("Canada");
    mockTrader.setDob(new Date(1996, 9, 1));

    Optional<Trader> mockOptionalTrader = Optional.of(mockTrader);
    when(mockTraderDao.findById(any())).thenReturn(mockOptionalTrader);

    Account mockAccount = new Account();
    mockAccount.setId(1);
    mockAccount.setTraderId(1);
    mockAccount.setAmount(100.0);

    Optional<Account> optionalAccount = Optional.of(mockAccount);
    when(mockAccountDao.findByTraderId(any())).thenReturn(optionalAccount);

    TraderAccountView traderAccountView = dashboardService.getTraderAccount(1);
    assertEquals(mockTrader.getFirstName(), traderAccountView.getTrader().getFirstName());
    assertEquals(mockAccount.getAmount(), traderAccountView.getAccount().getAmount());
  }

  @Test
  public void getProfileViewByTraderId() {
    Position mockPosition = new Position();
    mockPosition.setAccountId(1);
    mockPosition.setPosition(1);
    mockPosition.setTicker("AMD");

    Position mockPosition2 = new Position();
    mockPosition.setAccountId(1);
    mockPosition.setPosition(1);
    mockPosition.setTicker("AAPL");

    List<Position> mockPositionList = Arrays.asList(mockPosition, mockPosition2);
    when(mockPositionDao.findAllById(any())).thenReturn(mockPositionList);

    Quote mockQuote = new Quote();
    mockQuote.setTicker("AMD");
    mockQuote.setLastPrice(100d);
    mockQuote.setBidPrice(100d);
    mockQuote.setBidSize(1);
    mockQuote.setAskPrice(110d);
    mockQuote.setAskSize(1);

    Optional<Quote> optionalMockQuote = Optional.of(mockQuote);
    when(mockQuoteDao.findById(any())).thenReturn(optionalMockQuote);

    PortfolioView portfolioView = dashboardService.getProfileViewByTraderId(1);

    assertEquals(mockPosition.getTicker(),
        portfolioView.getSecurityRows().get(0).getPosition().getTicker());

    assertEquals(mockQuote.getTicker(),
        portfolioView.getSecurityRows().get(0).getQuote().getTicker());
  }

  @Test (expected = IllegalArgumentException.class)
  public void traderException() {
    Optional<Trader> mockOptionalTrader = Optional.empty();
    when(mockTraderDao.findById(any())).thenReturn(mockOptionalTrader);

    TraderAccountView traderAccountView = dashboardService.getTraderAccount(1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void accountException() {
    Trader mockTrader = new Trader();
    mockTrader.setId(1);
    mockTrader.setFirstName("John");
    mockTrader.setLastName("Doe");
    mockTrader.setEmail("test@email.com");
    mockTrader.setCountry("Canada");
    mockTrader.setDob(new Date(1996, 9, 1));

    Optional<Trader> mockOptionalTrader = Optional.of(mockTrader);
    when(mockTraderDao.findById(any())).thenReturn(mockOptionalTrader);

    Optional<Account> optionalAccount = Optional.empty();
    when(mockAccountDao.findByTraderId(any())).thenReturn(optionalAccount);

    TraderAccountView traderAccountView = dashboardService.getTraderAccount(1);
  }

  @Test (expected = DataRetrievalFailureException.class)
  public void quoteException() {
    Position mockPosition = new Position();
    mockPosition.setAccountId(1);
    mockPosition.setPosition(1);
    mockPosition.setTicker("AMD");

    List<Position> mockPositionList = Collections.singletonList(mockPosition);
    when(mockPositionDao.findAllById(any())).thenReturn(mockPositionList);

    Optional<Quote> optionalMockQuote = Optional.empty();
    when(mockQuoteDao.findById(any())).thenReturn(optionalMockQuote);

    PortfolioView portfolioView = dashboardService.getProfileViewByTraderId(1);
  }
}