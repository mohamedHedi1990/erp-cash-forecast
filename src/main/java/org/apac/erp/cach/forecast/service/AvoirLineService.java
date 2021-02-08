package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.Avoir;
import org.apac.erp.cach.forecast.persistence.entities.AvoirLine;
import org.apac.erp.cach.forecast.persistence.repositories.AvoirLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvoirLineService {

    @Autowired
    private AvoirLineRepository avoirLineRepository;

    public AvoirLine saveAvoirLine(AvoirLine avoirLine){
        return avoirLineRepository.save(avoirLine);
    }

    public void deleteAvoirLine(Long avoirLineId){
         avoirLineRepository.delete(avoirLineId);
    }

    public AvoirLine findById(Long id){
        return avoirLineRepository.findOne(id);
    }

    public List<AvoirLine> findByAvoir(Avoir avoir){
        return avoirLineRepository.findByAvoir(avoir);
    }

    public void deleteAllLines(List<AvoirLine> avoirLines) {
        avoirLines.forEach(avoirLine -> {
            this.avoirLineRepository.delete(avoirLine);
        });
    }
}
