package ca.jrvs.apps.twitter.model.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Entities {

  private List<Mentions> mentions;
  private List<Hashtag> hashtags;

  public List<Mentions> getMentions() {
    return mentions;
  }

  public void setMentions(List<Mentions> mentions) {
    this.mentions = mentions;
  }

  public List<Hashtag> getHashtags() {
    return hashtags;
  }

  public void setHashtags(List<Hashtag> hashtags) {
    this.hashtags = hashtags;
  }

  @Override
  public String toString() {
    return "Entities{"
        + "mentions=" + mentions
        + ", hashtags=" + hashtags
        + '}';
  }
}
