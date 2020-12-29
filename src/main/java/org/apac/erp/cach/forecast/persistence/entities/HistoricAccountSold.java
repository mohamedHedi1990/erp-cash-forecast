package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.apac.erp.cach.forecast.constants.Utils;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "erp_historic_account_sold")
@Data
public class HistoricAccountSold extends AuditableSql implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long historicId;
	
	@ManyToOne
	private BankAccount bankAccount;
	
	private Double solde;
	
	private String soldeS;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	@Temporal(TemporalType.DATE)
	private Date date;

	
	public HistoricAccountSold() {
	}
	
	public HistoricAccountSold(BankAccount bankAccount, Double solde, Date date) {
		this.bankAccount = bankAccount;
		this.solde = solde;
		this.date = date;
	}
	
	@PrePersist
	public void initInvoice() {
		this.soldeS = Utils.convertAmountToString(this.solde);
		
	}

	@PreUpdate
	public void preUpdate() {
		this.soldeS = Utils.convertAmountToString(this.solde);
	}
}
