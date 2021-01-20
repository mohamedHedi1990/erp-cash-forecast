package org.apac.erp.cach.forecast.persistence.repositories;

import org.apac.erp.cach.forecast.persistence.entities.GeneratedInvoice;
import org.apac.erp.cach.forecast.persistence.entities.GeneratedInvoiceLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneratedInvoiceLineRepository extends JpaRepository<GeneratedInvoiceLine, Long> {
    List<GeneratedInvoiceLine> findByGeneratedInvoice(GeneratedInvoice generatedInvoice);
}
