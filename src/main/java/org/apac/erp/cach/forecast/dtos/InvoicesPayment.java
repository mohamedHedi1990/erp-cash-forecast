package org.apac.erp.cach.forecast.dtos;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvoicesPayment {
	
	private List<Invoice> selectedInvoices;
	
	private PaymentRule paymentRule;

}
