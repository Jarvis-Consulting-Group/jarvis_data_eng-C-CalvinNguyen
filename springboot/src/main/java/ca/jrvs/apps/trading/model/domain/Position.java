package ca.jrvs.apps.trading.model.domain;

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
