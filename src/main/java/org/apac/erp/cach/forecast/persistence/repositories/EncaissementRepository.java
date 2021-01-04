package org.apac.erp.cach.forecast.persistence.repositories;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.Decaissement;
import org.apac.erp.cach.forecast.persistence.entities.Encaissement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncaissementRepository extends JpaRepository<Encaissement, Long> {
	List<Encaissement> findByEncaissementBankAccountAndEncaissementDeadlineDateBetweenOrderByEncaissementDeadlineDateAsc(BankAccount bankAccount, Date startDate, Date endDate);
	List<Encaissement> findByEncaissementBankAccountAndIsValidatedAndEncaissementDeadlineDateBeforeOrderByEncaissementDeadlineDateAsc(BankAccount bankAccount, Boolean isValidated, Date startDate);

}
