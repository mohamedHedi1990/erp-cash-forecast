package org.apac.erp.cach.forecast.persistence.repositories;

import org.apac.erp.cach.forecast.persistence.entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactureRepository extends JpaRepository<Facture,Long> {
}
