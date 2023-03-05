package ca.jrvs.apps.twitter.model.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * TwitterV2 class used for mapping objects from JSONs returned from the Twitter API V2.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({
    "edit_history_tweet_ids"
})
public class TweetV2 {

  @JsonProperty("id")
  private String id;
  @JsonProperty("text")
  private String text;
  @JsonProperty("created_at")
  private Date createdAt;
  @JsonProperty("entities")
  private EntitiesV2 entitiesV2;
  @JsonProperty("public_metrics")
  private PublicMetricsV2 publicMetrics;

  private boolean deleted = false;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public EntitiesV2 getEntities() {
    return entitiesV2;
  }

  public void setEntities(EntitiesV2 entitiesV2) {
    this.entitiesV2 = entitiesV2;
  }

  public PublicMetricsV2 getPublicMetrics() {
    return publicMetrics;
  }

  public void setPublicMetrics(PublicMetricsV2 publicMetrics) {
    this.publicMetrics = publicMetrics;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  @Override
  public String toString() {
    return "Tweet{" + "\n"
        + "id='" + id + '\'' + "\n"
        + ", text='" + text + '\'' + "\n"
        + ", created_at=" + createdAt + "\n"
        + ", entities=" + entitiesV2 + "\n"
        + ", public_metrics=" + publicMetrics + "\n"
        + ", deleted=" + deleted + "\n"
        + '}';
  }
}
