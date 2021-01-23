package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.GeneratedInvoice;
import org.apac.erp.cach.forecast.persistence.entities.GeneratedInvoiceLine;
import org.apac.erp.cach.forecast.persistence.repositories.GeneratedInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class GeneratedInvoiceService {
    @Autowired
    GeneratedInvoiceRepository generatedInvoiceRepository;
    @Autowired
    GeneratedInvoiceLineService generatedInvoiceLineService;

    public List<GeneratedInvoice>getAllInvoices()
   {
    return generatedInvoiceRepository.findAll();
   }

   public GeneratedInvoice getInvoiceById(Long id)
   {
    return generatedInvoiceRepository.findOne(id);
   }

   public GeneratedInvoice saveGeneratedInvoice(GeneratedInvoice generatedInvoice)
   {
       GeneratedInvoice savedGeneratedInvoice=generatedInvoiceRepository.save(generatedInvoice);
       if(savedGeneratedInvoice.getGeneratedInvoiceLines() != null){
           savedGeneratedInvoice.getGeneratedInvoiceLines().forEach(generatedInvoiceLine -> {
               generatedInvoiceLine.setGeneratedInvoice(savedGeneratedInvoice);
               generatedInvoiceLineService.saveGeneratedInvoiceLine(generatedInvoiceLine);
           });
       }
       return generatedInvoiceRepository.save(generatedInvoice);
   }

   public void deleteGeneratedInvoiceById(Long id)
   {   GeneratedInvoice generatedInvoice=this.generatedInvoiceRepository.findOne(id);
   if(generatedInvoice != null && generatedInvoice.getGeneratedInvoiceLines() != null){
       this.generatedInvoiceLineService.deleteAllLines(generatedInvoice.getGeneratedInvoiceLines());
   }
       generatedInvoiceRepository.delete(id);
   }

    public void deleteAllGeneratedInvoice()
   {
       generatedInvoiceRepository.deleteAll();
   }
}
