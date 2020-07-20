package org.apac.erp.cach.forecast.dtos;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Contact;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	
	private List<Contact> customerContacts;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date updatedAt;
	
}
