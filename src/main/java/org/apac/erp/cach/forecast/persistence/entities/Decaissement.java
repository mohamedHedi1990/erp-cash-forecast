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
import org.apac.erp.cach.forecast.enumeration.PaymentMethod;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table(name = "erp_decaissement")
@Data
public class Decaissement extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long decaissementId;

	@ManyToOne
	private DecaissementType decaissementType;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	@Temporal(TemporalType.TIMESTAMP)
	private Date decaissementDeadlineDate ;

	@Enumerated(EnumType.STRING)
	private PaymentMethod decaissementPaymentType;
	
	private String decaissementPaymentRuleNumber;
	
	private String decaissementPaymentRuleDetails;

	private Double decaissementAmount ;
	
	private String decaissementAmountS ;

	private String decaissementLabel;
	
	private String decaissementChequeImpaye;
	
	
	private String decaissementDetails;
	
	@ManyToOne
	private Invoice decaissementInvoice ;
		
	@ManyToOne
	private BankAccount decaissementBankAccount;
	
	@ManyToOne
	private Provider decaissementProvider;
	
	private boolean isValidated = false;
	
	private String decaissementCurrency;
	
	private boolean isRelatedComissionValidated = false;
	
	private String beneficaryName;


	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	@Temporal(TemporalType.TIMESTAMP)
	private Date bankOperationDate;
	@PrePersist
	public void initPR() {
		
		this.decaissementAmountS = Utils.convertAmountToStringWithSeperator(this.decaissementAmount);
		
	}

	@PreUpdate
	public void preUpdate() {
		this.decaissementAmountS = Utils.convertAmountToStringWithSeperator(this.decaissementAmount);
	}

}
