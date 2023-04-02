package ca.jrvs.apps.trading.model.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Data Transfer Object (DTO) for the Quote records/objects of the Quote Table
 * in the PostgresSQL Database.
 */
@javax.persistence.Entity
@Table(name = "quote")
public class Quote implements Entity<String> {

  @Id
  @Column(name = "ticker")
  private String ticker;
  @Column(name = "last_price")
  private Double lastPrice;
  @Column(name = "bid_price")
  private Double bidPrice;
  @Column(name = "bid_size")
  private Integer bidSize;
  @Column(name = "ask_price")
  private Double askPrice;
  @Column(name = "ask_size")
  private Integer askSize;

  @Override
  public String getId() {
    return ticker;
  }

  @Override
  public void setId(String s) {
    this.ticker = s;
  }

  public Double getLastPrice() {
    return lastPrice;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public void setLastPrice(Double lastPrice) {
    this.lastPrice = lastPrice;
  }

  public Double getBidPrice() {
    return bidPrice;
  }

  public void setBidPrice(Double bidPrice) {
    this.bidPrice = bidPrice;
  }

  public Integer getBidSize() {
    return bidSize;
  }

  public void setBidSize(Integer bidSize) {
    this.bidSize = bidSize;
  }

  public Double getAskPrice() {
    return askPrice;
  }

  public void setAskPrice(Double askPrice) {
    this.askPrice = askPrice;
  }

  public Integer getAskSize() {
    return askSize;
  }

  public void setAskSize(Integer askSize) {
    this.askSize = askSize;
  }
}
