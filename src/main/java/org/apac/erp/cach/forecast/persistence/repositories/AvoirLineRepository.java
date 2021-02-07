package org.apac.erp.cach.forecast.persistence.repositories;

import org.apac.erp.cach.forecast.persistence.entities.Avoir;
import org.apac.erp.cach.forecast.persistence.entities.AvoirLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvoirLineRepository extends JpaRepository<AvoirLine, Long> {
    List<AvoirLine> findByAvoir(Avoir avoir);
}
