package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.domain.PortfolioView;
import ca.jrvs.apps.trading.model.domain.TraderAccountView;
import ca.jrvs.apps.trading.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller that is mapped to the dashboard URL path (/dashboard) and calls the appropriate
 * method when a path segment is given.
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

  private DashboardService dashboardService;

  @Autowired
  public DashboardController(DashboardService dashboardService) {
    this.dashboardService = dashboardService;
  }

  /**
   * Method is called when the WebServlet is given a get request at the path
   * /dashboard/profile/traderId/{traderId} and the method gets the TraderAccountView with the
   * Trader and Account information.
   * @param traderId TraderId used to get the Trader and Account.
   * @return TraderAccountView containing a Trader and Account.
   */
  @GetMapping(path = "/profile/traderId/{traderId}")
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  public TraderAccountView getAccount(@PathVariable Integer traderId) {
    try {
      return dashboardService.getTraderAccount(traderId);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

  /**
   * Method is called when the WebServlet is given a get request at the path
   * /dashboard/portfolio/traderId/{traderId} and the method gets the PortfolioView which has
   * the security rows (position, quote, etc.).
   *
   * @param traderId TraderId used to get the PortfolioView for that Trader.
   * @return PortfolioView containing security rows (trader's positions, quote, etc.).
   */
  @GetMapping(path = "/portfolio/traderId/{traderId}")
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  public PortfolioView getPortfolioView(@PathVariable Integer traderId) {
    try {
      return dashboardService.getProfileViewByTraderId(traderId);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }
}
