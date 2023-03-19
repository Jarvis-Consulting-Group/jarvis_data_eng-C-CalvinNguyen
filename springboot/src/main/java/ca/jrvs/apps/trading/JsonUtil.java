package ca.jrvs.apps.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * Contains a static method which maps a String to an object (DTO).
 */
public class JsonUtil {

  /**
   * Uses the ObjectMapper to populate an object with the data in the given String.
   * @param json Json String containing the data .
   * @param mapClass class which will be used to create an object and populate the data.
   * @return returns the object populated with data from the Json String.
   * @param <T> converts Object to type of the class.
   * @throws IOException throws an IOException if there is an error in the process of mapping.
   */
  public static <T> T toObjectFromJson(String json, Class mapClass) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    return (T) objectMapper.readValue(json, mapClass);
  }

}
