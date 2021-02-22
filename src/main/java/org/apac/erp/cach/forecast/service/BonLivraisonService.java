package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.BonLivraison;
import org.apac.erp.cach.forecast.persistence.entities.Devis;
import org.apac.erp.cach.forecast.persistence.entities.Facture;
import org.apac.erp.cach.forecast.persistence.repositories.BonLivraisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class BonLivraisonService {
    @Autowired
    BonLivraisonRepository bonLivraisonRepository;
    @Autowired
    BonLivraisonLineService bonLivraisonLineService;
    @Autowired
    CustomerInvoiceService customerInvoiceService;

    public List<BonLivraison>getAllInvoices()
   {
    return bonLivraisonRepository.findAll();
   }

   public BonLivraison getInvoiceById(Long id)
   {
    return bonLivraisonRepository.findOne(id);
   }
   public BonLivraison saveBonLivraison(BonLivraison bonLivraison) {
       if(bonLivraison.getBonLivraisonId() == null){
       final DateFormat df = new SimpleDateFormat("yyyy");
       Optional<BonLivraison> lastBL = bonLivraisonRepository.findTopByOrderByCreatedAtDesc();
       String blNumber = "";
       if (!lastBL.isPresent()) {
           blNumber = "BL" + "-" + df.format(Calendar.getInstance().getTime()) + "-" + String.format("%04d", 1);
       } else if (!df.format(lastBL.get().getCreatedAt()).equals(df.format(Calendar.getInstance().getTime()))) {
           blNumber = "BL" + "-" + df.format(Calendar.getInstance().getTime()) + "-" + String.format("%04d", 1);
       } else {
           String currentFactNumber = lastBL.get().getBonLivraisonNumber();
           String sequanceNumber = String.format("%04d", Integer.parseInt(currentFactNumber.substring(currentFactNumber.length() - 4)) + 1);
           blNumber = "BL" + "-" + df.format(Calendar.getInstance().getTime()) + "-" + sequanceNumber;
       }
       bonLivraison.setBonLivraisonNumber(blNumber);
        }
           BonLivraison savedBonLivraison = bonLivraisonRepository.save(bonLivraison);
           if (savedBonLivraison.getBonLivraisonLines() != null) {
               savedBonLivraison.getBonLivraisonLines().forEach(bonLivraisonLine -> {
                   bonLivraisonLine.setBonLivraison(savedBonLivraison);
                   bonLivraisonLineService.saveBonLivraisonLine(bonLivraisonLine);
               });
           }
       return savedBonLivraison;

   }

    /*private CustomerInvoice createCustomerInvoiceFromBonLivraison(BonLivraison bonLivraison) {
      CustomerInvoice customerInvoice=new CustomerInvoice();
      customerInvoice.setInvoiceNumber(bonLivraison.getBonLivraisonNumber());
      customerInvoice.setCustomer(bonLivraison.getCustomer());
      customerInvoice.setInvoiceCurrency(bonLivraison.getBonLivraisonCurrency());
      customerInvoice.setInvoiceDate(bonLivraison.getBonLivraisonDate());
      customerInvoice.setInvoiceDeadlineDate(bonLivraison.getBonLivraisonDeadlineDate());
      customerInvoice.setInvoiceDeadlineInNumberOfDays(bonLivraison.getBonLivraisonDeadlineInNumberOfDays());
      customerInvoice.setInvoiceTotalAmount(bonLivraison.getTotalTTC());
      customerInvoice.setInvoicePayment(0D);
      customerInvoice.setIsBonLivraison(true);
      return  customerInvoice;
    }*/

    public void deleteBonLivraisonById(Long id)
   {   BonLivraison bonLivraison=this.bonLivraisonRepository.findOne(id);
   if(bonLivraison != null && bonLivraison.getBonLivraisonLines() != null){
       this.bonLivraisonLineService.deleteAllLines(bonLivraison.getBonLivraisonLines());
   } /*if(bonLivraison.getInvoiceCustomerId() != null) {
       customerInvoiceService.deleteCustomerInvoice(bonLivraison.getInvoiceCustomerId());
   }*/
       bonLivraisonRepository.delete(id);
   }

    public void deleteAllBonLivraison()
   {
       bonLivraisonRepository.deleteAll();
   }

    public List<BonLivraison> findByBonLivraisonIds(List<Long> blsIds) {
        return bonLivraisonRepository.findByBonLivraisonIdIn(blsIds);
    }

    public boolean existesByBonLivaisonNumberAndOtherId(BonLivraison bonLivraison) {
        if(bonLivraison.getBonLivraisonId() == null  ){
            return false;
        }else {
            return this.bonLivraisonRepository.findByBonLivraisonIdNotLikeAndBonLivraisonNumber(bonLivraison.getBonLivraisonId(),bonLivraison.getBonLivraisonNumber()).isPresent();
        }
    }
}
