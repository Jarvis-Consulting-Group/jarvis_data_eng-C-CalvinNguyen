package ca.jrvs.apps.trading.model.domain;

import javax.persistence.*;

@javax.persistence.Entity
@Table(name = "account")
public class Account implements Entity<Integer> {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "trader_id")
  private Integer traderId;

  @Column(name = "amount")
  private Double amount;

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

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }
}
