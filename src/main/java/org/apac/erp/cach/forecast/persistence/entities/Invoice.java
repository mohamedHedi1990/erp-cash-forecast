package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table(name = "erp_invoice")
@Data
public class Invoice extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long invoiceId;

	private Integer invoiceNumber;

	private Integer invoiceDeadlineInNumberOfDays;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Africa/Tunis")
	@Temporal(TemporalType.TIMESTAMP)
	private Date invoiceDeadlineDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Africa/Tunis")
	@Temporal(TemporalType.TIMESTAMP)
	private Date invoiceDate;

	private Double invoiceTotalAmount;

	private Double invoiceRs;

	private Double invoiceNet;

	private Double invoicePayment;

	@Enumerated(EnumType.STRING)
	private InvoiceStatus invoiceStatus;

	@OneToMany
	private List<PaymentRule> invoicePaymentRules;

	@PrePersist
	private void persistId() {
		if (this.createdAt == null) {
			this.createdAt = new Date();
		}
		this.updatedAt = new Date();

	}

}
