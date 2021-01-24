package org.apac.erp.cach.forecast.persistence.repositories;

import org.apac.erp.cach.forecast.persistence.entities.BonLivraison;
import org.apac.erp.cach.forecast.persistence.entities.BonLivraisonLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonLivraisonLineRepository extends JpaRepository<BonLivraisonLine, Long> {
    List<BonLivraisonLine> findByBonLivraison(BonLivraison bonLivraison);
}
