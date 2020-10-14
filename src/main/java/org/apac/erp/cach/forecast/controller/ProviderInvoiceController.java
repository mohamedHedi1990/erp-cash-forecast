package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.ProviderInvoice;
import org.apac.erp.cach.forecast.service.ProviderInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apac.erp.cach.forecast.dtos.InvoicesProviderPayment;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;

@RestController
@RequestMapping("/api/invoice-provider")
public class ProviderInvoiceController {
	
	@Autowired
	private ProviderInvoiceService providerInvoiceService;

	@CrossOrigin
	@GetMapping()
	public List<ProviderInvoice> findAllProviderInvoices() {
		return providerInvoiceService.findAllProviderInvoices();
	}
	
	@CrossOrigin
	@GetMapping("by-provider-id/{providerId}")
	public List<ProviderInvoice> findAllProviderInvoicesByProviderId(@PathVariable("providerId") Long providerId) {
		return providerInvoiceService.findAllProviderInvoicesByProviderId(providerId);
	}

	@CrossOrigin
	@PostMapping()
	public ProviderInvoice saveNewProviderInvoice(@RequestBody ProviderInvoice invoice) {
		return providerInvoiceService.saveProviderInvoice(invoice);
	}
	
	@CrossOrigin
	@PostMapping("/pay")
	public List<ProviderInvoice>  payInvoices(
			@RequestBody InvoicesProviderPayment invoicePayment) {
		return providerInvoiceService.payInvoices(invoicePayment);
	}

}
