package ca.jrvs.apps.trading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller used to check if the application is running.
 */
@Controller
@RequestMapping("/health")
public class AppController {

  @Autowired
  public AppController() {
  }

  /**
   * If the application is healthy and running, it should return an HTTP STATUS OK with a message.
   *
   * @return String in the response body saying that the application is healthy and running.
   */
  @GetMapping(path = "/")
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  public String healthCheck() {
    try {
      return "I'm healthy v2";
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }
}
