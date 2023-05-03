package ca.jrvs.apps.trading.model.domain;

/**
 * DTO returned by the TraderAccountService, it includes the trader object and related account
 * object using records from the database.
 */
public class TraderAccountView {
  private Trader trader;
  private Account account;

  public Trader getTrader() {
    return trader;
  }

  public void setTrader(Trader trader) {
    this.trader = trader;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }
}
