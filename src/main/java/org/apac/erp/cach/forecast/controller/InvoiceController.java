package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.enumeration.OperationDtoType;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

	@Autowired
	private InvoiceService invoiceService;

/*
	@CrossOrigin
	@GetMapping()
	public List<Invoice> findAllInvoices() {
		return invoiceService.findAllInvoices();
	}
	*/
/*
	@CrossOrigin
	@PostMapping()
	public Invoice saveNewInvoice(@RequestBody Invoice invoice) {
		return invoiceService.saveNewInvoice(invoice);
	}*/

	@CrossOrigin
	@GetMapping("/{invoiceId}")
	public Invoice findInvoiceById(@PathVariable("invoiceId") Long invoiceId) {
		return invoiceService.findInvoiceById(invoiceId);
	}

	@CrossOrigin
	@DeleteMapping("/{invoiceId}")
	public void deleteInvoice(@PathVariable("invoiceId") Long invoiceId) {
		 invoiceService.deleteInvoice(invoiceId);
	}
	
	@CrossOrigin
	@PutMapping("/{invoiceId}")
	public void closeInvoice(@PathVariable("invoiceId") Long invoiceId) {
		 invoiceService.closeInvoice(invoiceId);
	}
	
	@CrossOrigin
	@PostMapping("/{invoiceId}")
	public Invoice addPaymentRuleForInvoice(@PathVariable("invoiceId") Long invoiceId,
			@RequestParam("operationType") OperationDtoType operationType,
			@RequestBody PaymentRule paymentRule) {
		return invoiceService.addPaymentRuleForInvoice(invoiceId, paymentRule, operationType);
	}
	@CrossOrigin
	@PostMapping("updatePaymentRule/{invoiceId}/{context}")
	public Invoice updatePaymentRuleForInvoice(@PathVariable("invoiceId") Long invoiceId,
			@PathVariable("context") String context,
											@RequestBody PaymentRule paymentRule) {
		return invoiceService.updatePaymentRule(invoiceId, paymentRule, context);
	}
	
}
