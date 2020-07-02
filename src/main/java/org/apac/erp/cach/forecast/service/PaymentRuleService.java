package org.apac.erp.cach.forecast.service;

import java.util.ArrayList;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.PaymentRuleDTO;
import org.apac.erp.cach.forecast.enumeration.InvoiceType;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.persistence.entities.ProviderInvoice;
import org.apac.erp.cach.forecast.persistence.repositories.PaymentRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentRuleService {

	@Autowired
	private PaymentRuleRepository paymentRuleRepo;

	@Autowired
	private CustomerInvoiceService customerInvoiceService;

	@Autowired
	private ProviderInvoiceService providerInvoiceService;

	@Autowired
	private InvoiceService invoiceService;

	public List<PaymentRule> findAllPaymentRules() {
		return paymentRuleRepo.findAll();
	}

	public PaymentRule saveNewPaymentRuleToCustomerInvoice(PaymentRule paymentRule, Long invoiceId) {
		CustomerInvoice invoice = (CustomerInvoice) customerInvoiceService.findCustomerInvoiceById(invoiceId);
		paymentRule.setInvoice(invoice);
		// TODO
		invoiceService.updateInvoiceWithPaymentRule(invoice, InvoiceType.CUSTOMER,paymentRule);
		return paymentRuleRepo.save(paymentRule);
	}

	public PaymentRule saveNewPaymentRuleToProviderInvoice(PaymentRule paymentRule, Long invoiceId) {
		Invoice invoice = providerInvoiceService.findProviderInvoiceById(invoiceId);
		paymentRule.setInvoice(invoice);
		// TODO
		invoiceService.updateInvoiceWithPaymentRule(invoice, InvoiceType.PROVIDER, paymentRule);
		return paymentRuleRepo.save(paymentRule);
	}

	public List<PaymentRuleDTO> findAllCustomerInvoicesPaymentRules() {
		ArrayList<PaymentRuleDTO> paymentRules = new ArrayList<>();
		List<CustomerInvoice> customerInvoices = customerInvoiceService.findAllCustomerInvoices();
		customerInvoices.stream().forEach(invoice -> {
			List<PaymentRule> invoicePaymentRules = invoice.getInvoicePaymentRules();
			invoicePaymentRules.stream().forEach(paymentRule -> {
				PaymentRuleDTO paymentRuleDTO = new PaymentRuleDTO(paymentRule.getPaymentRuleId(),
						paymentRule.getPaymentRulePaymentMethod(), paymentRule.getPaymentRulePaymentMethodNb(),
						paymentRule.getPaymentRuleDeadlineDate(), paymentRule.isValidated(),
						paymentRule.getInvoice().getInvoiceNumber(), paymentRule.getCreatedAt(),
						paymentRule.getUpdatedAt());
				paymentRules.add(paymentRuleDTO);
			});
		});
		return paymentRules;
	}

	public List<PaymentRuleDTO> findAllProviderInvoicesPaymentRules() {
		ArrayList<PaymentRuleDTO> paymentRules = new ArrayList<>();
		List<ProviderInvoice> providerInvoices = providerInvoiceService.findAllProviderInvoices();
		providerInvoices.stream().forEach(invoice -> {
			List<PaymentRule> invoicePaymentRules = invoice.getInvoicePaymentRules();
			invoicePaymentRules.stream().forEach(paymentRule -> {
				PaymentRuleDTO paymentRuleDTO = new PaymentRuleDTO(paymentRule.getPaymentRuleId(),
						paymentRule.getPaymentRulePaymentMethod(), paymentRule.getPaymentRulePaymentMethodNb(),
						paymentRule.getPaymentRuleDeadlineDate(), paymentRule.isValidated(),
						paymentRule.getInvoice().getInvoiceNumber(), paymentRule.getCreatedAt(),
						paymentRule.getUpdatedAt());
				paymentRules.add(paymentRuleDTO);
			});
		});
		return paymentRules;
	}

}
