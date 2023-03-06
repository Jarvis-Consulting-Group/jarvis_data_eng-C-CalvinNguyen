package ca.jrvs.apps.twitter.model.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Date;

/**
 * TwitterV2 class used for mapping the data and includes objects from JSONs returned from
 * the Twitter API V2.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "data"
})
public class TweetV2 {

  @JsonProperty("data")
  private Data data;

  /*
  @JsonProperty("includes")
  private Includes includes;
   */

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  /*
  public Includes getIncludes() {
    return includes;
  }

  public void setIncludes(Includes includes) {
    this.includes = includes;
  }
   */

  @Override
  public String toString() {
    return "TweetV2{"
        + "data=" + data
        + '}';
  }
}
