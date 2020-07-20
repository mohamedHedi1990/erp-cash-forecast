package org.apac.erp.cach.forecast.dtos;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.apac.erp.cach.forecast.persistence.entities.Contact;
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
	
	private List<Contact> providerContacts;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date updatedAt;

}
