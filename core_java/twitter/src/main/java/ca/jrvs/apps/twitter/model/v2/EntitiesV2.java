package ca.jrvs.apps.twitter.model.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntitiesV2 {

  private List<MentionsV2> mentions;
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
