package org.apac.erp.cach.forecast.service;

import java.util.ArrayList;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.BankAccountDTO;
import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BankAccountService {

	@Autowired
	private BankAccountRepository bankAccountRepo;

	public List<BankAccountDTO> findAllBankAccounts() {
		List<BankAccount> accounts = bankAccountRepo.findAll();
		List<BankAccountDTO> dtos = new ArrayList<>();
		accounts.stream().forEach(account -> {
			BankAccountDTO dto = new BankAccountDTO(account.getBankAccountId(), account.getBankAccountLabel(),
					account.getBankAccountNumber(), account.getBankAccountChargeCustomerName(),
					account.getBankAccountChargeCustomerPhoneNumber(), account.getBankAccountAgency().getAgencyName(),
					account.getBankAccountCompany().getCampanyName());
			dtos.add(dto);
		});
		return dtos;
	}

}