package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.enumeration.FactureType;
import org.apac.erp.cach.forecast.persistence.entities.Devis;
import org.apac.erp.cach.forecast.persistence.entities.Facture;
import org.apac.erp.cach.forecast.persistence.repositories.DevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

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
   public Devis saveDevis(Devis devis) {
       if(devis.getDevisId() == null){
        final DateFormat df = new SimpleDateFormat("yyyy");
       Optional<Devis> lastDevis = devisRepository.findTopByOrderByCreatedAtDesc();
       String devisNumber = "";
       if (!lastDevis.isPresent()) {
           devisNumber = "DEV" + "-" + df.format(Calendar.getInstance().getTime()) + "-" + String.format("%04d", 1);
       } else if (!df.format(lastDevis.get().getCreatedAt()).equals(df.format(Calendar.getInstance().getTime()))) {
           devisNumber = "DEV" + "-" + df.format(Calendar.getInstance().getTime()) + "-" + String.format("%04d", 1);
       } else {
           String currentFactNumber = lastDevis.get().getDevisNumber();
           String sequanceNumber = String.format("%04d", Integer.parseInt(currentFactNumber.substring(currentFactNumber.length() - 4)) + 1);
           devisNumber = "DEV" + "-" + df.format(Calendar.getInstance().getTime()) + "-" + sequanceNumber;
       }
       devis.setDevisNumber(devisNumber);
        }
       Devis savedDevis=devisRepository.save(devis);
       if(savedDevis.getDevisLines() != null){
           savedDevis.getDevisLines().forEach(devisLine -> {
               devisLine.setDevis(savedDevis);
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

    public boolean existesByDevisNumberAndOtherId(Devis devis) {
        if(devis.getDevisId() == null  ){
            return false;
        }else {
            return this.devisRepository.findByDevisIdNotLikeAndDevisNumber(devis.getDevisId(),devis.getDevisNumber()).isPresent();
        }
    }
}
