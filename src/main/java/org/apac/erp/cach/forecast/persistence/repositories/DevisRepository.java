package org.apac.erp.cach.forecast.persistence.repositories;

import org.apac.erp.cach.forecast.persistence.entities.Devis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevisRepository extends JpaRepository<Devis,Long> {
    List<Devis> findByDevisIdIn(List<Long> blsIds);
    Optional<Devis> findTopByOrderByCreatedAtDesc();
    Optional<Devis> findByDevisIdNotLikeAndDevisNumber(Long id,String number);
}
