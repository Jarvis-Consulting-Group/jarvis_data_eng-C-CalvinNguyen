package ca.jrvs.apps.twitter.model.v2;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Hashtag object used for mapping the hashtags object within the data/tweet object returned from
 * the Twitter API V2 JSON string.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HashtagV2 {

  private int start;
  private int end;
  private String tag;

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public int getEnd() {
    return end;
  }

  public void setEnd(int end) {
    this.end = end;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  @Override
  public String toString() {
    return "Hashtag{"
        + "start=" + start
        + ", end=" + end
        + ", tag='" + tag + '\''
        + '}';
  }
}
