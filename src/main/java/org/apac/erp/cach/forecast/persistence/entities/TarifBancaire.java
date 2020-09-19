package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.apac.erp.cach.forecast.enumeration.Annuity;

import lombok.Data;

@Entity
@Table(name = "erp_tarification_bancaire")
@Data
public class TarifBancaire extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tarifId;
	
	private String tarifLabel;
	
	@ManyToOne
	private BankAccount tarifAccount;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Comission> comissions;

}
