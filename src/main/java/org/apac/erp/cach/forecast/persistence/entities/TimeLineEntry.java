package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apac.erp.cach.forecast.constants.Utils;
import org.apac.erp.cach.forecast.enumeration.Annuity;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table(name = "erp_timeline_entry")
@Data
public class TimeLineEntry extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long timeLineEntryId;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	@Temporal(TemporalType.DATE)
	private Date lineDate;
	
	private Double  initialAmount;
	
	private String  initialAmountS;

	private Double interests;
	
	private String interestsS;
	
	private Double tva;
	
	private Double total;
	
	private String totalS;
	
	@Enumerated(EnumType.STRING)
	private Annuity timeLineAnnuity;
	
	private Double timeLineInterestRate;
	
	private String timeLineCreditNumber;
	
	@PrePersist
	public void initInvoice() {
		this.totalS = Utils.convertAmountToString(this.total);
		this.initialAmountS = Utils.convertAmountToString(this.initialAmount);
		this.interestsS = Utils.convertAmountToString(this.interests);

		
	}

	@PreUpdate
	public void preUpdate() {
		this.totalS = Utils.convertAmountToString(this.total);
		this.initialAmountS = Utils.convertAmountToString(this.initialAmount);
		this.interestsS = Utils.convertAmountToString(this.interests);
	}
	
	
}
