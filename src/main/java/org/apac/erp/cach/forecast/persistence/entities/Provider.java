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
@Table(name = "erp_provider")
@Data
public class Provider extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long providerId;

	private String providerLabel;

	private String providerAddress;

	private String providerUniqueIdentifier;

	private String providerManagerName;

	private String providerContactNumber;

	@OneToMany
	private List<ProviderInvoice> providerInvoices;
	
	@OneToMany
	private List<Contact> providerContacts;

}
