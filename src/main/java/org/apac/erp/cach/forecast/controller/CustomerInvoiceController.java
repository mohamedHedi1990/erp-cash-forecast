package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.service.CustomerInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apac.erp.cach.forecast.dtos.InvoicesCustomerPayment;


@RestController
@RequestMapping("/api/invoice-customer")
public class CustomerInvoiceController {

	@Autowired
	private CustomerInvoiceService customerInvoiceService;

	@CrossOrigin
	@GetMapping()
	public List<CustomerInvoice> findAllCustomerInvoices() {
		return customerInvoiceService.findAllCustomerInvoices();
	}

	@CrossOrigin
	@PostMapping()
	public CustomerInvoice saveNewCustomerInvoice(@RequestBody CustomerInvoice invoice) {
		return customerInvoiceService.saveCustomerInvoice(invoice);
	}
	
	@CrossOrigin
	@PostMapping("/pay")
	public List<Invoice>  payInvoices(
			@RequestBody InvoicesCustomerPayment invoicePayment) {
		return customerInvoiceService.payInvoices(invoicePayment);
	}

}
