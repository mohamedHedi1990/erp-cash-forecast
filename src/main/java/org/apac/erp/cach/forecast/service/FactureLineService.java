package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.Facture;
import org.apac.erp.cach.forecast.persistence.entities.FactureLine;
import org.apac.erp.cach.forecast.persistence.repositories.FactureLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactureLineService {

    @Autowired
    private FactureLineRepository factureLineRepository;

    public FactureLine saveFactureLine(FactureLine factureLine){
        return factureLineRepository.save(factureLine);
    }

    public void deleteFactureLine(Long factureLineId){
         factureLineRepository.delete(factureLineId);
    }

    public FactureLine findById(Long id){
        return factureLineRepository.findOne(id);
    }

    public List<FactureLine> findByFacture(Facture facture){
        return factureLineRepository.findByFacture(facture);
    }

    public void deleteAllLines(List<FactureLine> factureLines) {
        factureLines.forEach(factureLine -> {
            this.factureLineRepository.delete(factureLine);
        });
    }
}
