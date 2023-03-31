package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA DAO for accounts that extends the JpaRepository.
 */
@Repository
public interface AccountJpaDao extends JpaRepository<Account, Integer> {
  Account getAccountByTraderId(Integer traderId);
}
