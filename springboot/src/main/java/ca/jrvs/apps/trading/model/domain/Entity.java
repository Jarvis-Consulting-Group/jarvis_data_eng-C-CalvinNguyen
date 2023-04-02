package ca.jrvs.apps.trading.model.domain;

/**
 * Interface to be implemented by several DTOs with DAOs that extend the
 * JdbcCrudDao<Entity, Integer>.
 * @param <ID>
 */
public interface Entity<ID> {

  ID getId();

  void setId(ID id);
}
