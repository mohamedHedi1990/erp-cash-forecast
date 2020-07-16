package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "erp_agency")
@EqualsAndHashCode(callSuper = false)
@Data
public class Agency extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long agencyId;

	private String agencyName;

	private String agencyAddress;

	private String agencyPhoneNumber;

	private String agencyEmail;

	@OneToMany(cascade = CascadeType.REMOVE)
	private List<BankAccount> agencyBankAccounts;

	@ManyToOne
	private Bank agencyBank;
	
	@OneToMany
	private List<Contact> agencyContacts;
}
