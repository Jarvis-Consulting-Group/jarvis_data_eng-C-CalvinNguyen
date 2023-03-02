package ca.jrvs.apps.twitter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;

public class JsonUtil {

  public static  <T> T toObjectFromJson(String json, Class clazz) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    return (T) objectMapper.readValue(json, clazz);
  }

  public static String toJson(Object object, boolean prettyJson, boolean includeNullValues)
      throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();

    if(!includeNullValues) {
      objectMapper.setSerializationInclusion(Include.NON_NULL);
    }
    if(prettyJson) {
      objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    return objectMapper.writeValueAsString(object);
  }
}
