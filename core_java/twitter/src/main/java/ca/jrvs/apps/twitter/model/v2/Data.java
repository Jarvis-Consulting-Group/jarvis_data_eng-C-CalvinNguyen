package ca.jrvs.apps.twitter.model.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Date;

/**
 * The Data class will be used for mapping the data property/object from the Tweet JSON. the JSON is
 * {data: {...}}.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({
    "edit_history_tweet_ids"
})
public class Data {

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
    return "Data{"
        + "id='" + id + '\''
        + ", text='" + text + '\''
        + ", createdAt=" + createdAt
        + ", entitiesV2=" + entitiesV2
        + ", publicMetrics=" + publicMetrics
        + ", deleted=" + deleted
        + '}';
  }
}
