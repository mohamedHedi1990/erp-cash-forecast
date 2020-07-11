package org.apac.erp.cach.forecast.dtos;

import java.util.Date;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRuleDTO {

	private Long invoiceId;

	private String invoiceNumber;

	private Integer invoiceDeadlineInNumberOfDays;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Tunis")
	private Date invoiceDeadlineDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Tunis")
	private Date invoiceDate;

	private Double invoiceTotalAmount;

	private Double invoiceRs;

	private Double invoiceNet;

	private Double invoicePayment;

	private String ownerLabel;

	private Long ownerId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date updatedAt;

	@Enumerated(EnumType.STRING)
	private InvoiceStatus invoiceStatus;

	private List<PaymentRule> payments;

	public PaymentRuleDTO(Long invoiceId, String invoiceNumber, Integer invoiceDeadlineInNumberOfDays,
			Date invoiceDeadlineDate, Date invoiceDate, Double invoiceTotalAmount, Double invoiceRs, Double invoiceNet,
			Double invoicePayment, String customerLabel, Long ownerId, Date createdAt, Date updatedAt,
			InvoiceStatus invoiceStatus) {
		super();
		this.invoiceId = invoiceId;
		this.invoiceNumber = invoiceNumber;
		this.invoiceDeadlineInNumberOfDays = invoiceDeadlineInNumberOfDays;
		this.invoiceDeadlineDate = invoiceDeadlineDate;
		this.invoiceDate = invoiceDate;
		this.invoiceTotalAmount = invoiceTotalAmount;
		this.invoiceRs = invoiceRs;
		this.invoiceNet = invoiceNet;
		this.invoicePayment = invoicePayment;
		this.ownerLabel = customerLabel;
		this.ownerId = ownerId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.invoiceStatus = invoiceStatus;
	}

}
