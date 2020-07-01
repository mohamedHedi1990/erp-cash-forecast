package org.apac.erp.cach.forecast.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProviderInvoiceDTO {
	private Long invoiceId;

	private Integer invoiceNumber;

	private Integer invoiceDeadlineInNumberOfDays;

	private Date invoiceDeadlineDate;

	private Date invoiceDate;

	private Double invoiceTotalAmount;

	private Double invoiceRs;

	private Double invoiceNet;

	private Double invoicePayment;

	private String providerLabel;

	protected Date createdAt;

	protected Date updatedAt;

}
