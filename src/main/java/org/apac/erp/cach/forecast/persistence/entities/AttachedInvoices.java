package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.apac.erp.cach.forecast.constants.Utils;

import lombok.Data;

@Entity
@Table(name = "erp_attached_invoices")
@Data
public class AttachedInvoices extends AuditableSql implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long attachedInvoicesId;

	@OneToMany(cascade = CascadeType.ALL)
	private List<PaymentRule> paymentRules = new ArrayList<>();

	private double totalPaidAmount;

	private double totalRequiredAmount;

	private String totalPaidAmountS;

	private String totalRequiredAmountS;
	
	private String externalId;
	
	@PrePersist
	public void initInvoice() {
		this.totalPaidAmount =  (double)(Math.round(this.totalPaidAmount * 1000))/1000;
		this.totalRequiredAmount =  (double)(Math.round(this.totalRequiredAmount * 1000))/1000;
		this.totalRequiredAmountS = Utils.convertAmountToStringWithSeperator(this.totalRequiredAmount);
		
		this.totalPaidAmountS = Utils.convertAmountToStringWithSeperator(this.totalPaidAmount);
	}

	@PreUpdate
	public void preUpdate() {
		this.totalPaidAmount =  (double)(Math.round(this.totalPaidAmount * 1000))/1000;
		this.totalRequiredAmount =  (double)(Math.round(this.totalRequiredAmount * 1000))/1000;
		this.totalRequiredAmountS = Utils.convertAmountToStringWithSeperator(this.totalRequiredAmount);
		
		this.totalPaidAmountS = Utils.convertAmountToStringWithSeperator(this.totalPaidAmount);
	}

}
