package ca.jrvs.apps.twitter.model.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Data class that will be used for mapping Tweet objects (it contains one tweet object), the JSOn
 * is {data: {...}}.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "data"
})
public class Data {
  @JsonProperty("data")
  private TweetV2 tweetV2;

  public TweetV2 getTweet() {
    return tweetV2;
  }

  public void setTweets(TweetV2 tweetV2) {
    this.tweetV2 = tweetV2;
  }
}
