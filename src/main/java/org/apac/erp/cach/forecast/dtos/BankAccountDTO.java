package org.apac.erp.cach.forecast.dtos;

import java.util.Date;

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
	
	protected Date createdAt;

	protected Date updatedAt;

}
