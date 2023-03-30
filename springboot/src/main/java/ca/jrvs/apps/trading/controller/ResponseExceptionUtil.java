package ca.jrvs.apps.trading.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Returns a ResponseStatusException with the appropriate HTTP status depending on the exception
 * within the dependencies.
 */
public class ResponseExceptionUtil {

  public static final Logger logger = LoggerFactory.getLogger(ResponseExceptionUtil.class);

  /**
   * If an exception is thrown in any of the dependencies, throw a ResponseStatusException, one
   * being a bad request if there is an IllegalArgumentException and the other being an internal
   * server error for unexpected errors.
   *
   * @param e the exception from the dependencies.
   * @return returns the ResponseStatusException with the appropriate message and Http Status.
   */
  public static ResponseStatusException getResponseStatusException(Exception e) {
    if (e instanceof IllegalArgumentException) {
      logger.debug("Invalid input", e);
      return new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    } else {
      logger.error("Internal error", e);
      return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error");
    }
  }

}
