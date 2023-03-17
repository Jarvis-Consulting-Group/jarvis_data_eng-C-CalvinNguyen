package ca.jrvs.apps.trading;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class JsonUtil {

  public static <T> T toObjectFromJson(String json, Class mapClass) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    return (T) objectMapper.readValue(json, mapClass);
  }

}
