package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.service.PaymentRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/paymentRule")
public class PaymentRuleController {

	@Autowired
	private PaymentRuleService paymentRuleService;

	@CrossOrigin
	@PutMapping("/{paymentRuleId}")
	public PaymentRule validatePaymentRule(@PathVariable("paymentRuleId") Long paymentRuleId) {
		return paymentRuleService.validatePaymentRule(paymentRuleId);
	}
	
	@CrossOrigin
	@DeleteMapping("/{paymentRuleId}")
	public void deletePaymentRule(@PathVariable("paymentRuleId") Long paymentRuleId) {
		 paymentRuleService.deletePaymentRule(paymentRuleId);
	}
/*
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
*/
}
