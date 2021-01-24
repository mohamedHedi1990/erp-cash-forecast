package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.persistence.entities.BonLivraison;
import org.apac.erp.cach.forecast.persistence.entities.BonLivraisonLine;
import org.apac.erp.cach.forecast.persistence.repositories.BonLivraisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
   public BonLivraison saveBonLivraison(BonLivraison bonLivraison)
   {
       CustomerInvoice customerInvoice=createCustomerInvoiceFromBonLivraison(bonLivraison);

       if(bonLivraison.getBonLivraisonId()!=null)
       {
           if(bonLivraison.getInvoiceCustomerId()!=null)
           {
               customerInvoice.setInvoiceId(bonLivraison.getInvoiceCustomerId());
           }
       }

       CustomerInvoice customerInvoiceSaved=customerInvoiceService.saveCustomerInvoice(customerInvoice);
       bonLivraison.setInvoiceCustomerId(customerInvoiceSaved.getInvoiceId());
       BonLivraison savedBonLivraison=bonLivraisonRepository.save(bonLivraison);
       if(savedBonLivraison.getBonLivraisonLines() != null){
           savedBonLivraison.getBonLivraisonLines().forEach(bonLivraisonLine -> {
               bonLivraisonLine.setBonLivraison(savedBonLivraison);
               bonLivraisonLineService.saveBonLivraisonLine(bonLivraisonLine);
           });
       }
       return savedBonLivraison;

   }

    private CustomerInvoice createCustomerInvoiceFromBonLivraison(BonLivraison bonLivraison) {
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
    }

    public void deleteBonLivraisonById(Long id)
   {   BonLivraison bonLivraison=this.bonLivraisonRepository.findOne(id);
   if(bonLivraison != null && bonLivraison.getBonLivraisonLines() != null){
       this.bonLivraisonLineService.deleteAllLines(bonLivraison.getBonLivraisonLines());
   } if(bonLivraison.getInvoiceCustomerId() != null) {
       customerInvoiceService.deleteCustomerInvoice(bonLivraison.getInvoiceCustomerId());
   }
       bonLivraisonRepository.delete(id);
   }

    public void deleteAllBonLivraison()
   {
       bonLivraisonRepository.deleteAll();
   }
}
