package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.GeneratedInvoice;
import org.apac.erp.cach.forecast.persistence.entities.GeneratedInvoiceLine;
import org.apac.erp.cach.forecast.persistence.repositories.GeneratedInvoiceLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class GeneratedInvoiceLineService {

    @Autowired
    private GeneratedInvoiceLineRepository generatedInvoiceLineRepository;

    public GeneratedInvoiceLine saveGeneratedInvoiceLine(GeneratedInvoiceLine generatedInvoiceLine){
        return generatedInvoiceLineRepository.save(generatedInvoiceLine);
    }

    public void deleteGeneratedInvoiceLine(Long generatedInvoiceLineId){
         generatedInvoiceLineRepository.delete(generatedInvoiceLineId);
    }

    public GeneratedInvoiceLine findById(Long id){
        return generatedInvoiceLineRepository.findOne(id);
    }

    public List<GeneratedInvoiceLine> findByGeneratedInvoice(GeneratedInvoice generatedInvoice){
        return generatedInvoiceLineRepository.findByGeneratedInvoice(generatedInvoice);
    }

    public void deleteAllLines(Set<GeneratedInvoiceLine> generatedInvoiceLines) {
        generatedInvoiceLines.forEach(generatedInvoiceLine -> {
            this.generatedInvoiceLineRepository.delete(generatedInvoiceLine);
        });
    }
}
