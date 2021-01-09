package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.persistence.entities.CustomerAttachedInvoices;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerAttachedInvoicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerAttachedInvoicesService {

	@Autowired
	private CustomerAttachedInvoicesRepository customerAttachedInvoicesRepo;
	
	@Autowired
	private InvoiceService invoiceService;
	
	public void deleteAttachedInvoices(Long customerAttachedInvoicesId) {
		 CustomerAttachedInvoices  customerAttachedInvoices = this.customerAttachedInvoicesRepo.findOne(customerAttachedInvoicesId);
		 if(customerAttachedInvoices != null) {
			 for(CustomerInvoice invoice: customerAttachedInvoices.getInvoices()) {
				 if(invoice.getInvoiceStatus() == InvoiceStatus.CLOSED) {
					 invoice.setInvoicePayment(invoice.getInvoicePaymentRules().stream().mapToDouble(paymentRule -> paymentRule.getPaymentRuleAmount()).sum());
					 invoice.setInvoiceStatus(InvoiceStatus.OPENED);
					 invoice.setAssociationNumber(invoice.getAssociationNumber() - 1);
					 this.invoiceService.saveInvoice(invoice);
				 }
			 }
			 this.customerAttachedInvoicesRepo.delete(customerAttachedInvoicesId);
		 }
	}
}
