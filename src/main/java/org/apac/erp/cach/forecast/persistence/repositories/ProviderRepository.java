package org.apac.erp.cach.forecast.persistence.repositories;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
	List<Provider> findAllByOrderByProviderLabel();
}
