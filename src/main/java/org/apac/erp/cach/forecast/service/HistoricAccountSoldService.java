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
		return this.historicAccountSoldRepo.findFirstByBankAccountAndCreatedAtLessThanEqualOrderByCreatedAtDesc(bankAccount, endDate);
	}
	
	public HistoricAccountSold findFirstByBankAccountAndCreatedAtGreaterThanEqualOrderByCreatedAtAsc(BankAccount bankAccount, Date startDate) {
		return this.historicAccountSoldRepo.findFirstByBankAccountAndCreatedAtGreaterThanEqualOrderByCreatedAtAsc(bankAccount, startDate);
	}

	public List<HistoricAccountSold> findTheBeginningSold(Long accountId, Date startDate) {
		BankAccount bankAccount = this.accountService.getAccountById(accountId);
		return null;
	}

}
