package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apac.erp.cach.forecast.enumeration.ComissionType;
import org.apac.erp.cach.forecast.enumeration.Operation;

import lombok.Data;

@Entity
@Table(name = "erp_comission_bancaire")
@Data
public class Comission extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long comissionId;
	
	private String comissionLabel;
	
	@Enumerated(EnumType.STRING)
	private Operation comissionOperation;
	
	private Double comissionValue;
	
	@Enumerated(EnumType.STRING)
	private ComissionType commissionType;

}
