package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.persistence.entities.ProviderAttachedInvoices;
import org.apac.erp.cach.forecast.persistence.entities.ProviderInvoice;
import org.apac.erp.cach.forecast.persistence.repositories.ProviderAttachedInvoicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderAttachedInvoicesService {

	@Autowired
	private ProviderAttachedInvoicesRepository ProviderAttachedInvoicesRepo;
	
	@Autowired
	private InvoiceService invoiceService;
	
	public void deleteAttachedInvoices(Long ProviderAttachedInvoicesId) {
		 ProviderAttachedInvoices  ProviderAttachedInvoices = this.ProviderAttachedInvoicesRepo.findOne(ProviderAttachedInvoicesId);
		 if(ProviderAttachedInvoices != null) {
			 for(ProviderInvoice invoice: ProviderAttachedInvoices.getInvoices()) {
				 if(invoice.getInvoiceStatus() == InvoiceStatus.CLOSED) {
					 invoice.setInvoicePayment(invoice.getInvoicePaymentRules().stream().mapToDouble(paymentRule -> paymentRule.getPaymentRuleAmount()).sum());
					 invoice.setInvoiceStatus(InvoiceStatus.OPENED);
					 invoice.setAssociationNumber(invoice.getAssociationNumber() - 1);
					 this.invoiceService.saveInvoice(invoice);
				 }
			 }
			 this.ProviderAttachedInvoicesRepo.delete(ProviderAttachedInvoicesId);
		 }
	}
}
