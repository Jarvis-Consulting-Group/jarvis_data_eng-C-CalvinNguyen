package ca.jrvs.apps.trading.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.MarketOrder;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataRetrievalFailureException;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceUnitTest {

  @Captor
  ArgumentCaptor<SecurityOrder> securityOrderArgumentCaptor;

  @Mock
  private AccountDao mockAccountDao;
  @Mock
  private SecurityOrderDao mockSecurityOrderDao;
  @Mock
  private QuoteDao mockQuoteDao;
  @Mock
  private PositionDao mockPositionDao;

  @InjectMocks
  private OrderService orderService;

  private MarketOrder savedMarketOrder;
  private Quote savedQuote;
  private Account savedAccount;
  private Position savedPosition;

  @Before
  public void setUp() {
    MarketOrder marketOrder = new MarketOrder();
    marketOrder.setAccountId(1);
    marketOrder.setTicker("AMD");
    marketOrder.setSize(1);
    savedMarketOrder = marketOrder;

    Quote mockQuote = new Quote();
    mockQuote.setTicker("AMD");
    mockQuote.setLastPrice(10d);
    mockQuote.setAskPrice(10d);
    mockQuote.setAskSize(1);
    mockQuote.setBidPrice(10d);
    mockQuote.setBidSize(1);
    savedQuote = mockQuote;

    Account mockAccount = new Account();
    mockAccount.setId(1);
    mockAccount.setTraderId(1);
    mockAccount.setAmount(1000.0);
    savedAccount = mockAccount;

    Position mockPosition = new Position();
    mockPosition.setAccountId(mockAccount.getId());
    mockPosition.setPosition(1);
    mockPosition.setTicker(mockQuote.getTicker());
    savedPosition = mockPosition;
  }

  @Test
  public void successBuyOrder() {
    Optional<Quote> mockOptionalQuote = Optional.of(savedQuote);
    when(mockQuoteDao.findById("AMD")).thenReturn(mockOptionalQuote);

    Optional<Account> mockOptionalAccount = Optional.of(savedAccount);
    when(mockAccountDao.findById(savedMarketOrder.getAccountId())).thenReturn(mockOptionalAccount);

    orderService.executeMarketOrder(savedMarketOrder);

    verify(mockSecurityOrderDao).save(securityOrderArgumentCaptor.capture());
    SecurityOrder capturedSecurityOrder = securityOrderArgumentCaptor.getValue();

    assertEquals("FILLED", capturedSecurityOrder.getStatus());
    assertEquals("AMD", capturedSecurityOrder.getTicker());
    assertEquals(1, capturedSecurityOrder.getAccountId().intValue());
  }

  @Test
  public void failBuyOrder() {
    Optional<Quote> mockOptionalQuote = Optional.of(savedQuote);
    when(mockQuoteDao.findById("AMD")).thenReturn(mockOptionalQuote);

    savedAccount.setAmount(1d);
    Optional<Account> mockOptionalAccount = Optional.of(savedAccount);
    when(mockAccountDao.findById(savedMarketOrder.getAccountId())).thenReturn(mockOptionalAccount);

    orderService.executeMarketOrder(savedMarketOrder);

    verify(mockSecurityOrderDao).save(securityOrderArgumentCaptor.capture());
    SecurityOrder capturedSecurityOrder = securityOrderArgumentCaptor.getValue();

    assertEquals("CANCELED", capturedSecurityOrder.getStatus());
    assertEquals("AMD", capturedSecurityOrder.getTicker());
    assertEquals(1, capturedSecurityOrder.getAccountId().intValue());
  }

  @Test
  public void successSellOrder() {
    Optional<Quote> mockOptionalQuote = Optional.of(savedQuote);
    when(mockQuoteDao.findById("AMD")).thenReturn(mockOptionalQuote);

    Optional<Account> mockOptionalAccount = Optional.of(savedAccount);
    when(mockAccountDao.findById(savedMarketOrder.getAccountId())).thenReturn(mockOptionalAccount);

    Optional<Position> mockOptionalPosition = Optional.of(savedPosition);
    when(mockPositionDao.findByAccountIdAndTicker(savedAccount.getId(), savedQuote.getTicker()))
        .thenReturn(mockOptionalPosition);

    savedMarketOrder.setSize(-1);
    orderService.executeMarketOrder(savedMarketOrder);

    verify(mockSecurityOrderDao).save(securityOrderArgumentCaptor.capture());
    SecurityOrder capturedSecurityOrder = securityOrderArgumentCaptor.getValue();

    assertEquals("FILLED", capturedSecurityOrder.getStatus());
    assertEquals("AMD", capturedSecurityOrder.getTicker());
    assertEquals(1, capturedSecurityOrder.getAccountId().intValue());
  }

  @Test
  public void failSellOrder() {
    Optional<Quote> mockOptionalQuote = Optional.of(savedQuote);
    when(mockQuoteDao.findById("AMD")).thenReturn(mockOptionalQuote);

    Optional<Account> mockOptionalAccount = Optional.of(savedAccount);
    when(mockAccountDao.findById(savedMarketOrder.getAccountId())).thenReturn(mockOptionalAccount);

    Optional<Position> mockOptionalPosition = Optional.of(savedPosition);
    when(mockPositionDao.findByAccountIdAndTicker(savedAccount.getId(), savedQuote.getTicker()))
        .thenReturn(mockOptionalPosition);

    savedMarketOrder.setSize(-2);
    orderService.executeMarketOrder(savedMarketOrder);

    verify(mockSecurityOrderDao).save(securityOrderArgumentCaptor.capture());
    SecurityOrder capturedSecurityOrder = securityOrderArgumentCaptor.getValue();

    assertEquals("CANCELED", capturedSecurityOrder.getStatus());
    assertEquals("AMD", capturedSecurityOrder.getTicker());
    assertEquals(1, capturedSecurityOrder.getAccountId().intValue());
  }

  @Test
  public void marketOrderException() {
    savedMarketOrder.setSize(null);
    try {
      orderService.executeMarketOrder(savedMarketOrder);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    savedMarketOrder.setSize(0);
    try {
      orderService.executeMarketOrder(savedMarketOrder);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    savedMarketOrder.setSize(1);
    savedMarketOrder.setAccountId(null);
    try {
      orderService.executeMarketOrder(savedMarketOrder);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    savedMarketOrder.setAccountId(1);
    savedMarketOrder.setTicker(null);
    try {
      orderService.executeMarketOrder(savedMarketOrder);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    savedMarketOrder = null;
    try {
      orderService.executeMarketOrder(savedMarketOrder);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test (expected = DataRetrievalFailureException.class)
  public void quoteDaoException() {
    Optional<Quote> mockOptionalQuote = Optional.empty();
    when(mockQuoteDao.findById("AMD")).thenReturn(mockOptionalQuote);

    orderService.executeMarketOrder(savedMarketOrder);
  }

  @Test (expected = DataRetrievalFailureException.class)
  public void accountDaoException() {
    Optional<Quote> mockOptionalQuote = Optional.of(savedQuote);
    when(mockQuoteDao.findById("AMD")).thenReturn(mockOptionalQuote);

    Optional<Account> mockOptionalAccount = Optional.empty();
    when(mockAccountDao.findById(savedMarketOrder.getAccountId())).thenReturn(mockOptionalAccount);

    orderService.executeMarketOrder(savedMarketOrder);
  }

  @Test (expected = IllegalArgumentException.class)
  public void positionDaoException() {
    Optional<Quote> mockOptionalQuote = Optional.of(savedQuote);
    when(mockQuoteDao.findById("AMD")).thenReturn(mockOptionalQuote);

    Optional<Account> mockOptionalAccount = Optional.of(savedAccount);
    when(mockAccountDao.findById(savedMarketOrder.getAccountId())).thenReturn(mockOptionalAccount);

    Optional<Position> mockOptionalPosition = Optional.empty();
    when(mockPositionDao.findByAccountIdAndTicker(savedAccount.getId(), savedQuote.getTicker()))
        .thenReturn(mockOptionalPosition);

    savedMarketOrder.setSize(-1);
    orderService.executeMarketOrder(savedMarketOrder);
  }
}