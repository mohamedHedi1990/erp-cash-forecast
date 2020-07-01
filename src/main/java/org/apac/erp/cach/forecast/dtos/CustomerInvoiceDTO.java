package org.apac.erp.cach.forecast.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerInvoiceDTO {
	private Long invoiceId;

	private Integer invoiceNumber;

	private Integer invoiceDeadlineInNumberOfDays;

	private Date invoiceDeadlineDate;

	private Date invoiceDate;

	private Double invoiceTotalAmount;

	private Double invoiceRs;

	private Double invoiceNet;

	private Double invoicePayment;

	private String customerLabel;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date updatedAt;

}
