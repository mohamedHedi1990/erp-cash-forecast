package org.apac.erp.cach.forecast.service;

import java.io.IOException;
import java.util.List;

import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.persistence.entities.Provider;
import org.apac.erp.cach.forecast.persistence.entities.ProviderInvoice;
import org.apac.erp.cach.forecast.persistence.repositories.ProviderInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderInvoiceService {
	@Autowired
	private ProviderInvoiceRepository providerInvoiceRepo;

	@Autowired
	private ProviderService providerService;

	@Autowired
	private InvoiceService invoiceService;

	public List<ProviderInvoice> findAllProviderInvoices() {
		return providerInvoiceRepo.findAll();
	}

	public ProviderInvoice saveProviderInvoice(ProviderInvoice invoice, Long providerId) {
		Provider provider = providerService.getProviderById(providerId);
		invoice.setProvider(provider);
		invoice.setInvoiceStatus(InvoiceStatus.OPENED);
		invoice.setInvoiceTotalAmount(invoice.getInvoiceNet() + invoice.getInvoiceRs());

		try {
			long days = invoiceService.betweenDates(invoice.getInvoiceDate(), invoice.getInvoiceDeadlineDate());
			invoice.setInvoiceDeadlineInNumberOfDays((int) days);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ProviderInvoice savedInvoice = providerInvoiceRepo.save(invoice);

		return savedInvoice;
	}
}
