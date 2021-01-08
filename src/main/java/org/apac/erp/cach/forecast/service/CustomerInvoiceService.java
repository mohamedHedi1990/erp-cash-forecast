package org.apac.erp.cach.forecast.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apac.erp.cach.forecast.dtos.InvoicesCustomerPayment;
import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.entities.CustomerAttachedInvoices;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerAttachedInvoicesRepository;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerInvoiceRepository;
import org.apac.erp.cach.forecast.persistence.repositories.PaymentRuleRepository;
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
	@Autowired
	private PaymentRuleRepository paymentRuleRepository;

	@Autowired
	private CustomerAttachedInvoicesRepository customerAttachedRepo;

	@Autowired
	private CustomerAttachedInvoicesRepository customerAttachedInvoicesRepository;

	public List<CustomerInvoice> findAllCustomerInvoices() {

		return customerInvoiceRepo.findAllByOrderByInvoiceNumberDesc();
	}

	public List<CustomerInvoice> findAllByCustomerInvoices(Customer customer) {
		return customerInvoiceRepo.findByCustomer(customer);
	}

	public CustomerInvoice saveCustomerInvoice(CustomerInvoice invoice) {
		// Customer customer = customerService.getCustomerById(customerId);
		// invoice.setCustomer(customer);
		invoice.setInvoiceStatus(InvoiceStatus.OPENED);
		// invoice.setInvoiceTotalAmount(invoice.getInvoiceNet() +
		// invoice.getInvoiceRs());
		CustomerInvoice customerInvoice = invoice;
		try {
			long days = invoiceService.betweenDates(invoice.getInvoiceDate(), invoice.getInvoiceDeadlineDate());
			invoice.setInvoiceDeadlineInNumberOfDays((int) days);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (invoice.getInvoiceId() != null) {

			if (invoice.getInvoiceTotalAmount().compareTo(invoice.getInvoicePayment()) == 0) {
				invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
			}
		}

		CustomerInvoice savedInvoice = customerInvoiceRepo.save(invoice);
		if (customerInvoice.getInvoiceId() != null) {
			if (customerInvoice.getInvoicePaymentRules() != null) {
				customerInvoice.getInvoicePaymentRules().forEach(rule -> {
					rule.setInvoice(savedInvoice);
					paymentRuleRepository.save(rule);
				});
			}
		}
		return savedInvoice;
	}

	/*
	 * public List<CustomerInvoice> payInvoices(InvoicesCustomerPayment
	 * invoicePayment) {
	 * invoicePayment.getSelectedInvoices().stream().forEach(invoice -> {
	 * List<PaymentRule> paymentRules = invoice.getInvoicePaymentRules();
	 * if(paymentRules == null) { paymentRules = new ArrayList<PaymentRule>(); }
	 * if(invoicePayment.getPaymentRule().getPaymentRuleInvoices() == null) {
	 * invoicePayment.getPaymentRule().setPaymentRuleInvoices(""+invoice.
	 * getInvoiceId()); } else {
	 * invoicePayment.getPaymentRule().setPaymentRuleInvoices(invoicePayment.
	 * getPaymentRule().getPaymentRuleInvoices() + ","+invoice.getInvoiceId());
	 * } paymentRules.add(invoicePayment.getPaymentRule());
	 * invoice.setInvoicePayment(invoice.getInvoiceTotalAmount());
	 * invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
	 * invoice.setInvoicePaymentRules(paymentRules); });
	 * 
	 * return
	 * this.customerInvoiceRepo.save(invoicePayment.getSelectedInvoices()); }
	 */

	public CustomerAttachedInvoices payInvoices(InvoicesCustomerPayment invoicePayment) {

		String externalId = "";
		invoicePayment.setSelectedInvoices(invoicePayment.getSelectedInvoices().stream()
				.sorted(Comparator.comparing(CustomerInvoice::getInvoiceId)).collect(Collectors.toList()));
		for (CustomerInvoice invoice : invoicePayment.getSelectedInvoices()) {
			if (externalId.isEmpty()) {
				externalId = "" + invoice.getInvoiceId();
			} else {
				externalId = externalId + "." + invoice.getInvoiceId();
			}
		}

		CustomerAttachedInvoices attachedInvoices = this.customerAttachedInvoicesRepository
				.findByExternalId(externalId);

		if (attachedInvoices == null) {
			attachedInvoices = new CustomerAttachedInvoices();
			attachedInvoices.setInvoices(new ArrayList<>());
			attachedInvoices.setPaymentRules(new ArrayList<>());

			for (CustomerInvoice invoice : invoicePayment.getSelectedInvoices()) {

				attachedInvoices.getInvoices().add(invoice);
				attachedInvoices.setTotalRequiredAmount(
						attachedInvoices.getTotalRequiredAmount() + invoice.getInvoiceTotalAmount());

			}
			attachedInvoices.setExternalId(externalId);

		}

		for (CustomerInvoice invoice : invoicePayment.getSelectedInvoices()) {

			List<PaymentRule> paymentRules = invoice.getInvoicePaymentRules();
			if (paymentRules != null) {
				for (PaymentRule paymentRule : paymentRules) {
					attachedInvoices.setTotalPaidAmount(
							attachedInvoices.getTotalPaidAmount() + paymentRule.getPaymentRuleAmount());
				}

			}

		}
		attachedInvoices.setTotalPaidAmount(
				attachedInvoices.getTotalPaidAmount() + invoicePayment.getPaymentRule().getPaymentRuleAmount());
		if (attachedInvoices.getTotalPaidAmount() == attachedInvoices.getTotalRequiredAmount()) {
			attachedInvoices.getInvoices().stream().forEach(invoice -> {
				invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
				invoice.setInvoicePayment(invoice.getInvoiceTotalAmount());
				this.invoiceService.saveInvoice(invoice);
			});
		}
		attachedInvoices.getPaymentRules().add(invoicePayment.getPaymentRule());
		return this.customerAttachedInvoicesRepository.save(attachedInvoices);
	}

	public CustomerInvoice getCustomerInvoiceById(Long invoiceId) {
		return this.customerInvoiceRepo.findOne(invoiceId);
	}

	public List<CustomerInvoice> findAllCustomerInvoicesByCustomerId(Long customerId) {
		Customer customer = customerService.getCustomerById(customerId);
		if (customer != null) {
			return this.customerInvoiceRepo.findByCustomer(customer);
		} else
			return null;
	}

	public List<CustomerInvoice> findAllCustomerInvoicesByCustomerIdAndIsOpened(Long customerId) {
		Customer customer = customerService.getCustomerById(customerId);
		if (customer != null) {
			return this.customerInvoiceRepo.findAllByCustomerAndInvoiceStatus(customer, InvoiceStatus.OPENED);
		} else
			return null;
	}

	private List<CustomerInvoice> findAllCustomerInvoicesAttached_private() {
		List<CustomerAttachedInvoices> customerAttachedInvoices = this.customerAttachedInvoicesRepository.findAll();
		List<CustomerInvoice> customerInvoices = new ArrayList<CustomerInvoice>();
		customerAttachedInvoices.forEach(attachedInvoice -> {
			CustomerInvoice invoice = new CustomerInvoice();
			invoice.setInvoiceNumber("");
			String invoiceNum = "";
			String invoiceDates = "";
			String invoiceDeadlineDates = "";
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			for (CustomerInvoice customerInvoice : attachedInvoice.getInvoices()) {
				if (invoiceNum.isEmpty()) {
					invoiceNum = customerInvoice.getInvoiceNumber();
					invoiceDates = formatter.format(customerInvoice.getInvoiceDate());
					invoiceDeadlineDates = formatter.format(customerInvoice.getInvoiceDeadlineDate());
				} else {
					invoiceNum = invoiceNum + " / " + customerInvoice.getInvoiceNumber();
					invoiceDates = invoiceDates + " / " + formatter.format(customerInvoice.getInvoiceDate());
					invoiceDeadlineDates = invoiceDeadlineDates + " / "
							+ formatter.format(customerInvoice.getInvoiceDeadlineDate());

				}
				invoice.setCustomer(customerInvoice.getCustomer());
				invoice.setInvoiceDate(customerInvoice.getInvoiceDate());
				invoice.setInvoiceDeadlineDate(customerInvoice.getInvoiceDeadlineDate());
			}
			invoice.setInvoiceNumber(invoiceNum);
			invoice.setAssocited(true);
			invoice.setInvoiceStatus(attachedInvoice.getInvoices().get(0).getInvoiceStatus());
			invoice.setInvoiceTotalAmount(attachedInvoice.getTotalRequiredAmount());
			invoice.setInvoicePayment(attachedInvoice.getTotalPaidAmount());
			invoice.setInvoiceTotalAmountS(attachedInvoice.getTotalRequiredAmountS());
			invoice.setInvoicePaymentS(attachedInvoice.getTotalPaidAmountS());
			invoice.setInvoiceCurrency(attachedInvoice.getInvoices().get(0).getInvoiceCurrency());
			invoice.setInvoicePaymentRules(attachedInvoice.getPaymentRules());
			customerInvoices.add(invoice);
			invoice.setInvoiceDates(invoiceDates);
			int rand = (int)(Math.random() * (10000 - 8050)) + 8050;
			invoice.setInvoiceId((long) rand);
			invoice.setInvoiceDeadlineDates(invoiceDeadlineDates);
		});
		return customerInvoices;
	}

	public List<CustomerInvoice> findAllCustomerInvoicesAttached() {
		List<CustomerInvoice> customerInvoices = this.findAllCustomerInvoices();
		List<CustomerInvoice> attachedCustomerInvoices = this.findAllCustomerInvoicesAttached_private();

		List<CustomerInvoice> allInvoices = new ArrayList<>();
		allInvoices.addAll(customerInvoices);
		allInvoices.addAll(attachedCustomerInvoices);

		allInvoices = allInvoices.stream().sorted(Comparator.comparing(CustomerInvoice::getInvoiceNumber))
				.collect(Collectors.toList());
		return allInvoices;
	}

}
