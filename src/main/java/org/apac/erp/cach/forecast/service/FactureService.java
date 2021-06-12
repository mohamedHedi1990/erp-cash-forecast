package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.dtos.FactureFilterDto;
import org.apac.erp.cach.forecast.dtos.FactureProductDto;
import org.apac.erp.cach.forecast.enumeration.FactureType;
import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.enumeration.RsTypeSaisie;
import org.apac.erp.cach.forecast.persistence.entities.*;
import org.apac.erp.cach.forecast.persistence.repositories.BonLivraisonRepository;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerInvoiceRepository;
import org.apac.erp.cach.forecast.persistence.repositories.FactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FactureService {

    @Autowired
    FactureRepository factureRepository;
    @Autowired
    BonLivraisonRepository bonLivraisonRepository;
    @Autowired
    FactureLineService factureLineService;
    @Autowired
    CustomerInvoiceService customerInvoiceService;
    @Autowired
    DevisService devisService;

    @Autowired
    BonLivraisonService bonLivraisonService;

    @Autowired
    BonLivraisonLineService bonLivraisonLineService;
    
    @Autowired
    private CustomerInvoiceRepository customerInvoiceRepo;

    public List<Facture>getAllInvoices()
   {
    return factureRepository.findAllByOrderByFactureNumberDesc();
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
        facturegenerer.setFactureType(FactureType.FACTURE);
        List<BonLivraison> bonLivraisons=bonLivraisonService.findByBonLivraisonIds(blsIds);
        List<FactureLine> factureLines=new ArrayList<>();
        bonLivraisons.forEach(bonLivraison -> {
            if(facturegenerer.getCustomer() == null){
                facturegenerer.setCustomer(bonLivraison.getCustomer());
            }
            if(facturegenerer.getFactureCurrency() == null){
                facturegenerer.setFactureCurrency(bonLivraison.getBonLivraisonCurrency());
            }
            if(bonLivraison.getCommercialName() != null && bonLivraison.getCommercialName() != ""){
                facturegenerer.setCommercialName(bonLivraison.getCommercialName());
            }
            /*if(facturegenerer.getFactureDeadlineDate() == null){
                Calendar c=Calendar.getInstance();
                c.add(Calendar.DATE,30);
                facturegenerer.setFactureDeadlineDate(c.getTime());
            }
            if(facturegenerer.getFactureDeadlineInNumberOfDays() == null){
                facturegenerer.setFactureDeadlineInNumberOfDays(30);
            }*/
            facturegenerer.setFactureCondition("");
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
        Optional<Facture> lastFacture=factureRepository.findTopByFactureTypeOrderByCreatedAtDesc(FactureType.FACTURE);
        String factureNumber="";
        if(! lastFacture.isPresent()) {
            factureNumber="FACT" + "-" + df.format(Calendar.getInstance().getTime())+ "-" +String.format("%04d", 1);
        }else if(! df.format(lastFacture.get().getCreatedAt()).equals(df.format(Calendar.getInstance().getTime()))){
            factureNumber="FACT" + "-" + df.format(Calendar.getInstance().getTime())+ "-" +String.format("%04d", 1);
        }else{
            String currentFactNumber=lastFacture.get().getFactureNumber();
            String sequanceNumber=String.format("%04d",Integer.parseInt(currentFactNumber.substring(currentFactNumber.length()-4))+1);
            factureNumber="FACT" + "-" + df.format(Calendar.getInstance().getTime())+ "-" +sequanceNumber;
        }
        facturegenerer.setFactureNumber(factureNumber);
        Facture savedFacture=factureRepository.save(facturegenerer);
        CustomerInvoice customerInvoice=this.genererCustomerInvoiceFromFacture(savedFacture);
        savedFacture.setInvoiceCustomerId(customerInvoice.getInvoiceId());
        savedFacture.getFactureLines().forEach(factureLine -> {
                factureLine.setFacture(savedFacture);
                factureLineService.saveFactureLine(factureLine);
            });
        Facture savedFact=factureRepository.save(facturegenerer);
        customerInvoice.setInvoiceNumber(savedFacture.getFactureNumber());
        customerInvoice.setIsGeneratedInvoice(true);
        this.customerInvoiceRepo.save(customerInvoice);
        bonLivraisons.forEach(bonLivraison -> {
            bonLivraisonService.deleteBonLivraisonById(bonLivraison.getBonLivraisonId());
        });
        return savedFact;

   }

    public Facture saveFacture(Facture facture) {
            CustomerInvoice customerInvoice = null;

            if (facture.getFactureId() != null) {
                if (facture.getInvoiceCustomerId() != null) {
                	customerInvoice = customerInvoiceRepo.findOne(facture.getInvoiceCustomerId());
                	customerInvoice.setInvoiceTotalAmount(facture.getTotalTTC());
                	if(customerInvoice.getInvoiceRsType() == RsTypeSaisie.POURCENTAGE) {
                		double invoiceNet = facture.getTotalTTC() - (facture.getTotalTTC() * customerInvoice.getInvoiceRs() / 100);
                		customerInvoice.setInvoiceNet(Math.round(invoiceNet * 1000.0) / 1000.0);
                	} else {
                		customerInvoice.setInvoiceNet(facture.getTotalTTC() -  customerInvoice.getInvoiceRs());
                		if(customerInvoice.getInvoiceNet() > customerInvoice.getInvoiceTotalAmount()) {
                			customerInvoice.setInvoiceNet(customerInvoice.getInvoiceTotalAmount());
                			customerInvoice.setInvoiceRs(0.0);
                			customerInvoice.setInvoiceRsType(RsTypeSaisie.POURCENTAGE);
                		}
                	}
                	customerInvoice.setInvoiceDate(facture.getFactureDate());
                    //customerInvoice.setInvoiceId(facture.getInvoiceCustomerId());
                } else { 
                	customerInvoice = createCustomerInvoiceFromFacture(facture);
                }
            } else {
            	customerInvoice =  createCustomerInvoiceFromFacture(facture);
            }

            CustomerInvoice customerInvoiceSaved = customerInvoiceRepo.save(customerInvoice);
            facture.setInvoiceCustomerId(customerInvoiceSaved.getInvoiceId());

        /*if (facture.getFactureType() == FactureType.AVOIR) {
        	facture.setFactureDate(new Date());
        	facture.setFactureDeadlineInNumberOfDays(0);
        	facture.setFactureDeadlineDate(new Date());
        }*/
        if(facture.getFactureId() == null) {
            final DateFormat df = new SimpleDateFormat("yyyy");
            if (facture.getFactureType().equals(FactureType.FACTURE)) {
                Optional<Facture> lastFacture = factureRepository.findTopByFactureTypeOrderByCreatedAtDesc(FactureType.FACTURE);
                String factureNumber = "";
                if (!lastFacture.isPresent()) {
                    factureNumber = "FACT" + "-" + df.format(Calendar.getInstance().getTime()) + "-" + String.format("%04d", 1);
                } else if (!df.format(lastFacture.get().getCreatedAt()).equals(df.format(Calendar.getInstance().getTime()))) {
                    factureNumber = "FACT" + "-" + df.format(Calendar.getInstance().getTime()) + "-" + String.format("%04d", 1);
                } else {
                    String currentFactNumber = lastFacture.get().getFactureNumber();
                    String sequanceNumber = String.format("%04d", Integer.parseInt(currentFactNumber.substring(currentFactNumber.length() - 4)) + 1);
                    factureNumber = "FACT" + "-" + df.format(Calendar.getInstance().getTime()) + "-" + sequanceNumber;
                }
                facture.setFactureNumber(factureNumber);
            } else {
                Optional<Facture> lastFacture = factureRepository.findTopByFactureTypeOrderByCreatedAtDesc(FactureType.AVOIR);
                String factureNumber = "";
                if (!lastFacture.isPresent()) {
                    factureNumber = "AV" + "-" + df.format(Calendar.getInstance().getTime()) + "-" + String.format("%04d", 1);
                } else if (!df.format(lastFacture.get().getCreatedAt()).equals(df.format(Calendar.getInstance().getTime()))) {
                    factureNumber = "AV" + "-" + df.format(Calendar.getInstance().getTime()) + "-" + String.format("%04d", 1);
                } else {
                    String currentFactNumber = lastFacture.get().getFactureNumber();
                    String sequanceNumber = String.format("%04d", Integer.parseInt(currentFactNumber.substring(currentFactNumber.length() - 4)) + 1);
                    factureNumber = "AV" + "-" + df.format(Calendar.getInstance().getTime()) + "-" + sequanceNumber;
                }
                facture.setFactureNumber(factureNumber);
            }
        }
                Facture savedFacture = factureRepository.save(facture);
                customerInvoiceSaved.setInvoiceNumber(savedFacture.getFactureNumber());
                customerInvoiceRepo.save(customerInvoiceSaved);

            Facture savedFact = factureRepository.save(savedFacture);
            if (savedFacture.getFactureLines() != null) {
                savedFacture.getFactureLines().forEach(factureLine -> {
                    factureLine.setFacture(savedFact);
                    factureLineService.saveFactureLine(factureLine);
                });
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
            fl.setProductGroup(bonLivraisonLine.getProductGroup());
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
        if(customerInvoice.getInvoiceDeadlineDate() == null) {
            Calendar c=Calendar.getInstance();
            c.add(Calendar.DATE,30);
            customerInvoice.setInvoiceDeadlineDate(c.getTime());
        }
        if(customerInvoice.getInvoiceDeadlineInNumberOfDays() == null) {
            customerInvoice.setInvoiceDeadlineInNumberOfDays(30);
        }

      customerInvoice.setInvoiceTotalAmount(facture.getTotalTTC());
     
      customerInvoice.setIsFacture(true);

      customerInvoice.setInvoiceId(facture.getInvoiceCustomerId());
      customerInvoice.setInvoiceNet(facture.getTotalTTC());
      customerInvoice.setInvoiceRs(0.0);
      customerInvoice.setInvoiceRsType(RsTypeSaisie.POURCENTAGE);
      customerInvoice.setInvoiceStatus(InvoiceStatus.OPENED);
      customerInvoice.setInvoicePayment(0D);
      
        /*if(facture.getFactureId()!=null)
        {
            if(facture.getInvoiceCustomerId()!=null)
            {
                customerInvoice.setInvoiceId(facture.getInvoiceCustomerId());
            } else {
            	 customerInvoice.setInvoiceNet(facture.getTotalTTC());
                 customerInvoice.setInvoiceRs(0.0);
                 customerInvoice.setInvoiceRsType(RsTypeSaisie.POURCENTAGE);
                 customerInvoice.setInvoicePayment(0D);
            }
        }*/

        customerInvoice=customerInvoiceRepo.save(customerInvoice);
      return  customerInvoice;
    }

    private CustomerInvoice createCustomerInvoiceFromFacture(Facture facture) {
        CustomerInvoice customerInvoice=new CustomerInvoice();
        customerInvoice.setInvoiceNumber(facture.getFactureNumber());
        customerInvoice.setCustomer(facture.getCustomer());
        customerInvoice.setInvoiceCurrency(facture.getFactureCurrency());
        customerInvoice.setInvoiceDate(facture.getFactureDate());
        if(customerInvoice.getInvoiceDeadlineDate() == null) {
            Calendar c=Calendar.getInstance();
            c.add(Calendar.DATE,30);
            customerInvoice.setInvoiceDeadlineDate(c.getTime());
        }
        if(customerInvoice.getInvoiceDeadlineInNumberOfDays() == null) {
            customerInvoice.setInvoiceDeadlineInNumberOfDays(30);
        }
        if(facture.getFactureType() == FactureType.AVOIR) {
            customerInvoice.setInvoiceTotalAmount((-1)*facture.getTotalTTC());
        }else{
            customerInvoice.setInvoiceTotalAmount(facture.getTotalTTC());
        }
        
        customerInvoice.setIsFacture(true);
        customerInvoice.setIsGeneratedInvoice(true);
     // Nouvelle facture à créer donc il faut initialiser ces champs
    	customerInvoice.setInvoicePayment(0D);
        customerInvoice.setInvoiceRs(0.0);
        customerInvoice.setInvoiceRsType(RsTypeSaisie.POURCENTAGE);
        customerInvoice.setInvoiceNet(facture.getTotalTTC());
        customerInvoice.setInvoiceStatus(InvoiceStatus.OPENED);
        return  customerInvoice;
    }

    public void deleteFactureById(Long id)
   {   Facture facture=this.factureRepository.findOne(id);
   if(facture != null && facture.getFactureLines() != null){
       this.factureLineService.deleteAllLines(facture.getFactureLines());
   }
       factureRepository.delete(id);
   }

    public void deleteAllFacture()
   {
       factureRepository.deleteAll();
   }


    public List<Facture> findAllFacturesDateBetween(Map<String, Date> dates) {
        return this.factureRepository.findByFactureDateBetweenOrderByFactureDate(dates.get("beginDate"),dates.get("endDate"));
    }

    public List<Facture> findByFilterWithoutProduct(FactureFilterDto factureFilterDto) {
        List<Facture> factures=this.factureRepository.findByFactureDateBetweenOrderByFactureDate(factureFilterDto.getBeginDate(),factureFilterDto.getEndDate());
        List<Customer> customers=factureFilterDto.getCustomerList();
        if(factureFilterDto.getCustomerList().size()>0) {
            factures = factures.stream().filter(facture -> customers.stream().map(Customer::getCustomerId).anyMatch(name -> name.equals(facture.getCustomer().getCustomerId())))
                    .collect(Collectors.toList());
        }
        return factures;
    }

    public List<FactureProductDto> findByFilterWithProduct(FactureFilterDto factureFilterDto) {
        List<FactureProductDto> factureProductDtos=new ArrayList<>();
        List<Facture> factures=this.factureRepository.findByFactureDateBetweenOrderByFactureDate(factureFilterDto.getBeginDate(),factureFilterDto.getEndDate());
        List<Customer> customers=factureFilterDto.getCustomerList();
        List<Product> products=factureFilterDto.getProductList();

        if(factureFilterDto.getCustomerList().size()>0) {
            factures = factures.stream().filter(facture -> customers.stream().map(Customer::getCustomerId).anyMatch(name -> name.equals(facture.getCustomer().getCustomerId())))
                    .collect(Collectors.toList());
        }
        factures.forEach(facture -> {
            facture.getFactureLines().forEach(factureLine -> {
                Optional<Product> product=products.stream().filter(product1 -> product1.getProductId().equals(factureLine.getProduct().getProductId())).findFirst();
                if(product.isPresent()){
                    FactureProductDto factureProductDto=new FactureProductDto();
                    factureProductDto.setFacture(facture);
                    factureProductDto.setFactureLine(factureLine);
                    factureProductDtos.add(factureProductDto);
                }
            });
        });
        return factureProductDtos;
    }

    public boolean existesByFactureNumberAndOtherId(Facture facture) {
        if(facture.getFactureId() == null  ){
            return false;
        }else {
            return this.factureRepository.findByFactureIdNotLikeAndFactureNumberAndFactureType(facture.getFactureId(),facture.getFactureNumber(),facture.getFactureType()).isPresent();
        }
    }
}
