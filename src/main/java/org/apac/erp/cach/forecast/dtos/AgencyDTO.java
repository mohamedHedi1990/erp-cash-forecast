package org.apac.erp.cach.forecast.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgencyDTO {

	private Long agencyId;

	private String agencyName;

	private String agencyAddress;

	private String agencyPhoneNumber;

	private String agencyEmail;

	private String agencyBankName;
	
	protected Date createdAt;

	protected Date updatedAt;

}
