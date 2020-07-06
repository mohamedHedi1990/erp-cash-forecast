package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.dtos.PaymentRuleDTO;
import org.apac.erp.cach.forecast.enumeration.PaymentMethod;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.service.PaymentRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/paymentRule")
public class PaymentRuleController {

	@Autowired
	private PaymentRuleService paymentRuleService;

	@CrossOrigin
	@GetMapping()
	public List<PaymentRule> findAllPaymentRules() {
		return paymentRuleService.findAllPaymentRules();
	}

	@CrossOrigin
	@GetMapping("/methods")
	public List<PaymentMethod> findAllPaymentMethods() {
		return paymentRuleService.findAllPaymentMethods();
	}

	@CrossOrigin
	@GetMapping("/customerInvoices")
	public List<PaymentRuleDTO> findAllCustomerInvoicesPaymentRules() {
		return paymentRuleService.findAllCustomerInvoicesPaymentRules();
	}

	@CrossOrigin
	@GetMapping("/providerInvoices")
	public List<PaymentRuleDTO> findAllProviderInvoicesPaymentRules() {
		return paymentRuleService.findAllProviderInvoicesPaymentRules();
	}

	@CrossOrigin
	@PostMapping("/invoice/{invoiceId}/account/{accountId}")
	public PaymentRule saveNewPaymentRuleToCustomerInvoice(@RequestBody PaymentRule paymentRule,
			@PathVariable("invoiceId") Long invoiceId,
			@PathVariable("accountId") Long accountId) {
		return paymentRuleService.saveNewPaymentRuleToInvoice(paymentRule, invoiceId, accountId);
	}

}
