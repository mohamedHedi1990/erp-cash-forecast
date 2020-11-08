package org.apac.erp.cach.forecast.persistence.repositories;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.HistoricAccountSold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricAccountSoldRepository extends JpaRepository<HistoricAccountSold, Long>{

	public HistoricAccountSold findFirstByBankAccountAndCreatedAtLessThanEqualOrderByCreatedAtDesc(BankAccount bankAccount, Date endDate);
	
	public HistoricAccountSold findFirstByBankAccountAndCreatedAtGreaterThanEqualOrderByCreatedAtAsc(BankAccount bankAccount, Date startDate);

}
