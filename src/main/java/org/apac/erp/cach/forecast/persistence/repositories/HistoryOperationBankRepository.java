package org.apac.erp.cach.forecast.persistence.repositories;

import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.HistoryOperationBank;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HistoryOperationBankRepository extends JpaRepository<HistoryOperationBank,Long> {
    List<HistoryOperationBank> findByBankAccountAndHistoryOperationBankDateGreaterThanEqualOrderByHistoryOperationBankDate(BankAccount bankAccount, Date historyOperationBankValueDate);
    HistoryOperationBank findFirstByBankAccountAndHistoryOperationBankDateBeforeOrderByHistoryOperationBankDateDesc(BankAccount bankAccount, Date historyOperationBankValueDate);

}
