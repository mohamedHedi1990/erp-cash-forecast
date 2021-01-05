package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apac.erp.cach.forecast.constants.Utils;
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
	
	private String comissionValueS;
	
	@Enumerated(EnumType.STRING)
	private ComissionType commissionType;
	@ManyToOne
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	BankAccount bankAccount;
	
	@PrePersist
	public void initInvoice() {
		this.comissionValueS = Utils.convertAmountToStringWithSeperator(this.comissionValue);
		
	}

	@PreUpdate
	public void preUpdate() {
		this.comissionValueS = Utils.convertAmountToStringWithSeperator(this.comissionValue);
	}

}
