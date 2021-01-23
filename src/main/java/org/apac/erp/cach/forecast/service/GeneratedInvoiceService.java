package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.persistence.entities.GeneratedInvoice;
import org.apac.erp.cach.forecast.persistence.entities.GeneratedInvoiceLine;
import org.apac.erp.cach.forecast.persistence.repositories.GeneratedInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GeneratedInvoiceService {
    @Autowired
    GeneratedInvoiceRepository generatedInvoiceRepository;
    @Autowired
    GeneratedInvoiceLineService generatedInvoiceLineService;
    @Autowired
    CustomerInvoiceService customerInvoiceService;

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
       CustomerInvoice customerInvoice=createCustomerInvoiceFromGeneratedInvoice(generatedInvoice);

       if(generatedInvoice.getGeneratedInvoiceId()!=null)
       {
           if(generatedInvoice.getInvoiceCustomerId()!=null)
           {
               customerInvoice.setInvoiceId(generatedInvoice.getInvoiceCustomerId());
           }
       }

       CustomerInvoice customerInvoiceSaved=customerInvoiceService.saveCustomerInvoice(customerInvoice);
       generatedInvoice.setInvoiceCustomerId(customerInvoiceSaved.getInvoiceId());
       GeneratedInvoice savedGeneratedInvoice=generatedInvoiceRepository.save(generatedInvoice);
       if(savedGeneratedInvoice.getGeneratedInvoiceLines() != null){
           savedGeneratedInvoice.getGeneratedInvoiceLines().forEach(generatedInvoiceLine -> {
               generatedInvoiceLine.setGeneratedInvoice(savedGeneratedInvoice);
               generatedInvoiceLineService.saveGeneratedInvoiceLine(generatedInvoiceLine);
           });
       }
       return savedGeneratedInvoice;

   }

    private CustomerInvoice createCustomerInvoiceFromGeneratedInvoice(GeneratedInvoice generatedInvoice) {
      CustomerInvoice customerInvoice=new CustomerInvoice();
      customerInvoice.setInvoiceNumber(generatedInvoice.getGeneratedInvoiceNumber());
      customerInvoice.setCustomer(generatedInvoice.getCustomer());
      customerInvoice.setInvoiceCurrency(generatedInvoice.getGeneratedInvoiceCurrency());
      customerInvoice.setInvoiceDate(generatedInvoice.getGeneratedInvoiceDate());
      customerInvoice.setInvoiceDeadlineDate(generatedInvoice.getGeneratedInvoiceDeadlineDate());
      customerInvoice.setInvoiceDeadlineInNumberOfDays(generatedInvoice.getGeneratedInvoiceDeadlineInNumberOfDays());
      customerInvoice.setInvoiceTotalAmount(generatedInvoice.getTotalTTC());
      customerInvoice.setInvoicePayment(0D);
      customerInvoice.setIsGeneratedInvoice(true);
      return  customerInvoice;
    }

    public void deleteGeneratedInvoiceById(Long id)
   {   GeneratedInvoice generatedInvoice=this.generatedInvoiceRepository.findOne(id);
   if(generatedInvoice != null && generatedInvoice.getGeneratedInvoiceLines() != null){
       this.generatedInvoiceLineService.deleteAllLines(generatedInvoice.getGeneratedInvoiceLines());
   }
       customerInvoiceService.deleteCustomerInvoice(generatedInvoice.getInvoiceCustomerId());
       generatedInvoiceRepository.delete(id);
   }

    public void deleteAllGeneratedInvoice()
   {
       generatedInvoiceRepository.deleteAll();
   }
}
