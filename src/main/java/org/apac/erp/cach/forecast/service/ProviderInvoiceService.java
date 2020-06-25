package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.persistence.entities.ProviderInvoice;
import org.apac.erp.cach.forecast.persistence.repositories.ProviderInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderInvoiceService {

	@Autowired
	private ProviderInvoiceRepository providerInvoiceRepo;

	public List<ProviderInvoice> findAllProviderInvoices() {
		return providerInvoiceRepo.findAll();
	}

	public ProviderInvoice saveNewProviderInvoice(ProviderInvoice invoice) {
		return providerInvoiceRepo.save(invoice);
	}

	public Invoice findProviderInvoiceById(Long invoiceId) {
		return providerInvoiceRepo.findOne(invoiceId);
	}

}
