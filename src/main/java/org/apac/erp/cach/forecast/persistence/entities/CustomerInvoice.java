package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class CustomerInvoice extends Invoice implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Customer customer;
	private Boolean isFacture;


}
