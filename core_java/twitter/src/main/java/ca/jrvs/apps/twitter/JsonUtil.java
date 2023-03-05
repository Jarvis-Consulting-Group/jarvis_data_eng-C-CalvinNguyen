package ca.jrvs.apps.twitter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;

/**
 * Class containing static methods for handling JSON strings, such as mapping them to an object or
 * converting an object into a JSON string.
 */
public class JsonUtil {

  /**
   * Uses the ObjectMapper to map a JSON string into an object.
   * @param json the JSON string passed (JSON string from HttpResponse Twitter API V2).
   * @param clazz the class object the JSON string will be mapped to.
   * @return returns the created object mapped with data from the JSON string.
   * @param <T> generic object to map any specific type of object.
   * @throws IOException throws an IOException if it was unable to map the object.
   */
  public static  <T> T toObjectFromJson(String json, Class clazz) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    return (T) objectMapper.readValue(json, clazz);
  }

  /**
   * uses the ObjectMapper to take an object and presents all of its data within a JSON string.
   * @param object the Object where the data will be presented as a JSON string.
   * @param prettyJson boolean value, the option of the ObjectMapper whether to use PrettyJson or not.
   * @param includeNullValues boolean value, the option of the ObjectMapper whether to show null values.
   * @return returns a String containing properties/the state of the object passed.
   * @throws JsonProcessingException Throws an JsonProcessingException if it was unable to convert.
   */
  public static String toJson(Object object, boolean prettyJson, boolean includeNullValues)
      throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();

    if (!includeNullValues) {
      objectMapper.setSerializationInclusion(Include.NON_NULL);
    }
    if (prettyJson) {
      objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    return objectMapper.writeValueAsString(object);
  }
}
