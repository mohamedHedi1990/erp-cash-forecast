package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "erp_bankAccount")
@Data
public class BankAccount extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bankAccountId;

	private String bankAccountLabel;

	private String bankAccountNumber;

	private String bankAccountChargeCustomerName;

	private String bankAccountChargeCustomerPhoneNumber;
	
	private String bankAccountCurrency;

	@ManyToOne
	private Agency bankAccountAgency;
	
}
