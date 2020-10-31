package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apac.erp.cach.forecast.constants.Utils;
import org.apac.erp.cach.forecast.enumeration.OperationType;
import org.apac.erp.cach.forecast.enumeration.PaymentMethod;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table(name = "erp_paymentRule")
@Data
public class PaymentRule extends AuditableSql implements Serializable {

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

	private Boolean isValidated;
	
	private Double paymentRuleAmount;

	private String paymentRuleAmountS;
	
	@ManyToOne
	private BankAccount paymentRuleAccount;
	
	private String paymentRuleInvoices;
	
	@Enumerated(EnumType.STRING)
	private OperationType paymentRuleOperationType;

	@PrePersist
	public void initPR() {
		
		this.paymentRuleAmountS = Utils.convertAmountToString(this.paymentRuleAmount);
		
	}

	@PreUpdate
	public void preUpdate() {
		this.paymentRuleAmountS = Utils.convertAmountToString(this.paymentRuleAmount);
	}

	
	
}
