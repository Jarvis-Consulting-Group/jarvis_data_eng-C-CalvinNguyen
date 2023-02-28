package ca.jrvs.apps.twitter.model.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({
    "edit_history_tweet_ids"
})
public class TweetV2 {

  private String id;
  private String text;
  private Date created_at;
  private EntitiesV2 entitiesV2;
  private PublicMetricsV2 public_metrics;

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

  public Date getCreated_at() {
    return created_at;
  }

  public void setCreated_at(Date created_at) {
    this.created_at = created_at;
  }

  public EntitiesV2 getEntities() {
    return entitiesV2;
  }

  public void setEntities(EntitiesV2 entitiesV2) {
    this.entitiesV2 = entitiesV2;
  }

  public PublicMetricsV2 getPublic_metrics() {
    return public_metrics;
  }

  public void setPublic_metrics(PublicMetricsV2 public_metrics) {
    this.public_metrics = public_metrics;
  }

  @Override
  public String toString() {
    return "Tweet{" + "\n"
        + "id='" + id + '\'' + "\n"
        + ", text='" + text + '\'' + "\n"
        + ", created_at=" + created_at + "\n"
        + ", entities=" + entitiesV2 + "\n"
        + ", public_metrics=" + public_metrics + "\n"
        + '}';
  }
}
