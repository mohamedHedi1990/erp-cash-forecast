package org.apac.erp.cach.forecast.persistence.repositories;

import org.apac.erp.cach.forecast.persistence.entities.BonLivraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BonLivraisonRepository extends JpaRepository<BonLivraison,Long> {
    List<BonLivraison> findByBonLivraisonIdIn(List<Long> blsIds);
    Optional<BonLivraison> findTopByOrderByCreatedAtDesc();
    Optional<BonLivraison> findByBonLivraisonIdNotLikeAndBonLivraisonNumber(Long bonLivraisonId, String bonLivraisonNumber);
}
