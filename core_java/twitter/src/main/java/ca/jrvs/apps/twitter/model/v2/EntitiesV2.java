package ca.jrvs.apps.twitter.model.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Entities object used for mapping the entities object within the data/tweet object returned in
 * JSON string from the Twitter API V2.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({
    "cashtags",
    "urls",
    "annotations"
})
public class EntitiesV2 {

  @JsonProperty("mentions")
  private List<MentionsV2> mentions;
  @JsonProperty("hashtags")
  private List<HashtagV2> hashtagV2s;

  public List<MentionsV2> getMentions() {
    return mentions;
  }

  public void setMentions(List<MentionsV2> mentions) {
    this.mentions = mentions;
  }

  public List<HashtagV2> getHashtags() {
    return hashtagV2s;
  }

  public void setHashtags(List<HashtagV2> hashtagV2s) {
    this.hashtagV2s = hashtagV2s;
  }

  @Override
  public String toString() {
    return "Entities{"
        + "mentions=" + mentions
        + ", hashtags=" + hashtagV2s
        + '}';
  }
}
