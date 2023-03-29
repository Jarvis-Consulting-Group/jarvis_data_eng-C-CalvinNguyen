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

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

  private DashboardService dashboardService;

  @Autowired
  public DashboardController(DashboardService dashboardService) {
    this.dashboardService = dashboardService;
  }

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
