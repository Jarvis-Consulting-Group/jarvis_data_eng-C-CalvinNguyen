package ca.jrvs.apps.twitter.model.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Entities {

  private List<Hashtag> hashtags;

  // API v1.1
  private List<UserMention> user_mentions;

  // API v2
  private List<UserMention> mentions;

  public List<Hashtag> getHashtags() {
    return hashtags;
  }

  public void setHashtags(List<Hashtag> hashtags) {
    this.hashtags = hashtags;
  }

  public List<UserMention> getUser_mentions() {
    return user_mentions;
  }

  public void setUser_mentions(List<UserMention> user_mentions) {
    this.user_mentions = user_mentions;
  }

  /*
  public List<UserMention> getMentions() {
    return mentions;
  }

  public void setMentions(List<UserMention> mentions) {
    this.mentions = mentions;
  }
   */
}
