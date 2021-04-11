package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PreUpdate;

import org.apac.erp.cach.forecast.constants.Utils;

import lombok.Data;

@Entity
@Data
public class CustomerInvoice extends Invoice implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Customer customer;
	private Boolean isFacture;
	
	private Boolean isGeneratedInvoice;
	

}
