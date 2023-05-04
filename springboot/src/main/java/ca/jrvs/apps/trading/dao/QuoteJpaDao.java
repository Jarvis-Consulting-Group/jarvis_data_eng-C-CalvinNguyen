package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA DAO for Quotes that extends the JpaRepository.
 */
@Repository
public interface QuoteJpaDao extends JpaRepository<Quote, String> {

}