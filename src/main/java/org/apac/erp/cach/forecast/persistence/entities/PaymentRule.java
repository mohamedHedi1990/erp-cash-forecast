package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apac.erp.cach.forecast.constants.Utils;
import org.apac.erp.cach.forecast.enumeration.OperationType;
import org.apac.erp.cach.forecast.enumeration.PaymentMethod;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table(name = "erp_paymentRule")
@Data
public class  PaymentRule extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentRuleId;

	private PaymentMethod paymentRulePaymentMethod;

	private Integer paymentRulePaymentMethodNb;
	
	private String paymentRuleNumber;
	
	private String paymentRuleDetails;
	
	private String paymentRuleLabel;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	@Temporal(TemporalType.TIMESTAMP)
	private Date paymentRuleDeadlineDate;

	private boolean isValidated = false;
	
	private Double paymentRuleAmount;

	private String paymentRuleAmountS;
	
	@ManyToOne
	private BankAccount paymentRuleAccount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "invoice_id")
	@JsonIgnore
	private Invoice invoice;
	
	private String paymentRuleInvoices;
	
	private boolean isRelatedComissionValidated = false;
	
	private boolean isRelatedToAnAttachedInvoices = false;
	private Long attachedInvoicesId;
	
	@ManyToOne
	private Customer customer;
	
	@ManyToOne
	private Provider provider;
	
	@Enumerated(EnumType.STRING)
	private OperationType paymentRuleOperationType;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	@Temporal(TemporalType.TIMESTAMP)
	private Date bankOperationDate;

	@PrePersist
	public void initPR() {
		this.paymentRuleAmountS = Utils.convertAmountToStringWithSeperator(this.paymentRuleAmount);
		
	}

	@PreUpdate
	public void preUpdate() {
		this.paymentRuleAmountS = Utils.convertAmountToStringWithSeperator(this.paymentRuleAmount);
	}

	
	
}
