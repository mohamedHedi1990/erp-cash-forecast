package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apac.erp.cach.forecast.constants.Utils;
import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.enumeration.RsTypeSaisie;

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

	private String invoiceNumber;

	private Integer invoiceDeadlineInNumberOfDays;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	@Temporal(TemporalType.DATE)
	private Date invoiceDeadlineDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	@Temporal(TemporalType.DATE)
	private Date invoiceDate;

	private Double invoiceTotalAmount;
	
	private String invoiceTotalAmountS;

	private Double invoiceRs;

	private RsTypeSaisie invoiceRsType;

	private Double invoiceNet;

	private Double invoicePayment;
	
	private String invoicePaymentS;

	@Enumerated(EnumType.STRING)
	private InvoiceStatus invoiceStatus;
	
	private String invoiceCurrency;

	@OneToMany(mappedBy = "invoice",cascade= {CascadeType.MERGE,CascadeType.REMOVE})
	private List<PaymentRule> invoicePaymentRules;
	
	@Transient
	private String invoiceDates;
	
	@Transient
	private String invoiceDeadlineDates;
	
	@Transient
	private boolean isAssocited;
	
	@PrePersist
	public void initInvoice() {
		if (this.invoiceId == null) {
			this.invoicePayment = 0.0;
		}
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setGroupingSeparator(',');
		simbolos.setDecimalSeparator('.');
		this.invoiceTotalAmount =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.invoiceTotalAmount));
		this.invoicePayment =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.invoicePayment));
		this.invoiceTotalAmountS = Utils.convertAmountToStringWithSeperator(this.invoiceTotalAmount);
		this.invoicePaymentS = Utils.convertAmountToStringWithSeperator(this.invoicePayment);
		
	}

	@PreUpdate
	public void preUpdate() {
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setGroupingSeparator(',');
		simbolos.setDecimalSeparator('.');
		this.invoiceTotalAmount =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.invoiceTotalAmount));
		this.invoicePayment =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.invoicePayment));
		this.invoiceTotalAmountS = Utils.convertAmountToStringWithSeperator(this.invoiceTotalAmount);
		this.invoicePaymentS = Utils.convertAmountToStringWithSeperator(this.invoicePayment);
	}

}
