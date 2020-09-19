package org.apac.erp.cach.forecast.controller;

import java.util.HashMap;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.CustomerInvoiceDTO;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.service.CustomerInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/invoice")
public class CustomerInvoiceController {
/*
	@Autowired
	private CustomerInvoiceService customerInvoiceService;

	@CrossOrigin
	@GetMapping()
	public List<CustomerInvoiceDTO> findAllCustomerInvoices() {
		return customerInvoiceService.findAllCustomerInvoicesDTO();
	}
	
	@CrossOrigin
	@GetMapping("/sorted")
	public HashMap<Customer, List<CustomerInvoice>> findAllInvoicesByCustomer() {
		return customerInvoiceService.findAllInvoicesByCustomer();
	}


	@CrossOrigin
	@PostMapping("{customerId}")
	public CustomerInvoice saveNewCustomerInvoice(@RequestBody CustomerInvoice invoice,
			@PathVariable("customerId") Long customerId) {
		return customerInvoiceService.saveNewCustomerInvoice(invoice, customerId);
	}

	@CrossOrigin
	@GetMapping("by-invoice-id/{invoiceId}")
	public Invoice findInvoiceById(@PathVariable("invoiceId") Long invoiceId) {
		return customerInvoiceService.findCustomerInvoiceById(invoiceId);
	}

	@CrossOrigin
	@DeleteMapping("/{customerId}")
	public void deleteInvoice(@PathVariable("customerId") Long customerId) {
		customerInvoiceService.deleteInvoice(customerId);
	}
*/
}
