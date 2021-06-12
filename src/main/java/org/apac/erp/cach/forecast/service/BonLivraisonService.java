package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.*;
import org.apac.erp.cach.forecast.persistence.repositories.BonLivraisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BonLivraisonService {
    @Autowired
    BonLivraisonRepository bonLivraisonRepository;
    @Autowired
    BonLivraisonLineService bonLivraisonLineService;
    @Autowired
    CustomerInvoiceService customerInvoiceService;
    @Autowired
    DevisService devisService;

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

    public BonLivraison genererBLFromDevis(Long devisId)
    {    BonLivraison blgenerer=new BonLivraison();
        blgenerer.setTotalHTBrut(0d);
        blgenerer.setRemise(0d);
        blgenerer.setTotalHT(0d);
        blgenerer.setTotalTVA(0d);
        blgenerer.setTotalFodec(0d);
        blgenerer.setTotalTaxe(0d);
        blgenerer.setTimbreFiscal(0d);
        blgenerer.setTotalTTC(0d);
        Devis devis=devisService.devisRepository.findOne(devisId);
        List<BonLivraisonLine> bonLivraisonLines=new ArrayList<>();
        if(blgenerer.getCustomer() == null){
            blgenerer.setCustomer(devis.getCustomer());
        }
        if(blgenerer.getBonLivraisonCurrency() == null){
            blgenerer.setBonLivraisonCurrency(devis.getDevisCurrency());
        }
        blgenerer.setCommercialName(devis.getCommercialName());
        /*if(facturegenerer.getFactureDeadlineDate() == null){
            Calendar c=Calendar.getInstance();
            c.add(Calendar.DATE,30);
            facturegenerer.setFactureDeadlineDate(c.getTime());
        }
        if(facturegenerer.getFactureDeadlineInNumberOfDays() == null){
            facturegenerer.setFactureDeadlineInNumberOfDays(30);
        }*/
        blgenerer.setTotalHTBrut(blgenerer.getTotalHTBrut()+devis.getTotalHTBrut());
        blgenerer.setRemise(blgenerer.getRemise()+devis.getRemise());
        blgenerer.setTotalHT(blgenerer.getTotalHT()+devis.getTotalHT());
        blgenerer.setTotalTVA(blgenerer.getTotalTVA()+devis.getTotalTVA());
        blgenerer.setTotalFodec(blgenerer.getTotalFodec()+devis.getTotalFodec());
        blgenerer.setTotalTaxe(blgenerer.getTotalTaxe()+devis.getTotalTaxe());
        blgenerer.setTimbreFiscal(blgenerer.getTimbreFiscal()+devis.getTimbreFiscal());
        blgenerer.setTotalTTC(blgenerer.getTotalTTC()+devis.getTotalTTC());
        blgenerer.setBonLivraisonDate(new Date());
        bonLivraisonLines.addAll(this.devisLinesToBLLines(devis.getDevisLines()));

        blgenerer.setBonLivraisonLines(bonLivraisonLines);
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
        blgenerer.setBonLivraisonNumber(blNumber);

        BonLivraison savedBonLivraison = bonLivraisonRepository.save(blgenerer);
        if (savedBonLivraison.getBonLivraisonLines() != null) {
            savedBonLivraison.getBonLivraisonLines().forEach(bonLivraisonLine -> {
                bonLivraisonLine.setBonLivraison(savedBonLivraison);
                bonLivraisonLineService.saveBonLivraisonLine(bonLivraisonLine);
            });
        }
        return savedBonLivraison;
    }

    private List<BonLivraisonLine> devisLinesToBLLines(List<DevisLine> devisLines) {
        ArrayList<BonLivraisonLine> bonLivraisonLines=new ArrayList<>();
        devisLines.forEach(devisLine -> {
            BonLivraisonLine blLine=new BonLivraisonLine();
            blLine.setMontantFaudec(devisLine.getMontantFaudec());
            blLine.setMontantHt(devisLine.getMontantHt());
            blLine.setMontantHtBrut(devisLine.getMontantHtBrut());
            blLine.setMontantHtBrut(devisLine.getMontantHtBrut());
            blLine.setMontantTva(devisLine.getMontantTva());
            blLine.setProduct(devisLine.getProduct());
            blLine.setProductGroup(devisLine.getProductGroup());
            blLine.setQuantity(devisLine.getQuantity());
            blLine.setRemiseTaux(devisLine.getRemiseTaux());
            blLine.setRemiseValeur(devisLine.getRemiseValeur());
            bonLivraisonLines.add(blLine);
        });
        return bonLivraisonLines;
    }


}
