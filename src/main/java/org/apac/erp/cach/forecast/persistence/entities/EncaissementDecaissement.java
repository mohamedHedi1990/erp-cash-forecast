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

import org.apac.erp.cach.forecast.enumeration.EncaissementDecaissementType;
import org.apac.erp.cach.forecast.enumeration.PaymentMethod;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table(name = "erp_encaissementDecaissement")
@Data
public class EncaissementDecaissement extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long encaissementDecaissementId;

	@Enumerated(EnumType.STRING)
	private EncaissementDecaissementType encaissementDecaissementType;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	@Temporal(TemporalType.TIMESTAMP)
	private Date encaissementDecaissementDeadlineDate;

	@Enumerated(EnumType.STRING)
	private PaymentMethod encaissementDecaissementPaymentType;

	private String encaissementDecaissementNb;

	private String encaissementDecaissementLabel;

	private Double encaissementDecaissementAmount;
	
	private String invoiceNumber;
		
	@ManyToOne
	private BankAccount bankAccount;

}
