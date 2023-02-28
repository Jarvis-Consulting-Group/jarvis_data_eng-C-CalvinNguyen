package ca.jrvs.apps.twitter.model.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "data"
})
public class Data {
  @JsonProperty("data")
  private Tweet tweet;

  public Tweet getTweet() {
    return tweet;
  }

  public void setTweets(Tweet tweet) {
    this.tweet = tweet;
  }
}
