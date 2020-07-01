package org.apac.erp.cach.forecast.dtos;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProviderInvoiceDTO {
	private Long invoiceId;

	private Integer invoiceNumber;

	private Integer invoiceDeadlineInNumberOfDays;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	private Date invoiceDeadlineDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	private Date invoiceDate;

	private Double invoiceTotalAmount;

	private Double invoiceRs;

	private Double invoiceNet;

	private Double invoicePayment;

	private String providerLabel;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date updatedAt;
	
	@Enumerated(EnumType.STRING)
	private InvoiceStatus invoiceStatus;

}
