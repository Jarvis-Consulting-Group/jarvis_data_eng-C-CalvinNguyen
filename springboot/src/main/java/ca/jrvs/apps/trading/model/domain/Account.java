package ca.jrvs.apps.trading.model.domain;

public class Account implements Entity<Integer> {

  private Integer id;

  private Integer traderId;

  private Number amount;


  @Override
  public Integer getId() {
    return this.id;
  }

  @Override
  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getTraderId() {
    return traderId;
  }

  public void setTraderId(Integer traderId) {
    this.traderId = traderId;
  }

  public Number getAmount() {
    return amount;
  }

  public void setAmount(Number amount) {
    this.amount = amount;
  }
}
