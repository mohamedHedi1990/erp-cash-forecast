package org.apac.erp.cach.forecast.persistence.repositories;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.persistence.entities.Provider;
import org.apac.erp.cach.forecast.persistence.entities.ProviderInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderInvoiceRepository extends JpaRepository<ProviderInvoice, Long> {
	List<ProviderInvoice> findByProvider(Provider provider);
	List<ProviderInvoice>findAllByProviderAndInvoiceStatus(Provider provider, InvoiceStatus invoiceStatus);
	List<ProviderInvoice>findAllByOrderByInvoiceDateDesc();
	List<ProviderInvoice> findByInvoiceStatusAndInvoiceDeadlineDateBetween(InvoiceStatus invoiceStatus, Date startDate, Date endDate);
	List<ProviderInvoice> findByInvoiceStatusAndInvoiceDeadlineDateBefore(InvoiceStatus invoiceStatus, Date startDate);

}
