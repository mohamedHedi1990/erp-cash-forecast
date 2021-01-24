package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.BonLivraison;
import org.apac.erp.cach.forecast.persistence.entities.BonLivraisonLine;
import org.apac.erp.cach.forecast.persistence.repositories.BonLivraisonLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class BonLivraisonLineService {

    @Autowired
    private BonLivraisonLineRepository bonLivraisonLineRepository;

    public BonLivraisonLine saveBonLivraisonLine(BonLivraisonLine bonLivraisonLine){
        return bonLivraisonLineRepository.save(bonLivraisonLine);
    }

    public void deleteBonLivraisonLine(Long bonLivraisonLineId){
         bonLivraisonLineRepository.delete(bonLivraisonLineId);
    }

    public BonLivraisonLine findById(Long id){
        return bonLivraisonLineRepository.findOne(id);
    }

    public List<BonLivraisonLine> findByBonLivraison(BonLivraison bonLivraison){
        return bonLivraisonLineRepository.findByBonLivraison(bonLivraison);
    }

    public void deleteAllLines(List<BonLivraisonLine> bonLivraisonLines) {
        bonLivraisonLines.forEach(bonLivraisonLine -> {
            this.bonLivraisonLineRepository.delete(bonLivraisonLine);
        });
    }
}
