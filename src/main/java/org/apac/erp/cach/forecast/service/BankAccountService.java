package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.HistoricAccountSold;
import org.apac.erp.cach.forecast.persistence.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

	@Autowired
	private BankAccountRepository bankAccountRepo;
	
	@Autowired
	private HistoricAccountSoldService historicAccountSoldService;

	public BankAccount saveAccount(BankAccount account) {
		BankAccount accountB = this.bankAccountRepo.save(account);
		HistoricAccountSold historicSolde = new HistoricAccountSold(account, account.getAccountInitialAmount());
		historicAccountSoldService.saveHistoricSolde(historicSolde);
		return accountB;
	}
	
	public List<BankAccount> getAllBankAccounts() {
		return this.bankAccountRepo.findAll();
	}
	
	public BankAccount getAccountById(Long accountId) {
		return this.bankAccountRepo.findOne(accountId);
	}

	public void deleteAccount(Long accountId) {
		 this.bankAccountRepo.delete(accountId);
		
	}
}