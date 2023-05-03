package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.domain.TraderAccountView;
import ca.jrvs.apps.trading.service.TraderAccountService;
import java.sql.Date;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller that is mapped to the trader URL path (/trader) and calls the appropriate method when
 * a path segment is given.
 */
@Controller
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/trader")
public class TraderAccountController {

  private final TraderAccountService traderAccountService;

  @Autowired
  public TraderAccountController(TraderAccountService traderAccountService) {
    this.traderAccountService = traderAccountService;
  }

  /**
   * Method is called when the WebServlet is given a post request at the path
   * /trader/firstname/{firstname}/lastname/{lastname}/dob/{dob}/country/{country}/email/{email} and
   * this method creates a Trader and Account given the properties from the path variables.
   *
   * @param firstname String firstname from the path variable.
   * @param lastname  String lastname from the path variable.
   * @param dob       LocalDate object created from a string in the path variable.
   * @param country   String country from the path variable.
   * @param email     String email from the path variable.
   * @return returns the TraderAccountView after creating the Trader and Account.
   */
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  @PostMapping(
      path = "/firstname/{firstname}/lastname/{lastname}/dob/{dob}/country/{country}/email/{email}"
  )
  public TraderAccountView createTrader(
      @PathVariable String firstname, @PathVariable String lastname,
      @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dob,
      @PathVariable String country, @PathVariable String email) {

    try {
      Trader trader = new Trader();
      trader.setFirstName(firstname);
      trader.setLastName(lastname);
      trader.setCountry(country);
      trader.setEmail(email);
      trader.setDob(Date.valueOf(dob));
      return traderAccountService.createTraderAndAccount(trader);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

  /**
   * Method is called when the WebServlet is given a post request at the path /trader/ and this
   * method creates a Trader and Account given the properties from the request body.
   *
   * @param trader trader object from the request body.
   * @return returns the TraderAccountView after creating the Trader and Account.
   */
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  @PostMapping(
      path = "/"
  )
  public TraderAccountView createTrader(@RequestBody Trader trader) {
    try {
      return traderAccountService.createTraderAndAccount(trader);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

  /**
   * Method is called when the WebServlet is given a delete request at the path
   * /trader/traderId/{traderId} and this method deletes the trader given the traderId in the
   * pathVariable.
   *
   * @param traderId traderId used to delete the Trader, Account and appropriate Security Orders.
   */
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(path = "/traderId/{traderId}")
  public void deleteTrader(@PathVariable Integer traderId) {
    try {
      traderAccountService.deleteTraderById(traderId);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

  /**
   * Method is called when the WebServlet is given a put request at the path
   * /trader/deposit/traderId/{traderId}/amount/{amount} and this method deposits the given amount
   * in the account of the appropriate trader using the traderId.
   *
   * @param traderId TraderId used to deposit funds into the appropriate account.
   * @param amount   Amount to be deposited.
   * @return returns the Account with the deposited funds.
   */
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  @PutMapping(
      path = "/deposit/traderId/{traderId}/amount/{amount}"
  )
  public Account depositFund(@PathVariable Integer traderId, @PathVariable Double amount) {
    try {
      return traderAccountService.deposit(traderId, amount);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

  /**
   * Method is called when the WebServlet is given a put request at the path
   * /trader/withdraw/traderId/{traderId}/amount/{amount} and this method withdraws funds from the
   * appropriate account given the traderId.
   *
   * @param traderId traderId used to get the appropriate account to withdraw funds from.
   * @param amount   amount to be withdrawn from the account.
   * @return returns the account with the funds withdrawn.
   */
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  @PutMapping(
      path = "/withdraw/traderId/{traderId}/amount/{amount}"
  )
  public Account withdrawFund(@PathVariable Integer traderId, @PathVariable Double amount) {
    try {
      return traderAccountService.withdraw(traderId, amount);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }
}
