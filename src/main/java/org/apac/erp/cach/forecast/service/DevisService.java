package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.Devis;
import org.apac.erp.cach.forecast.persistence.repositories.DevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class DevisService {
    @Autowired
    DevisRepository devisRepository;
    @Autowired
    DevisLineService devisLineService;
    @Autowired
    CustomerInvoiceService customerInvoiceService;

    public List<Devis>getAllInvoices()
   {
    return devisRepository.findAll();
   }

   public Devis getInvoiceById(Long id)
   {
    return devisRepository.findOne(id);
   }
   public Devis saveDevis(Devis devis)
   {
       Devis savedDevis=devisRepository.save(devis);
       if((savedDevis != null )&& (savedDevis.getDevisNumber() == null || savedDevis.getDevisNumber().equals(""))){
           final DateFormat df = new SimpleDateFormat("yyyy");
           String year=df.format(savedDevis.getDevisDate());
           Long id=savedDevis.getDevisId();
           String ids="";
           if(id<10){
               ids="0"+String.valueOf(id);
           }else{
               ids=String.valueOf(id);
           }
           savedDevis.setDevisNumber("DEV-"+year+"-"+ids);
       }
       Devis savedDev=devisRepository.save(savedDevis);
       if(savedDevis.getDevisLines() != null){
           savedDevis.getDevisLines().forEach(devisLine -> {
               devisLine.setDevis(savedDev);
               devisLineService.saveDevisLine(devisLine);
           });
       }
       return savedDevis;
   }

    public void deleteDevisById(Long id)
   {   Devis devis=this.devisRepository.findOne(id);
   if(devis != null && devis.getDevisLines() != null){
       this.devisLineService.deleteAllLines(devis.getDevisLines());
   } 
   devisRepository.delete(id);
   }

    public void deleteAllDevis()
   {
       devisRepository.deleteAll();
   }

    public List<Devis> findByDevisIds(List<Long> blsIds) {
        return devisRepository.findByDevisIdIn(blsIds);
    }
}
