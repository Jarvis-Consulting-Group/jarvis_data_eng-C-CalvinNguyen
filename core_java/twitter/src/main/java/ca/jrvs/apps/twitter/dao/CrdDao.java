package ca.jrvs.apps.twitter.dao;

/**
 * Interface that once implemented will call endpoints using HttpHelper (TwitterHttpHelper) and
 * return Twitter objects populated by information from the HttpResponse jsons.
 * @param <T> Generic Object (WIll be Tweet).
 * @param <S> Generic Object (Will be String).
 */
public interface CrdDao<T, S> {

  /**
   * Will call the httpPost method from the HttpHelper using the generic object passed in
   * the arguments (This will specifically be a TweetV2 Object).
   * @param entity the generic type object passed (In this project it will be TweetV2 object).
   * @return returns the TweetV2 object created from the HttpResponse JSON string.
   */
  T create(T entity);

  /**
   * Will call the httpGet method from the HttpHelper using the generic S object passed in
   * the arguments (This will specifically be a String object).
   * @param s the generic type object passed (In this project it will be a String object for ID).
   * @return returns the TweetV2 object created from the HttpResponse JSON string.
   */
  T findById(S s);

  /**
   *  Will call the httpDelete method from the HttpHelper using the generic S object passed in
   *  the arguments (This will specifically be a String object).
   * @param s the generic type object passed (in this project it will be a String object for ID).
   * @return returns the TweetV2 object created from the httpResponse JSON string. (API V2 only
   * passes (data: {deleted: true}) so the deleted property in the TweetV2 will be set to true.
   */
  T deleteById(S s);

}
