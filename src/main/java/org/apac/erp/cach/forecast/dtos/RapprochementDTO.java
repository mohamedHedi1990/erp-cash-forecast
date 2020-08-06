package org.apac.erp.cach.forecast.dtos;

import java.util.Date;

import org.apac.erp.cach.forecast.enumeration.TransactionType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class RapprochementDTO {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	private Date operationDate;
	
	private Double amount;
	
	private TransactionType type;
	
	private String label;
	
	private String details;
	
	

}
