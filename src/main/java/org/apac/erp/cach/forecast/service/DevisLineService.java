package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.Devis;
import org.apac.erp.cach.forecast.persistence.entities.DevisLine;
import org.apac.erp.cach.forecast.persistence.repositories.DevisLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DevisLineService {

    @Autowired
    private DevisLineRepository devisLineRepository;

    public DevisLine saveDevisLine(DevisLine devisLine){
        return devisLineRepository.save(devisLine);
    }

    public void deleteDevisLine(Long devisLineId){
         devisLineRepository.delete(devisLineId);
    }

    public DevisLine findById(Long id){
        return devisLineRepository.findOne(id);
    }

    public List<DevisLine> findByDevis(Devis devis){
        return devisLineRepository.findByDevis(devis);
    }

    public void deleteAllLines(List<DevisLine> devisLines) {
        devisLines.forEach(devisLine -> {
            this.devisLineRepository.delete(devisLine);
        });
    }
}
