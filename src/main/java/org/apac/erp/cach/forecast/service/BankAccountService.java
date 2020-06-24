package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

	@Autowired
	private BankAccountRepository bankAccountRepo;

	public List<BankAccount> findAllBankAccounts() {
		return bankAccountRepo.findAll();
	}

}
