package org.apac.erp.cach.forecast.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.ProviderInvoiceDTO;
import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;
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
		return  providerInvoiceRepo.findAll();
	
	}
	
	public List<ProviderInvoiceDTO> findAllProviderInvoicesDTO() {
		List<ProviderInvoice> invoices = providerInvoiceRepo.findAll();
		List<ProviderInvoiceDTO> dtos = new ArrayList<>();
		invoices.stream().forEach(invoice -> {
			ProviderInvoiceDTO dto = new ProviderInvoiceDTO(invoice.getInvoiceId(), invoice.getInvoiceNumber(),
					invoice.getInvoiceDeadlineInNumberOfDays(), invoice.getInvoiceDeadlineDate(),
					invoice.getInvoiceDate(), invoice.getInvoiceTotalAmount(), invoice.getInvoiceRs(),
					invoice.getInvoiceNet(), invoice.getInvoicePayment(), invoice.getProvider().getProviderLabel(),
					invoice.getCreatedAt(), invoice.getUpdatedAt(), invoice.getInvoiceStatus());

			dtos.add(dto);
		});
		return dtos;
	}

	public ProviderInvoice saveNewProviderInvoice(ProviderInvoice invoice, Long providerId) {
		invoice.setProvider(providerService.findProviderById(providerId));
		invoice.setInvoiceStatus(InvoiceStatus.OPENED);
		invoice.setInvoiceTotalAmount(invoice.getInvoiceNet() + invoice.getInvoiceRs());
		try {
			long days = invoiceService.betweenDates(invoice.getInvoiceDate(), invoice.getInvoiceDeadlineDate());
			invoice.setInvoiceDeadlineInNumberOfDays((int) days);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return providerInvoiceRepo.save(invoice);
	}

	public Invoice findProviderInvoiceById(Long invoiceId) {
		return providerInvoiceRepo.findOne(invoiceId);
	}

	public void deleteInvoice(Long invoiceId) {
		providerInvoiceRepo.delete(invoiceId);
	}

}
