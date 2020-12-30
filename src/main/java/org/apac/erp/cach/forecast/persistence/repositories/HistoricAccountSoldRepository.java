package org.apac.erp.cach.forecast.persistence.repositories;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.HistoricAccountSold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface HistoricAccountSoldRepository extends JpaRepository<HistoricAccountSold, Long>{

	public HistoricAccountSold findTopByBankAccountAndDateLessThanEqualOrderByCreatedAtDesc(BankAccount bankAccount, Date endDate);
		
	public HistoricAccountSold findTopByBankAccountAndDateGreaterThanEqualOrderByCreatedAtAsc(BankAccount bankAccount, Date startDate);
	
	public HistoricAccountSold findTopByBankAccountOrderByCreatedAtAsc(BankAccount bankAccount);
	List<HistoricAccountSold> findByBankAccount(BankAccount bankAccount);
	@Transactional
	public void deleteAllByBankAccount(BankAccount bankAccount);
}
