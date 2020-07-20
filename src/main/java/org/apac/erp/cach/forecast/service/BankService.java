package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Bank;
import org.apac.erp.cach.forecast.persistence.entities.Contact;
import org.apac.erp.cach.forecast.persistence.repositories.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankService {

	@Autowired
	private BankRepository bankRepo;

	@Autowired
	private ContactService contactService;

	public List<Bank> findAllBanks() {
		return bankRepo.findAll();
	}

	public Bank saveNewBank(Bank bank) {

		List<Contact> contacts = bank.getBankContacts();
		contacts.stream().forEach(contact -> contactService.saveNewContact(contact));

		return bankRepo.save(bank);
	}

	public Bank findBankById(Long bankId) {
		return bankRepo.findOne(bankId);
	}

	public void deleteBank(Long bankId) {
		bankRepo.delete(bankId);

	}

}
