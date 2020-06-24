package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Bank;
import org.apac.erp.cach.forecast.persistence.repositories.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankService {

	@Autowired
	private BankRepository bankRepo;

	public List<Bank> findAllBanks() {
		return bankRepo.findAll();
	}

}
