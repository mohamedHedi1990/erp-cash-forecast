package org.apac.erp.cach.forecast.dtos;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvoicesCustomerPayment {
	
	private List<CustomerInvoice> selectedInvoices;
	
	private PaymentRule paymentRule;

}
