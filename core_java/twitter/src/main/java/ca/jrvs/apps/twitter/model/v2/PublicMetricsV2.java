package ca.jrvs.apps.twitter.model.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PublicMetricsV2 class used for mapping the public_metrics object within the data/tweet object in
 * the JSON object in the Twitter API V2.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublicMetricsV2 {

  @JsonProperty("retweet_count")
  private int retweetCount;
  @JsonProperty("reply_count")
  private int replyCount;
  @JsonProperty("like_count")
  private int likeCount;
  @JsonProperty("quote_count")
  private int quoteCount;
  @JsonProperty("impression_count")
  private int impressionCount;

  public int getRetweetCount() {
    return retweetCount;
  }

  public void setRetweetCount(int retweetCount) {
    this.retweetCount = retweetCount;
  }

  public int getReplyCount() {
    return replyCount;
  }

  public void setReplyCount(int replyCount) {
    this.replyCount = replyCount;
  }

  public int getLikeCount() {
    return likeCount;
  }

  public void setLikeCount(int likeCount) {
    this.likeCount = likeCount;
  }

  public int getQuoteCount() {
    return quoteCount;
  }

  public void setQuoteCount(int quoteCount) {
    this.quoteCount = quoteCount;
  }

  public int getImpressionCount() {
    return impressionCount;
  }

  public void setImpressionCount(int impressionCount) {
    this.impressionCount = impressionCount;
  }

  @Override
  public String toString() {
    return "PublicMetricsV2{"
        + "retweetCount=" + retweetCount
        + ", replyCount=" + replyCount
        + ", likeCount=" + likeCount
        + ", quoteCount=" + quoteCount
        + ", impressionCount=" + impressionCount
        + '}';
  }
}
