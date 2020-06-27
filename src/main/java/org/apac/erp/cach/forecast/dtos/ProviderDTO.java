package org.apac.erp.cach.forecast.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProviderDTO {

	private Long providerId;

	private String providerLabel;

	private String providerAddress;

	private String providerUniqueIdentifier;

	private String providerManagerName;

	private String providerContactNumber;

	private String companyName;

}
