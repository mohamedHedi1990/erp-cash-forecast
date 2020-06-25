package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Invoice;
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

@RestController
@RequestMapping("/api/provider/invoice")
public class ProviderInvoiceController {
	
	@Autowired
	private ProviderInvoiceService providerInvoiceService;

	@CrossOrigin
	@GetMapping()
	public List<ProviderInvoice> findAllProviderInvoices() {
		return providerInvoiceService.findAllProviderInvoices();
	}

	@CrossOrigin
	@PostMapping()
	public ProviderInvoice saveNewProviderInvoice(@RequestBody ProviderInvoice invoice) {
		return providerInvoiceService.saveNewProviderInvoice(invoice);
	}
	
	@CrossOrigin
	@GetMapping("by-invoice-id/{invoiceId}")
	public Invoice findInvoiceById(@PathVariable("invoiceId") Long invoiceId) {
		return providerInvoiceService.findProviderInvoiceById(invoiceId);
	}

}
