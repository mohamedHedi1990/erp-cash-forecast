package org.apac.erp.cach.forecast.dtos;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.enumeration.OperationDtoType;
import org.apac.erp.cach.forecast.enumeration.OperationType;
import org.apac.erp.cach.forecast.persistence.entities.BankAccount;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class OperationTreserorieDto {

	private String operationAmountS;
	private Double operationAmount;
	private Double progressiveAmount;
	private String progressiveAmountS;
	private String opperationCurrency;
	private String opperationLabel;
	private String opperationFacultatifLabel;
	private List<String> opperationDetails;
	private OperationType opperationType;
	private String beneficiaryName;
	private boolean isValidated = false;
	private Long operationRealId;
	private OperationDtoType operationRealType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	private Date operationDate;
	private BankAccount operationAccount;
	private String decaissementType;
	private Boolean isInTheSimulatedPeriod;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
	private Date historyOperationDate;
	
	private String operationCategory;
}
