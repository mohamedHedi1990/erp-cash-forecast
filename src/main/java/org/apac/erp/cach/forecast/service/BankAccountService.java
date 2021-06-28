package org.apac.erp.cach.forecast.service;

import java.util.Date;
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
		//re-persister les tarifications banciares: suite au bug remonté que les comissions sont supprimés une fois on modifie le compte bancaire
		if(account.getAccountId() != null && account.getAccountComissions() != null && !account.getAccountComissions().isEmpty()) {
			account.getAccountComissions().stream().forEach(comission -> comission.setBankAccount(account));
		}
		
		BankAccount accountB = this.bankAccountRepo.save(account);
		HistoricAccountSold historicSolde = new HistoricAccountSold(account, account.getAccountInitialAmount(), new Date());
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

	public double getGlobalAmount() {
		return getAllBankAccounts().stream().mapToDouble(account -> account.getAccountInitialAmount()).sum();
	}
}