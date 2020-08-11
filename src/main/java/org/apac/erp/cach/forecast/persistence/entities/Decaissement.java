package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apac.erp.cach.forecast.enumeration.DecaissementType;

import lombok.Data;

@Entity
@Table(name = "erp_decaissement")
@Data
public class Decaissement extends Transaction implements Serializable {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private DecaissementType transactionType;

	@ManyToOne
	private ProviderInvoice invoice;

}
