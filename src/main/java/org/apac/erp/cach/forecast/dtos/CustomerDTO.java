package org.apac.erp.cach.forecast.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerDTO {
	
	private Long customerId;

	
	private String customerLabel;

	private String customerAddress;

	private String customerUniqueIdentifier;

	private String customerManagerName;

	private String customerContactNumber;
	
	protected Date createdAt;

	protected Date updatedAt;
	
}
