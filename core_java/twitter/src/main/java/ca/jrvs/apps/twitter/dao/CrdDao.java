package ca.jrvs.apps.twitter.dao;

/**
 * Interface that once implemented will call endpoints using HttpHelper (TwitterHttpHelper) and
 * return Twitter objects populated by information from the HttpResponse jsons.
 * @param <T> Generic Object (WIll be Tweet).
 * @param <S> Generic Object (Will be String).
 */
public interface CrdDao<T, S> {

  T create(T entity);

  T findById(S s);

  T deleteById(S s);

}
