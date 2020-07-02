package org.apac.erp.cach.forecast.service;

import java.util.ArrayList;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.PaymentRuleDTO;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
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

	public List<PaymentRule> findAllPaymentRules() {
		return paymentRuleRepo.findAll();
	}

	public PaymentRule saveNewPaymentRuleToCustomerInvoice(PaymentRule paymentRule, Long invoiceId) {
		paymentRule.setInvoice(customerInvoiceService.findCustomerInvoiceById(invoiceId));
		return paymentRuleRepo.save(paymentRule);
	}

	public PaymentRule saveNewPaymentRuleToProviderInvoice(PaymentRule paymentRule, Long invoiceId) {
		paymentRule.setInvoice(providerInvoiceService.findProviderInvoiceById(invoiceId));
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
