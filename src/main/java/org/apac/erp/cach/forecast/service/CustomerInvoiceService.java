package org.apac.erp.cach.forecast.service;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apac.erp.cach.forecast.dtos.InvoicesCustomerPayment;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.persistence.entities.Provider;
import org.apac.erp.cach.forecast.persistence.entities.ProviderInvoice;

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
	
	public List<CustomerInvoice> payInvoices(InvoicesCustomerPayment invoicePayment) {
				invoicePayment.getSelectedInvoices().stream().forEach(invoice -> {
			List<PaymentRule> paymentRules = invoice.getInvoicePaymentRules();
			if(paymentRules == null) {
				paymentRules = new ArrayList<PaymentRule>();
			}
			if(invoicePayment.getPaymentRule().getPaymentRuleInvoices() == null) {
				invoicePayment.getPaymentRule().setPaymentRuleInvoices(""+invoice.getInvoiceId());
			} else {
				invoicePayment.getPaymentRule().setPaymentRuleInvoices(invoicePayment.getPaymentRule().getPaymentRuleInvoices() + ","+invoice.getInvoiceId());
			}
			paymentRules.add(invoicePayment.getPaymentRule());
			invoice.setInvoicePayment(invoice.getInvoiceTotalAmount());
			invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
			invoice.setInvoicePaymentRules(paymentRules);
		});

		return this.customerInvoiceRepo.save(invoicePayment.getSelectedInvoices());
	}
	
	public CustomerInvoice getCustomerInvoiceById(Long invoiceId) {
		return this.customerInvoiceRepo.findOne(invoiceId);
	}

	public List<CustomerInvoice> findAllCustomerInvoicesByCustomerId(Long customerId) {
		Customer customer = customerService.getCustomerById(customerId);
		if(customer != null) {
			return this.customerInvoiceRepo.findByCustomer(customer);
		}
		else return null;
	}
	

}
