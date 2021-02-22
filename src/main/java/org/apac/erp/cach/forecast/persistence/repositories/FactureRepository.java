package org.apac.erp.cach.forecast.persistence.repositories;

import org.apac.erp.cach.forecast.enumeration.FactureType;
import org.apac.erp.cach.forecast.persistence.entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface FactureRepository extends JpaRepository<Facture,Long> {
    List<Facture> findAllByOrderByFactureDate();
    List<Facture> findByFactureDeadlineDateBetweenOrderByFactureDate(Date begin,Date end);
    //Facture findTopByOrderByCreatedAtDesc();
    Optional<Facture> findTopByFactureTypeOrderByCreatedAtDesc(FactureType type);

    Optional<Facture> findByFactureIdNotLikeAndFactureNumberAndFactureType(Long factureId, String factureNumber, FactureType factureType);
}
