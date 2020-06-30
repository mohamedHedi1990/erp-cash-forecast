package org.apac.erp.cach.forecast.service;

import java.io.IOException;
import java.util.List;

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
	@Autowired
	private InvoiceService invoiceService;

	public List<CustomerInvoice> findAllCustomerInvoices() {
		return customerInvoiceRepo.findAll();
	}

	public CustomerInvoice saveNewCustomerInvoice(CustomerInvoice invoice, Long customerId) {
		invoice.setCustomer(customerService.findCustomerById(customerId));
		invoice.setInvoiceTotalAmount(invoice.getInvoiceNet() + invoice.getInvoiceRs());

		try {
			long days = invoiceService.betweenDates(invoice.getInvoiceDate(), invoice.getInvoiceDeadlineDate());
			invoice.setInvoiceDeadlineInNumberOfDays((int) days);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return customerInvoiceRepo.save(invoice);
	}

	public Invoice findCustomerInvoiceById(Long invoiceId) {
		return customerInvoiceRepo.findOne(invoiceId);
	}

	public void deleteInvoice(Long customerId) {
		customerInvoiceRepo.delete(customerId);
	}
}
