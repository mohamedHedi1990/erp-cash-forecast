package org.apac.erp.cach.forecast.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.CustomerInvoiceDTO;
import org.apac.erp.cach.forecast.dtos.ProviderInvoiceDTO;
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

	public List<CustomerInvoiceDTO> findAllCustomerInvoices() {

		List<CustomerInvoice> invoices = customerInvoiceRepo.findAll();
		List<CustomerInvoiceDTO> dtos = new ArrayList<>();
		invoices.stream().forEach(invoice -> {
			CustomerInvoiceDTO dto = new CustomerInvoiceDTO(invoice.getInvoiceId(), invoice.getInvoiceNumber(),
					invoice.getInvoiceDeadlineInNumberOfDays(), invoice.getInvoiceDeadlineDate(),
					invoice.getInvoiceDate(), invoice.getInvoiceTotalAmount(), invoice.getInvoiceRs(),
					invoice.getInvoiceNet(), invoice.getInvoicePayment(), invoice.getCustomer().getCustomerLabel(),
					invoice.getCreatedAt(), invoice.getUpdatedAt());

			dtos.add(dto);
		});
		return dtos;
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
