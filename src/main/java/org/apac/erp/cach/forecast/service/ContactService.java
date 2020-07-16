package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.Contact;
import org.apac.erp.cach.forecast.persistence.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
	
	@Autowired
	private ContactRepository contactRepo;
	
	public Contact saveNewContact(Contact contact) {
		return contactRepo.save(contact);
	}

}
