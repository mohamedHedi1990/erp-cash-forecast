package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
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
import org.apac.erp.cach.forecast.enumeration.EncaissementDecaissementType;
import org.apac.erp.cach.forecast.enumeration.PaymentMethod;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table(name = "erp_encaissement")
@Data
public class Encaissement extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long encaissementId;

	@ManyToOne
	private EncaissementType encaissementType;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	@Temporal(TemporalType.TIMESTAMP)
	private Date encaissementDeadlineDate;

	@Enumerated(EnumType.STRING)
	private PaymentMethod encaissementPaymentType;

	private String encaissementPaymentRuleNumber;

	private String encaissementPaymentRuleDetails;

	private Double encaissementAmount;

	private String encaissementAmountS;

	private String encaissementLabel;

	private String encaissementDetails;

	@ManyToOne(cascade = CascadeType.ALL)
	private Invoice encaissementInvoice;

	@ManyToOne(cascade = CascadeType.ALL)
	private BankAccount encaissementBankAccount;

	@ManyToOne(cascade = CascadeType.ALL)
	private Customer encaissementCustomer;

	private Boolean isValidated;

	private String encaissementCurrency;
	
	
	@PrePersist
	public void initPR() {
		
		this.encaissementAmountS = Utils.convertAmountToString(this.encaissementAmount);
		
	}

	@PreUpdate
	public void preUpdate() {
		this.encaissementAmountS = Utils.convertAmountToString(this.encaissementAmount);
	}

}
