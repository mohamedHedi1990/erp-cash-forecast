package org.apac.erp.cach.forecast.dtos;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.ProviderInvoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvoicesProviderPayment {
	
	private List<ProviderInvoice> selectedInvoices;
	
	private PaymentRule paymentRule;

	public InvoicesProviderPayment() {
		super();
	}
	
	

}
