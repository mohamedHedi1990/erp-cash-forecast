package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {
/*
	@Autowired
	private InvoiceService invoiceService;

	@CrossOrigin
	@GetMapping()
	public List<Invoice> findAllInvoices() {
		return invoiceService.findAllInvoices();
	}

	@CrossOrigin
	@PostMapping()
	public Invoice saveNewInvoice(@RequestBody Invoice invoice) {
		return invoiceService.saveNewInvoice(invoice);
	}

	@CrossOrigin
	@GetMapping("by-invoice-id/{invoiceId}")
	public Invoice findInvoiceById(@PathVariable("invoiceId") Long invoiceId) {
		return invoiceService.findInvoiceById(invoiceId);
	}
*/
}
