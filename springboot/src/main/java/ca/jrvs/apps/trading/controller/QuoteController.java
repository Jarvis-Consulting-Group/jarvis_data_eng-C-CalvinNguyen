package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.service.QuoteService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller that is mapped to the quote URL path (/quote) and calls the appropriate method when a
 * path segment is given.
 */
@Controller
@RequestMapping("/quote")
public class QuoteController {

  private final QuoteService quoteService;

  @Autowired
  public QuoteController(QuoteService quoteService) {
    this.quoteService = quoteService;
  }

  /**
   * Method is called when the WebServlet is given a get request at the path
   * /quote/iex/ticker/{ticker} and this method calls the quoteService with the path variable ticker
   * representing a unique ID/symbol/ticker as a String.
   *
   * @param ticker String representing unique ID/symbol/ticker.
   * @return returns IexQuote object that Spring / WebServlet automatically converts into a JSON.
   */
  //@ApiOperation(value = "Show iexQuote", notes = "Shows iexQuote for a given ticker/symbol/id")
  //@ApiResponses(value = {@ApiResponse(code = 404, message = "ticker/symbol not found")})
  @GetMapping(path = "/iex/ticker/{ticker}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public IexQuote getQuote(@PathVariable String ticker) {
    try {
      return quoteService.findIexQuoteBySymbol(ticker);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

  /**
   * Method is called when the WebServlet is given a put request at the path /quote/iexMarketData,
   * and this method calls the quoteService updateMarketData method which updates all the Quotes
   * within the database by the data from the IEX REST API.
   *
   * @return returns the updated list of Quotes.
   */
  @PutMapping(path = "/iexMarketData")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Quote> updateMarketData() {
    try {
      return quoteService.updateMarketData();
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

  /**
   * Method is called when the WebServlet is given a put request at the path /quote/ and this method
   * calls the quoteService saveQuote method which updates a Quote in the database.
   *
   * @param quote Quote object from the put request body.
   * @return returns the updated Quote.
   */
  @PutMapping(path = "/")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Quote putQuote(@RequestBody Quote quote) {
    try {
      return quoteService.saveQuote(quote);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

  /**
   * Method is called when the WebServlet is given a post request at the path
   * /quote/tickerId/{tickerId}, and this method calls the quoteService saveQuote with a String
   * tickerId. This gets an IexQuote from the IEX REST API, builds a Quote object from it, and adds
   * that Quote object to the database.
   *
   * @param tickerId String representing the IexQuote/Quote symbol/ticker.
   * @return returns the new saved Quote object.
   */
  @PostMapping(path = "/tickerId/{tickerId}")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public Quote createQuote(@PathVariable String tickerId) {
    try {
      return quoteService.saveQuote(tickerId);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

  /**
   * Method is called when the WebServlet is given a get request at the path /quote/dailyList, and
   * this method calls the quoteService findAllQuotes method which gets a List of all the Quotes in
   * the database.
   *
   * @return returns the List of all the Quotes in the database.
   */
  @GetMapping(path = "/dailyList")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Quote> getDailyList() {
    try {
      return quoteService.findAllQuotes();
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }
}
