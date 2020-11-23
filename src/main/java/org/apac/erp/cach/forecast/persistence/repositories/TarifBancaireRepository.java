package org.apac.erp.cach.forecast.persistence.repositories;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.TarifBancaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarifBancaireRepository extends JpaRepository<TarifBancaire, Long> {
//public List<TarifBancaire>findByTarifAccount(BankAccount account);
}
