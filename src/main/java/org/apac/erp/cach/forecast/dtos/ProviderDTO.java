package org.apac.erp.cach.forecast.dtos;

import java.util.Date;

import javax.persistence.Column;

import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	
	protected Date createdAt;

	protected Date updatedAt;

	private String companyName;

}
