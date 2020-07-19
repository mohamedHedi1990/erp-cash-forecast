package org.apac.erp.cach.forecast.dtos;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Contact;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	
	private List<Contact> agencyContacts;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date createdAt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date updatedAt;

}
