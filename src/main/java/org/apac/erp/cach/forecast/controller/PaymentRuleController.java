package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.service.InvoiceService;
import org.apac.erp.cach.forecast.service.PaymentRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paymentRule")
public class PaymentRuleController {

	@Autowired
	private PaymentRuleService paymentRuleService;
	@Autowired
	private InvoiceService invoiceService;

	@CrossOrigin
	@PutMapping("/{paymentRuleId}")
	public PaymentRule validatePaymentRule(@PathVariable("paymentRuleId") Long paymentRuleId) {
		return paymentRuleService.validatePaymentRule(paymentRuleId);
	}
	
	@CrossOrigin
	@DeleteMapping("/{paymentRuleId}")
	public void deletePaymentRule(@PathVariable("paymentRuleId") Long paymentRuleId) {
		PaymentRule paymentRuleToDelete=paymentRuleService.findPaymentRuleBYId(paymentRuleId);
		Invoice invoiceToUpdate=invoiceService.findInvoiceById(paymentRuleToDelete.getInvoice().getInvoiceId());
		Double invoiceOlder=invoiceToUpdate.getInvoicePayment();
		Double paymentRuleDelete=paymentRuleToDelete.getPaymentRuleAmount();
		Double newInvoicePayment=invoiceOlder-paymentRuleDelete;
		paymentRuleService.deletePaymentRule(paymentRuleId);
		System.out.println("invoice older:"+invoiceOlder);
		System.out.println("payment rule delete"+paymentRuleDelete);
		System.out.println("montant invoice new "+newInvoicePayment);
		invoiceToUpdate.setInvoicePayment(newInvoicePayment);
       invoiceService.saveInvoice(invoiceToUpdate);
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
