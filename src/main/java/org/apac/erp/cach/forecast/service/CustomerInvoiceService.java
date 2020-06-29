package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerInvoiceService {

	@Autowired
	private CustomerInvoiceRepository customerInvoiceRepo;
	
	@Autowired
	private CustomerService customerService;

	public List<CustomerInvoice> findAllCustomerInvoices() {
		return customerInvoiceRepo.findAll();
	}

	public CustomerInvoice saveNewCustomerInvoice(CustomerInvoice invoice, Long customerId) {
		invoice.setCustomer(customerService.findCustomerById(customerId));
		return customerInvoiceRepo.save(invoice);
	}

	public Invoice findCustomerInvoiceById(Long invoiceId) {
		return customerInvoiceRepo.findOne(invoiceId);
	}
}
