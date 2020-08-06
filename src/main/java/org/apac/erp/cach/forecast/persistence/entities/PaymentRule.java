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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apac.erp.cach.forecast.enumeration.PaymentMethod;
import org.apac.erp.cach.forecast.enumeration.TransactionType;

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

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Tunis")
	@Temporal(TemporalType.TIMESTAMP)
	private Date paymentRuleDeadlineDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	@Temporal(TemporalType.TIMESTAMP)
	private Date paymentValidationDate;
	
	private TransactionType paymentRuleType;

	private boolean isValidated;
	
	@Enumerated(EnumType.STRING)
	private TransactionType type;
	
	private Double paymentRuleAmount;
	
	@ManyToOne
	private Invoice invoice;
	
	@ManyToOne
	private BankAccount account;
	
}
