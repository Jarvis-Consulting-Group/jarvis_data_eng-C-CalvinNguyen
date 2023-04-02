package ca.jrvs.apps.trading.model.domain;

/**
 * Object that is created when inputting a market order, the market order will be used for
 * information that will enter the security_order table and SecurityOrder DTO.
 */
public class MarketOrder {

  private Integer accountId;
  private Integer size;
  private String ticker;

  public Integer getAccountId() {
    return accountId;
  }

  public void setAccountId(Integer accountId) {
    this.accountId = accountId;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }
}
