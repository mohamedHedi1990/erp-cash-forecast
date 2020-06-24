 package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "erp_compagny")
@Data
public class Company extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long campanyId;

	private String campanyName;

	private String campanyAddress;

	private String campanyUniqueIdentifier;

	private String campanyPhoneNumber;

	@OneToMany
	private List<BankAccount> campanyBankAccounts;

	@OneToMany
	private List<Timeline> campanyTimelines;

	@OneToMany
	private List<Customer> campanyCustomers;

	@OneToMany
	private List<Provider> campanyProviders;

	@PrePersist
	private void persistId() {
		if (this.createdAt == null) {
			this.createdAt = new Date();
		}
		this.updatedAt = new Date();

	}

}
