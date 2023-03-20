package ca.jrvs.apps.trading.model.domain;

/**
 * Data Transfer Object (DTO) for the Quote records/objects of the Quote Table
 * in the PostgresSQL Database.
 */
public class Quote implements Entity<String> {

  private String symbol;
  private Double lastPrice;
  private Double bidPrice;
  private Integer bidSize;
  private Double askPrice;
  private Integer askSize;

  @Override
  public String getId() {
    return symbol;
  }

  @Override
  public void setId(String s) {
    this.symbol = s;
  }
}
