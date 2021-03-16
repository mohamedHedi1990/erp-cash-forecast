package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.dtos.InvoicesCustomerPayment;
import org.apac.erp.cach.forecast.persistence.entities.CustomerAttachedInvoices;
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
	@GetMapping("/with-attached-invoices")
	public List<CustomerInvoice> findAllCustomerInvoicesAttached() {

		return customerInvoiceService.findAllCustomerInvoicesAttached();
	}

	@CrossOrigin
	@PostMapping()
	public CustomerInvoice saveNewCustomerInvoice(@RequestBody CustomerInvoice invoice) {
		return customerInvoiceService.saveCustomerInvoice(invoice);
	}
	
	@CrossOrigin
	@PostMapping("/pay")
	public CustomerAttachedInvoices  payInvoices(
			@RequestBody InvoicesCustomerPayment invoicePayment) {
		return customerInvoiceService.payInvoices(invoicePayment);
	}
	
	@CrossOrigin
	@GetMapping("by-customer-id/{customerId}")
	public List<CustomerInvoice> findAllCustomerInvoicesByCustomerId(@PathVariable("customerId") Long customerId) {
		return customerInvoiceService.findAllCustomerInvoicesByCustomerId(customerId);
	}
	@CrossOrigin
	@GetMapping("by-customer-id-and-opened-invoice/{customerId}")
	public List<CustomerInvoice> findAllCustomerInvoicesByCustomerIdAndIsOpened(@PathVariable("customerId") Long customerId) {
		return customerInvoiceService.findAllCustomerInvoicesByCustomerIdAndIsOpened(customerId);
	}
    @CrossOrigin
	@GetMapping("/invoiceIfExist/{customerInvoiceNumber}")
	public Boolean findInvoiceCustomerIfExists(@PathVariable("customerInvoiceNumber") String customerInvoiceNumber)
	{
		return customerInvoiceService.findInvoiceCustomerIfExists(customerInvoiceNumber);
	}


}
