package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.enumeration.PaymentMethod;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.service.PaymentRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

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
	@PostMapping
	public PaymentRule savePaymentRule(@RequestBody PaymentRule paymentRule) {
		return this.paymentRuleService.savePaymentRule(paymentRule);
	}

	@CrossOrigin
	@GetMapping("/get-effect-rule/{startDate}/{endDate}/{paymentMethode}")
	public List<PaymentRule> getEffectRule(@PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, @PathVariable("paymentMethode") String paymentMethod) {
		return paymentRuleService.getEffectRules(startDate,endDate,paymentMethod);
	}

	@CrossOrigin
	@DeleteMapping("/{paymentRuleId}/{context}")
	public void deletePaymentRule(@PathVariable("paymentRuleId") Long paymentRuleId,
			@PathVariable("context") String context) {
		paymentRuleService.deletePaymentRuleNew(paymentRuleId, context);

	}

	/*
	 * @CrossOrigin
	 * 
	 * @GetMapping("/methods") public List<PaymentMethod>
	 * findAllPaymentMethods() { return
	 * paymentRuleService.findAllPaymentMethods(); }
	 * 
	 * @CrossOrigin
	 * 
	 * @GetMapping("/customerInvoices") public List<PaymentRuleDTO>
	 * findAllCustomerInvoicesPaymentRules() { return
	 * paymentRuleService.findAllCustomerInvoicesPaymentRules(); }
	 * 
	 * @CrossOrigin
	 * 
	 * @GetMapping("/providerInvoices") public List<PaymentRuleDTO>
	 * findAllProviderInvoicesPaymentRules() { return
	 * paymentRuleService.findAllProviderInvoicesPaymentRules(); }
	 * 
	 * @CrossOrigin
	 * 
	 * @PostMapping("/invoice/{invoiceId}/account/{accountId}") public
	 * PaymentRule saveNewPaymentRuleToCustomerInvoice(@RequestBody PaymentRule
	 * paymentRule,
	 * 
	 * @PathVariable("invoiceId") Long invoiceId,
	 * 
	 * @PathVariable("accountId") Long accountId) { return
	 * paymentRuleService.saveNewPaymentRuleToInvoice(paymentRule, invoiceId,
	 * accountId); }
	 */
}
