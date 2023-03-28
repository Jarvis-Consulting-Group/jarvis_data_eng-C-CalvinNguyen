package ca.jrvs.apps.trading.model.domain;

import java.util.Date;

public class Trader implements Entity<Integer> {

  private Integer id;
  private String firstName;
  private String lastName;
  private String email;
  private String country;
  private String dob;


  @Override
  public Integer getId() {
    return this.id;
  }

  @Override
  public void setId(Integer integer) {
    this.id = integer;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getDob() {
    return dob;
  }

  public void setDob(String dob) {
    this.dob = dob;
  }
}
