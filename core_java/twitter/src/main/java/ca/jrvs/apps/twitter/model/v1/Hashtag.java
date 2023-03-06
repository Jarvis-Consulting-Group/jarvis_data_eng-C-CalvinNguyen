package ca.jrvs.apps.twitter.model.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Hashtag {
  // API v1.1
  private String text;
  private List<Integer> indices;

  // API v2
  /*
  private int start;
  private int end;
  private String tag;
  */

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public List<Integer> getIndices() {
    return indices;
  }

  public void setIndices(List<Integer> indices) {
    this.indices = indices;
  }

  /*
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

   */
}
