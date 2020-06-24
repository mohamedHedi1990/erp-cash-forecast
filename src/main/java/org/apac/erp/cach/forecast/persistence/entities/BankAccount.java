package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

	@ManyToOne
	private Agency bankAccountAgency;
	
	@OneToMany
	private List<EncaissementDecaissement> bankAccountEncaissementDecaissements;

	@PrePersist
	private void persistId() {
		if (this.createdAt == null) {
			this.createdAt = new Date();
		}
		this.updatedAt = new Date();

	}

}
