package org.apac.erp.cach.forecast.persistence.repositories;

import org.apac.erp.cach.forecast.persistence.entities.ProviderAttachedInvoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderAttachedInvoicesRepository  extends JpaRepository<ProviderAttachedInvoices, Long> {

	ProviderAttachedInvoices findByExternalId(String externalId);

}
