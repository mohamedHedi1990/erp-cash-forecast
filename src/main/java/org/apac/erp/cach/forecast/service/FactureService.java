package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.*;
import org.apac.erp.cach.forecast.persistence.repositories.FactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FactureService {

    @Autowired
    FactureRepository factureRepository;
    @Autowired
    FactureLineService factureLineService;
    @Autowired
    CustomerInvoiceService customerInvoiceService;
    @Autowired
    DevisService devisService;

    @Autowired
    BonLivraisonService bonLivraisonService;

    public List<Facture>getAllInvoices()
   {
    return factureRepository.findAll();
   }

   public Facture getInvoiceById(Long id)
   {
    return factureRepository.findOne(id);
   }


    public Facture genererFactureFromBL(List<Long> blsIds)   {    Facture facturegenerer=new Facture();
        facturegenerer.setTotalHTBrut(0d);
        facturegenerer.setRemise(0d);
        facturegenerer.setTotalHT(0d);
        facturegenerer.setTotalTVA(0d);
        facturegenerer.setTotalFodec(0d);
        facturegenerer.setTotalTaxe(0d);
        facturegenerer.setTimbreFiscal(0d);
        facturegenerer.setTotalTTC(0d);
        List<BonLivraison> bonLivraisons=bonLivraisonService.findByBonLivraisonIds(blsIds);
        List<FactureLine> factureLines=new ArrayList<>();
        bonLivraisons.forEach(bonLivraison -> {
            if(facturegenerer.getCustomer() == null){
                facturegenerer.setCustomer(bonLivraison.getCustomer());
            }
            if(facturegenerer.getFactureCurrency() == null){
                facturegenerer.setFactureCurrency(bonLivraison.getBonLivraisonCurrency());
            }
            if(facturegenerer.getFactureDeadlineDate() == null){
                Calendar c=Calendar.getInstance();
                c.add(Calendar.DATE,30);
                facturegenerer.setFactureDeadlineDate(c.getTime());
            }
            if(facturegenerer.getFactureDeadlineInNumberOfDays() == null){
                facturegenerer.setFactureDeadlineInNumberOfDays(30);
            }
            facturegenerer.setTotalHTBrut(facturegenerer.getTotalHTBrut()+bonLivraison.getTotalHTBrut());
            facturegenerer.setRemise(facturegenerer.getRemise()+bonLivraison.getRemise());
            facturegenerer.setTotalHT(facturegenerer.getTotalHT()+bonLivraison.getTotalHT());
            facturegenerer.setTotalTVA(facturegenerer.getTotalTVA()+bonLivraison.getTotalTVA());
            facturegenerer.setTotalFodec(facturegenerer.getTotalFodec()+bonLivraison.getTotalFodec());
            facturegenerer.setTotalTaxe(facturegenerer.getTotalTaxe()+bonLivraison.getTotalTaxe());
            facturegenerer.setTimbreFiscal(facturegenerer.getTimbreFiscal()+bonLivraison.getTimbreFiscal());
            facturegenerer.setTotalTTC(facturegenerer.getTotalTTC()+bonLivraison.getTotalTTC());
            facturegenerer.setFactureDate(new Date());
            factureLines.addAll(this.blLinesToFactureLines(bonLivraison.getBonLivraisonLines()));
        });
        facturegenerer.setFactureLines(factureLines);
        final DateFormat df = new SimpleDateFormat("yyyy");

        Facture savedFacture=factureRepository.save(facturegenerer);
        CustomerInvoice customerInvoice=this.genererCustomerInvoiceFromFacture(savedFacture);
        savedFacture.setInvoiceCustomerId(customerInvoice.getInvoiceId());
       if(savedFacture != null){
            String year=df.format(savedFacture.getFactureDate());
            Long id=savedFacture.getFactureId();
            String ids="";
            if(id<10){
                ids="0"+String.valueOf(id);
            }else{
                ids=String.valueOf(id);
            }
            savedFacture.setFactureNumber("Fact-"+year+"-"+ids);
            savedFacture.getFactureLines().forEach(factureLine -> {
                factureLine.setFacture(savedFacture);
                factureLineService.saveFactureLine(factureLine);
            });
        Facture savedFact=factureRepository.save(facturegenerer);
        customerInvoice.setInvoiceNumber(savedFacture.getFactureNumber());
        this.customerInvoiceService.saveCustomerInvoice(customerInvoice);
        bonLivraisons.forEach(bonLivraison -> {
            bonLivraisonService.deleteBonLivraisonById(bonLivraison.getBonLivraisonId());
        });
        return savedFact;
        }else {
            return null;
        }
   }

    public Facture genererFactureFromDevis(Long devisId)
    {    Facture facturegenerer=new Facture();
        facturegenerer.setTotalHTBrut(0d);
        facturegenerer.setRemise(0d);
        facturegenerer.setTotalHT(0d);
        facturegenerer.setTotalTVA(0d);
        facturegenerer.setTotalFodec(0d);
        facturegenerer.setTotalTaxe(0d);
        facturegenerer.setTimbreFiscal(0d);
        facturegenerer.setTotalTTC(0d);
        Devis devis=devisService.devisRepository.findOne(devisId);
        List<FactureLine> factureLines=new ArrayList<>();
        if(facturegenerer.getCustomer() == null){
            facturegenerer.setCustomer(devis.getCustomer());
        }
        if(facturegenerer.getFactureCurrency() == null){
            facturegenerer.setFactureCurrency(devis.getDevisCurrency());
        }
        if(facturegenerer.getFactureDeadlineDate() == null){
            Calendar c=Calendar.getInstance();
            c.add(Calendar.DATE,30);
            facturegenerer.setFactureDeadlineDate(c.getTime());
        }
        if(facturegenerer.getFactureDeadlineInNumberOfDays() == null){
            facturegenerer.setFactureDeadlineInNumberOfDays(30);
        }
        facturegenerer.setTotalHTBrut(facturegenerer.getTotalHTBrut()+devis.getTotalHTBrut());
        facturegenerer.setRemise(facturegenerer.getRemise()+devis.getRemise());
        facturegenerer.setTotalHT(facturegenerer.getTotalHT()+devis.getTotalHT());
        facturegenerer.setTotalTVA(facturegenerer.getTotalTVA()+devis.getTotalTVA());
        facturegenerer.setTotalFodec(facturegenerer.getTotalFodec()+devis.getTotalFodec());
        facturegenerer.setTotalTaxe(facturegenerer.getTotalTaxe()+devis.getTotalTaxe());
        facturegenerer.setTimbreFiscal(facturegenerer.getTimbreFiscal()+devis.getTimbreFiscal());
        facturegenerer.setTotalTTC(facturegenerer.getTotalTTC()+devis.getTotalTTC());
        facturegenerer.setFactureDate(new Date());
        factureLines.addAll(this.devisLinesToFactureLines(devis.getDevisLines()));

        facturegenerer.setFactureLines(factureLines);
        final DateFormat df = new SimpleDateFormat("yyyy");

        Facture savedFacture=factureRepository.save(facturegenerer);
        CustomerInvoice customerInvoice=this.genererCustomerInvoiceFromFacture(savedFacture);
        savedFacture.setInvoiceCustomerId(customerInvoice.getInvoiceId());
        if(savedFacture != null){
            String year=df.format(savedFacture.getFactureDate());
            Long id=savedFacture.getFactureId();
            String ids="";
            if(id<10){
                ids="0"+String.valueOf(id);
            }else{
                ids=String.valueOf(id);
            }
            savedFacture.setFactureNumber("Fact-"+year+"-"+ids);
            savedFacture.getFactureLines().forEach(factureLine -> {
                factureLine.setFacture(savedFacture);
                factureLineService.saveFactureLine(factureLine);
            });
            Facture savedFact=factureRepository.save(facturegenerer);
            customerInvoice.setInvoiceNumber(savedFacture.getFactureNumber());
            this.customerInvoiceService.saveCustomerInvoice(customerInvoice);
            devisService.deleteDevisById(devis.getDevisId());
            return savedFact;
        }else {
            return null;
        }
    }

    public Facture saveFacture(Facture facture)
    {
       CustomerInvoice customerInvoice=createCustomerInvoiceFromFacture(facture);

       if(facture.getFactureId()!=null)
       {
           if(facture.getInvoiceCustomerId()!=null)
           {
               customerInvoice.setInvoiceId(facture.getInvoiceCustomerId());
           }
       }

       CustomerInvoice customerInvoiceSaved=customerInvoiceService.saveCustomerInvoice(customerInvoice);
       facture.setInvoiceCustomerId(customerInvoiceSaved.getInvoiceId());

        Facture savedFacture=factureRepository.save(facture);
        if(savedFacture != null && (savedFacture.getFactureNumber() == null || savedFacture.getFactureNumber().equals(""))){
            final DateFormat df = new SimpleDateFormat("yyyy");
            String year=df.format(savedFacture.getFactureDate());
            Long id=savedFacture.getFactureId();
            String ids="";
            if(id<10){
                ids="0"+String.valueOf(id);
            }else{
                ids=String.valueOf(id);
        }
        savedFacture.setFactureNumber("Fact-"+year+"-"+ids);
        Facture savedFact=factureRepository.save(savedFacture);
        if(savedFact.getFactureLines() != null){
            savedFact.getFactureLines().forEach(factureLine -> {
                factureLine.setFacture(savedFact);
                factureLineService.saveFactureLine(factureLine);
            });
        }
        }
        return savedFacture;

    }


    private List<FactureLine> blLinesToFactureLines(List<BonLivraisonLine> bonLivraisonLines) {
        ArrayList<FactureLine> factureLines=new ArrayList<>();
        bonLivraisonLines.forEach(bonLivraisonLine -> {
            FactureLine fl=new FactureLine();
            fl.setMontantFaudec(bonLivraisonLine.getMontantFaudec());
            fl.setMontantHt(bonLivraisonLine.getMontantHt());
            fl.setMontantHtBrut(bonLivraisonLine.getMontantHtBrut());
            fl.setMontantHtBrut(bonLivraisonLine.getMontantHtBrut());
            fl.setMontantTva(bonLivraisonLine.getMontantTva());
            fl.setProduct(bonLivraisonLine.getProduct());
            fl.setQuantity(bonLivraisonLine.getQuantity());
            fl.setRemiseTaux(bonLivraisonLine.getRemiseTaux());
            fl.setRemiseValeur(bonLivraisonLine.getRemiseValeur());
            factureLines.add(fl);
        });
        return factureLines;
    }

    private CustomerInvoice genererCustomerInvoiceFromFacture(Facture facture) {
      CustomerInvoice customerInvoice=new CustomerInvoice();
      customerInvoice.setInvoiceNumber(facture.getFactureNumber());
      customerInvoice.setCustomer(facture.getCustomer());
      customerInvoice.setInvoiceCurrency(facture.getFactureCurrency());
      customerInvoice.setInvoiceDate(facture.getFactureDate());
      customerInvoice.setInvoiceDeadlineDate(facture.getFactureDeadlineDate());
      customerInvoice.setInvoiceDeadlineInNumberOfDays(facture.getFactureDeadlineInNumberOfDays());
      customerInvoice.setInvoiceTotalAmount(facture.getTotalTTC());
      customerInvoice.setInvoicePayment(0D);
      customerInvoice.setIsFacture(true);

        if(facture.getFactureId()!=null)
        {
            if(facture.getInvoiceCustomerId()!=null)
            {
                customerInvoice.setInvoiceId(facture.getInvoiceCustomerId());
            }
        }

        customerInvoice=customerInvoiceService.saveCustomerInvoice(customerInvoice);
      return  customerInvoice;
    }

    private CustomerInvoice createCustomerInvoiceFromFacture(Facture facture) {
        CustomerInvoice customerInvoice=new CustomerInvoice();
        customerInvoice.setInvoiceNumber(facture.getFactureNumber());
        customerInvoice.setCustomer(facture.getCustomer());
        customerInvoice.setInvoiceCurrency(facture.getFactureCurrency());
        customerInvoice.setInvoiceDate(facture.getFactureDate());
        customerInvoice.setInvoiceDeadlineDate(facture.getFactureDeadlineDate());
        customerInvoice.setInvoiceDeadlineInNumberOfDays(facture.getFactureDeadlineInNumberOfDays());
        customerInvoice.setInvoiceTotalAmount(facture.getTotalTTC());
        customerInvoice.setInvoicePayment(0D);
        customerInvoice.setIsFacture(true);
        return  customerInvoice;
    }

    public void deleteFactureById(Long id)
   {   Facture facture=this.factureRepository.findOne(id);
   if(facture != null && facture.getFactureLines() != null){
       this.factureLineService.deleteAllLines(facture.getFactureLines());
   } /*if(facture.getInvoiceCustomerId() != null) {
       customerInvoiceService.deleteCustomerInvoice(facture.getInvoiceCustomerId());
   }*/
       factureRepository.delete(id);
   }

    public void deleteAllFacture()
   {
       factureRepository.deleteAll();
   }

    private List<FactureLine> devisLinesToFactureLines(List<DevisLine> devisLines) {
        ArrayList<FactureLine> factureLines=new ArrayList<>();
        devisLines.forEach(devisLine -> {
            FactureLine fl=new FactureLine();
            fl.setMontantFaudec(devisLine.getMontantFaudec());
            fl.setMontantHt(devisLine.getMontantHt());
            fl.setMontantHtBrut(devisLine.getMontantHtBrut());
            fl.setMontantHtBrut(devisLine.getMontantHtBrut());
            fl.setMontantTva(devisLine.getMontantTva());
            fl.setProduct(devisLine.getProduct());
            fl.setQuantity(devisLine.getQuantity());
            fl.setRemiseTaux(devisLine.getRemiseTaux());
            fl.setRemiseValeur(devisLine.getRemiseValeur());
            factureLines.add(fl);
        });
        return factureLines;
    }
}
