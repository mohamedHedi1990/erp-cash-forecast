package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.service.CustomerAttachedInvoicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer-attahced-invoices")
public class CustomerAttachedInvoices {
	
	@Autowired
	private CustomerAttachedInvoicesService customerAttachedInvoicesService;
	
	@CrossOrigin
	@DeleteMapping("/{id}")
	public void deleteCustomerAttachedInvoices(@PathVariable("id") Long id) {
		this.customerAttachedInvoicesService.deleteAttachedInvoices(id);
	}

}
