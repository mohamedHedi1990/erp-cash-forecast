package org.apac.erp.cach.forecast.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.CustomerInvoiceDTO;
import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerInvoiceRepository;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerInvoiceService {

	@Autowired
	private CustomerInvoiceRepository customerInvoiceRepo;
	
	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private InvoiceService invoiceService;

	public List<CustomerInvoice> findAllCustomerInvoices() {
		return customerInvoiceRepo.findAll();
	}

	public List<CustomerInvoiceDTO> findAllCustomerInvoicesDTO() {

		List<CustomerInvoice> invoices = customerInvoiceRepo.findAll();
		List<CustomerInvoiceDTO> dtos = new ArrayList<>();
		invoices.stream().forEach(invoice -> {
			CustomerInvoiceDTO dto = new CustomerInvoiceDTO(invoice.getInvoiceId(), invoice.getInvoiceNumber(),
					invoice.getInvoiceDeadlineInNumberOfDays(), invoice.getInvoiceDeadlineDate(),
					invoice.getInvoiceDate(), invoice.getInvoiceTotalAmount(), invoice.getInvoiceRs(),invoice.getInvoiceRsType(),
					invoice.getInvoiceNet(), invoice.getInvoicePayment(), invoice.getCustomer(),
					invoice.getCustomer().getCustomerLabel(), invoice.getCreatedAt(), invoice.getUpdatedAt(),
					invoice.getInvoiceStatus());

			dtos.add(dto);
		});
		return dtos;
	}

	public CustomerInvoice saveNewCustomerInvoice(CustomerInvoice invoice, Long customerId) {
		Customer customer = customerService.findCustomerById(customerId);
		invoice.setCustomer(customer);
		invoice.setInvoiceStatus(InvoiceStatus.OPENED);
		invoice.setInvoiceTotalAmount(invoice.getInvoiceNet() + invoice.getInvoiceRs());

		try {
			long days = invoiceService.betweenDates(invoice.getInvoiceDate(), invoice.getInvoiceDeadlineDate());
			invoice.setInvoiceDeadlineInNumberOfDays((int) days);
		} catch (IOException e) {
			e.printStackTrace();
		}
		CustomerInvoice savedInvoice = customerInvoiceRepo.save(invoice);
		customer.getCustomerInvoices().add(savedInvoice);
		
		customerService.updateCustomer(customer);
		return savedInvoice;
	}

	public Invoice findCustomerInvoiceById(Long invoiceId) {
		return customerInvoiceRepo.findOne(invoiceId);
	}

	public void deleteInvoice(Long customerId) {
		customerInvoiceRepo.delete(customerId);
	}
	
	public List<CustomerInvoice> findInvoicesOfGivenCustomer(Customer customer) {
		return customerInvoiceRepo.findByCustomer(customer);
		
	}

	public HashMap<Customer, List<CustomerInvoice>> findAllInvoicesByCustomer() {
		
		HashMap<Customer, List<CustomerInvoice>> map = new HashMap<Customer, List<CustomerInvoice>>();
		List<Customer> customers = customerRepo.findAll();
		customers.stream().forEach(customer -> {
			map.put(customer, customerInvoiceRepo.findByCustomer(customer));
		});
		
		return map;
	}
}
