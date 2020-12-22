package org.apac.erp.cach.forecast.service;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.HistoricAccountSold;
import org.apac.erp.cach.forecast.persistence.repositories.HistoricAccountSoldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoricAccountSoldService {
	
	@Autowired
	private HistoricAccountSoldRepository historicAccountSoldRepo;
	
	@Autowired
	private BankAccountService accountService;
	
	public HistoricAccountSold findFirstByBankAccountAndCreatedAtLessThanEqualOrderByCreatedAtDesc(BankAccount bankAccount, Date endDate) {
		return this.historicAccountSoldRepo.findTopByBankAccountAndDateLessThanEqualOrderByCreatedAtDesc(bankAccount, endDate);
	}
	
	public HistoricAccountSold findFirstByBankAccountAndCreatedAtGreaterThanEqualOrderByCreatedAtAsc(BankAccount bankAccount, Date startDate) {
		return this.historicAccountSoldRepo.findTopByBankAccountAndDateGreaterThanEqualOrderByCreatedAtAsc(bankAccount, startDate);
	}

	public HistoricAccountSold findTheBeginningSold(Long accountId, Date startDate) {
		BankAccount bankAccount = this.accountService.getAccountById(accountId);
		HistoricAccountSold beginSoldeHistoric = findFirstByBankAccountAndCreatedAtLessThanEqualOrderByCreatedAtDesc(bankAccount, startDate);
		if(beginSoldeHistoric == null) {
			beginSoldeHistoric = findFirstByBankAccountAndCreatedAtGreaterThanEqualOrderByCreatedAtAsc(bankAccount, startDate);
		}
		
		return beginSoldeHistoric;
	}

	public HistoricAccountSold findFirst(Long accountId, Date startDate) {
		BankAccount bankAccount = this.accountService.getAccountById(accountId);
		HistoricAccountSold beginSoldeHistoric = this.historicAccountSoldRepo.findTopByBankAccountAndDateGreaterThanEqualOrderByCreatedAtAsc(bankAccount, startDate);
		
		return beginSoldeHistoric;
	}
	public HistoricAccountSold saveHistoricSolde(HistoricAccountSold historicSolde) {
		return this.historicAccountSoldRepo.save(historicSolde);
	}
}
