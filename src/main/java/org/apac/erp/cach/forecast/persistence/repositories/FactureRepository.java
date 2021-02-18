package org.apac.erp.cach.forecast.persistence.repositories;

import org.apac.erp.cach.forecast.persistence.entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FactureRepository extends JpaRepository<Facture,Long> {
    List<Facture> findAllByOrderByFactureDate();
    List<Facture> findByFactureDeadlineDateBetweenOrderByFactureDate(Date begin,Date end);
}
