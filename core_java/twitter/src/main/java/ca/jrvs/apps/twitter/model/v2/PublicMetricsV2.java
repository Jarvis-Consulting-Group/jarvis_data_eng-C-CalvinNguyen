package ca.jrvs.apps.twitter.model.v2;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublicMetricsV2 {

  private int retweet_count;
  private int reply_count;
  private int like_count;
  private int quote_count;
  private int impression_count;

  public int getRetweet_count() {
    return retweet_count;
  }

  public void setRetweet_count(int retweet_count) {
    this.retweet_count = retweet_count;
  }

  public int getReply_count() {
    return reply_count;
  }

  public void setReply_count(int reply_count) {
    this.reply_count = reply_count;
  }

  public int getLike_count() {
    return like_count;
  }

  public void setLike_count(int like_count) {
    this.like_count = like_count;
  }

  public int getQuote_count() {
    return quote_count;
  }

  public void setQuote_count(int quote_count) {
    this.quote_count = quote_count;
  }

  public int getImpression_count() {
    return impression_count;
  }

  public void setImpression_count(int impression_count) {
    this.impression_count = impression_count;
  }

  @Override
  public String toString() {
    return "PublicMetrics{"
        + "retweet_count=" + retweet_count
        + ", reply_count=" + reply_count
        + ", like_count=" + like_count
        + ", quote_count=" + quote_count
        + ", impression_count=" + impression_count
        + '}';
  }
}
