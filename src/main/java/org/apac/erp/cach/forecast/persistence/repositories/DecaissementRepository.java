package org.apac.erp.cach.forecast.persistence.repositories;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.Decaissement;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecaissementRepository extends JpaRepository<Decaissement, Long> {

	List<Decaissement> findByDecaissementBankAccountAndDecaissementDeadlineDateBetweenOrderByDecaissementDeadlineDateAsc(BankAccount bankAccount, Date startDate, Date endDate);


}
