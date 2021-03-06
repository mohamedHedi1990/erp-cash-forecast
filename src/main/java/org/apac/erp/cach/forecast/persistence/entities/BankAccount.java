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
@Table(name = "erp_bankAccount")
@Data
public class BankAccount extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountId;

	private String accountLabel;
	
	private String accountBank;
	
	private String accountBankAdress;
	
	private String accountAgency;
	
	private double accountInitialAmount;
	private double accountOpenedAmount;
	private String accountOpenedAmountS;
	private String accountInitialAmountS;
	
	private String accountAgencyAdress;

	private String accountChargeCustomerName;

	private String accountChargeCustomerPhoneNumber;
	
	private String accountChargeCustomerEmail;
	
	private String accountCurrency;
	
	private String accountNumber;
	
	private String accountRIB;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Contact> accountContacts;
	
	@OneToMany(mappedBy = "bankAccount",cascade = CascadeType.ALL)
	private List<Comission> accountComissions;

	@OneToMany(mappedBy = "bankAccount",cascade = CascadeType.ALL)
	private List<HistoryOperationBank> historyOperationBanks;

	@PrePersist
	public void initInvoice() {
		this.accountInitialAmount =  (double)(Math.round(this.accountInitialAmount * 1000))/1000;
		this.accountInitialAmountS = Utils.convertAmountToStringWithSeperator(this.accountInitialAmount);
		this.accountOpenedAmount=this.accountInitialAmount;
		this.accountOpenedAmountS=Utils.convertAmountToStringWithSeperator(this.accountOpenedAmount);
		this.accountComissions = new ArrayList<Comission>();
		
	}

	@PreUpdate
	public void preUpdate() {
		this.accountInitialAmount =  (double)(Math.round(this.accountInitialAmount * 1000))/1000;
		this.accountInitialAmountS = Utils.convertAmountToStringWithSeperator(this.accountInitialAmount);
	}
	
}
