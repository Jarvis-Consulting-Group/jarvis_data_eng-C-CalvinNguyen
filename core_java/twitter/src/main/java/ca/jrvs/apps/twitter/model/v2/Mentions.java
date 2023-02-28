package ca.jrvs.apps.twitter.model.v2;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Mentions {

  private int start;
  private int end;
  private String username;
  private String id;

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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Mentions{"
        + "start=" + start
        + ", end=" + end
        + ", username='" + username + '\''
        + ", id='" + id + '\''
        + '}';
  }
}
