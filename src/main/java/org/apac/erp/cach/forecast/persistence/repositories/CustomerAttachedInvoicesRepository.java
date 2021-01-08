package org.apac.erp.cach.forecast.persistence.repositories;

import org.apac.erp.cach.forecast.persistence.entities.CustomerAttachedInvoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAttachedInvoicesRepository  extends JpaRepository<CustomerAttachedInvoices, Long> {
	CustomerAttachedInvoices findByExternalId(String externalId);
}
