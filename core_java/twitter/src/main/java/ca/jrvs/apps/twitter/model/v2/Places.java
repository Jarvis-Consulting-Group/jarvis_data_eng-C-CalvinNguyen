package ca.jrvs.apps.twitter.model.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({
    "full_name",
    "id"
})
public class Places {

  private Geo geo;

  public Geo getGeo() {
    return geo;
  }

  public void setGeo(Geo geo) {
    this.geo = geo;
  }

  @Override
  public String toString() {
    return "Places{"
        + "geo=" + geo
        + '}';
  }
}
