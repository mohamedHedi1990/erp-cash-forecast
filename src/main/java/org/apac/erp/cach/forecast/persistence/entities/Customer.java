package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "erp_customer")
@Data
public class Customer extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerId;

	private String customerLabel;

	private String customerAddress;

	private String customerUniqueIdentifier;

	private String customerManagerName;

	private String customerContactNumber;

	@OneToMany
	private List<CustomerInvoice> customerInvoices;

	@OneToMany
	private List<Contact> customerContacts;

}
