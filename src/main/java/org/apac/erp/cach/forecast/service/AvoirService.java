package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.*;
import org.apac.erp.cach.forecast.persistence.repositories.AvoirRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AvoirService {

    @Autowired
    AvoirRepository avoirRepository;
    @Autowired
    AvoirLineService avoirLineService;
    @Autowired
    CustomerInvoiceService customerInvoiceService;

    @Autowired
    FactureService factureService;

    public List<Avoir>getAllInvoices()
   {
    return avoirRepository.findAll();
   }

   public Avoir getInvoiceById(Long id)
   {
    return avoirRepository.findOne(id);
   }

    public Avoir genererAvoirFromFacture(Long factureId)
    {    Avoir avoirgenerer=new Avoir();
        avoirgenerer.setTotalHTBrut(0d);
        avoirgenerer.setRemise(0d);
        avoirgenerer.setTotalHT(0d);
        avoirgenerer.setTotalTVA(0d);
        avoirgenerer.setTotalFodec(0d);
        avoirgenerer.setTotalTaxe(0d);
        avoirgenerer.setTimbreFiscal(0d);
        avoirgenerer.setTotalTTC(0d);
        Facture facture=factureService.factureRepository.findOne(factureId);
        List<AvoirLine> avoirLines=new ArrayList<>();
        if(avoirgenerer.getCustomer() == null){
            avoirgenerer.setCustomer(facture.getCustomer());
        }
        avoirgenerer.setCommercialName(facture.getCommercialName());
        if(avoirgenerer.getAvoirCurrency() == null){
            avoirgenerer.setAvoirCurrency(facture.getFactureCurrency());
        }
        avoirgenerer.setTotalHTBrut(0D);
        //avoirgenerer.setRemise(facture.getTotalHTBrut()*(-1));
        avoirLines.addAll(this.factureLinesToAvoirLines(facture.getFactureLines()));
        avoirLines.forEach(avoirLine -> {
            avoirgenerer.setTotalTVA(avoirgenerer.getTotalTVA()+avoirLine.getMontantTva());
            avoirgenerer.setTotalFodec(avoirgenerer.getTotalFodec()+avoirLine.getMontantFaudec());
            avoirgenerer.setTotalHT(avoirgenerer.getTotalHT()+avoirLine.getMontantHt());
        });
        avoirgenerer.setTimbreFiscal(facture.getTimbreFiscal());
        avoirgenerer.setTotalTaxe(avoirgenerer.getTotalTVA()+avoirgenerer.getTotalFodec()+avoirgenerer.getTimbreFiscal());
        avoirgenerer.setTotalTTC(avoirgenerer.getTotalTaxe()+avoirgenerer.getTotalHT());
        avoirgenerer.setAvoirDate(new Date());
        avoirgenerer.setAvoirLines(avoirLines);
        avoirgenerer.setRemise(avoirgenerer.getTotalHT()*(-1));
        final DateFormat df = new SimpleDateFormat("yyyy");

        Avoir savedAvoir=avoirRepository.save(avoirgenerer);
        if(savedAvoir != null){
            String year=df.format(savedAvoir.getAvoirDate());
            Long id=savedAvoir.getAvoirId();
            String ids="";
            if(id<10){
                ids="0"+String.valueOf(id);
            }else{
                ids=String.valueOf(id);
            }
            savedAvoir.setAvoirNumber("FV-"+year+"-"+ids);
            savedAvoir.getAvoirLines().forEach(avoirLine -> {
                avoirLine.setAvoir(savedAvoir);
                avoirLineService.saveAvoirLine(avoirLine);
            });
            Avoir savedFact=avoirRepository.save(avoirgenerer);
            // customerInvoiceService.deleteCustomerInvoice(facture.getInvoiceCustomerId());
            // factureService.deleteFactureById(facture.getFactureId());
            return savedFact;
        }else {
            return null;
        }
    }


    public void deleteAvoirById(Long id)
   {   Avoir avoir=this.avoirRepository.findOne(id);
   if(avoir != null && avoir.getAvoirLines() != null){
       this.avoirLineService.deleteAllLines(avoir.getAvoirLines());
   } 
       avoirRepository.delete(id);
   }

    public void deleteAllAvoir()
   {
       avoirRepository.deleteAll();
   }


    private List<AvoirLine> factureLinesToAvoirLines(List<FactureLine> factureLines) {
        ArrayList<AvoirLine> avoirLines=new ArrayList<>();
        factureLines.forEach(factureLine -> {
            AvoirLine avoirLine=new AvoirLine();
            avoirLine.setMontantHt(factureLine.getMontantHt());
            avoirLine.setMontantFaudec(avoirLine.getMontantHt()*factureLine.getProduct().getProductFodec()/100);
            avoirLine.setMontantTva((avoirLine.getMontantFaudec()+avoirLine.getMontantHt())*factureLine.getProduct().getProductTVA()/100);
            avoirLine.setProduct(factureLine.getProduct());
            avoirLine.setQuantity(factureLine.getQuantity());
            avoirLine.setProductGroup(factureLine.getProductGroup());
            avoirLines.add(avoirLine);
        });
        return avoirLines;
    }
}
