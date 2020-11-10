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
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.apac.erp.cach.forecast.constants.Utils;
import org.apac.erp.cach.forecast.enumeration.Annuity;

import lombok.Data;

@Entity
@Table(name = "erp_timeline")
@Data
public class TimeLine extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long timeLineId;
	
	private String timeLineLabel;
	
	@ManyToOne
	private BankAccount timeLineAccount;

	private String timeLineCreditNumber;
	
	private Double timeLineInitialAmount;
	
	private String timeLineInitialAmountS;
	
	private Integer timeLineYearNumber;
	
	@Enumerated(EnumType.STRING)
	private Annuity timeLineAnnuity;
	
	private Double timeLineInterestRate;
	private String timeLineInterestRateS;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<TimeLineEntry> timeLineTable;
	
	@PrePersist
	public void initInvoice() {
		
		this.timeLineInitialAmountS = Utils.convertAmountToString(this.timeLineInitialAmount);
		this.timeLineInterestRateS = Utils.convertAmountToString(this.timeLineInterestRate);
		
	}

	@PreUpdate
	public void preUpdate() {
		this.timeLineInitialAmountS = Utils.convertAmountToString(this.timeLineInitialAmount);
		this.timeLineInterestRateS = Utils.convertAmountToString(this.timeLineInterestRate);
	}

}
