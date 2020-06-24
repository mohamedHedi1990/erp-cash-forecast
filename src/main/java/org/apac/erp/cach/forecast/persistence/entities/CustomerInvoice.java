package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "erp_customerInvoice")
@Data
public class CustomerInvoice extends Invoice implements Serializable {

	private static final long serialVersionUID = 1L;

}
