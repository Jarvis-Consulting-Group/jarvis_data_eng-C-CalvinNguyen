package ca.jrvs.apps.trading.model.domain;

/**
 * Position represents the position view within the database which is the accountId, the sum of all
 * security order sizes for a given symbol/ticker quote.
 */
public class Position implements Entity<Integer> {

  private Integer accountId;
  private Integer position;
  private String ticker;



  public Integer getAccountId() {
    return accountId;
  }

  public void setAccountId(Integer accountId) {
    this.accountId = accountId;
  }

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  @Override
  public Integer getId() {
    return null;
  }

  @Override
  public void setId(Integer integer) {

  }
}
