package org.apac.erp.cach.forecast.dtos;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.enumeration.RsTypeSaisie;
import org.apac.erp.cach.forecast.persistence.entities.Provider;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProviderInvoiceDTO {
	private Long invoiceId;

	private String invoiceNumber;

	private Integer invoiceDeadlineInNumberOfDays;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Tunis")
	private Date invoiceDeadlineDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Tunis")
	private Date invoiceDate;

	private Double invoiceTotalAmount;

	private Double invoiceRs;
	
	private RsTypeSaisie invoiceRsType;

	private Double invoiceNet;

	private Double invoicePayment;

	private String providerLabel;
	
	private Provider provider;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date updatedAt;
	
	@Enumerated(EnumType.STRING)
	private InvoiceStatus invoiceStatus;

}
