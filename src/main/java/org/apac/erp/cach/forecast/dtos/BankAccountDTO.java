package org.apac.erp.cach.forecast.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankAccountDTO {

	private Long bankAccountId;

	private String bankAccountLabel;

	private String bankAccountNumber;

	private String bankAccountChargeCustomerName;

	private String bankAccountChargeCustomerPhoneNumber;

	private String bankAccountAgencyName;
	
	private String bankAccountCurrency;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date updatedAt;

}
