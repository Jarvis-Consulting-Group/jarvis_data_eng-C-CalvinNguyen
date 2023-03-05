package ca.jrvs.apps.twitter.model.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Includes {

  private Places places;

  public Places getPlaces() {
    return places;
  }

  public void setPlaces(Places places) {
    this.places = places;
  }

  @Override
  public String toString() {
    return "Includes{"
        + "places=" + places
        + '}';
  }
}
