package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.domain.MarketOrder;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller that is mapped to the order URL path (/order) and calls the appropriate
 * method when a path segment is given.
 */
@Controller
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/order")
public class OrderController {

  private OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  /**
   * Method is called when the WebServlet is given a post request at the path
   * /order/marketOrder and this method calls the orderService to either buy or sell stocks.
   *
   * @param marketOrder market order within the request body.
   * @return returns the Security Order created from the market order.
   */
  @PostMapping(path = "/marketOrder")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public SecurityOrder postMarketOrder(@RequestBody MarketOrder marketOrder) {
    try {
      return orderService.executeMarketOrder(marketOrder);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }
}
