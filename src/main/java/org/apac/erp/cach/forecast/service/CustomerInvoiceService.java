package org.apac.erp.cach.forecast.service;

import java.io.IOException;
import java.util.List;

import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerInvoiceService {

	@Autowired
	private CustomerInvoiceRepository customerInvoiceRepo;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private InvoiceService invoiceService;

	public List<CustomerInvoice> findAllCustomerInvoices() {
		return customerInvoiceRepo.findAll();
	}

	public CustomerInvoice saveCustomerInvoice(CustomerInvoice invoice) {
		// Customer customer = customerService.getCustomerById(customerId);
		// invoice.setCustomer(customer);
		invoice.setInvoiceStatus(InvoiceStatus.OPENED);
		// invoice.setInvoiceTotalAmount(invoice.getInvoiceNet() + invoice.getInvoiceRs());

		try {
			long days = invoiceService.betweenDates(invoice.getInvoiceDate(), invoice.getInvoiceDeadlineDate());
			invoice.setInvoiceDeadlineInNumberOfDays((int) days);
		} catch (IOException e) {
			e.printStackTrace();
		}
		CustomerInvoice savedInvoice = customerInvoiceRepo.save(invoice);

		return savedInvoice;
	}

}
