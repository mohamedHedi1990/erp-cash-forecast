package org.apac.erp.cach.forecast.dtos;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apac.erp.cach.forecast.enumeration.Annuity;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class TimeLineInitialInfo {

	private Double timeLineInitialAmount;

	private Annuity timeLineAnnuity;

	private Integer timeLineAnnityNumber;

	private Double timeLineInterestRate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	@Temporal(TemporalType.DATE)
	private Date timeLineStartDate;
}
