package org.apac.erp.cach.forecast.persistence.repositories;

import org.apac.erp.cach.forecast.persistence.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FactureLineRepository extends JpaRepository<FactureLine, Long> {
    List<FactureLine> findByFacture(Facture facture);

    List<FactureLine> findByFactureInAndProductAndProductGroup(List<Facture> factures, Product product, ProductGroup productGroup);

    List<FactureLine> findByFactureInAndProduct(List<Facture> factures, Product product);
}
