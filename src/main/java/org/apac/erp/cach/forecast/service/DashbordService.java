package org.apac.erp.cach.forecast.service;

import lombok.Data;
import org.apac.erp.cach.forecast.constants.Utils;
import org.apac.erp.cach.forecast.controller.DecaissementController;
import org.apac.erp.cach.forecast.dtos.CustomerSaleDto;
import org.apac.erp.cach.forecast.dtos.DashbordDto;
import org.apac.erp.cach.forecast.dtos.OperationTreserorieDto;
import org.apac.erp.cach.forecast.dtos.ProductSaleDto;
import org.apac.erp.cach.forecast.enumeration.OperationType;
import org.apac.erp.cach.forecast.persistence.entities.*;
import org.apac.erp.cach.forecast.persistence.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashbordService {

    @Autowired
    PaymentRuleRepository paymentRuleRepository;

    @Autowired
    SupervisionTresorerieService supervisionTresorerieService;
    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    FactureRepository factureRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ProductGroupRepository productGroupRepository;
    @Autowired
    FactureLineRepository factureLineRepository;


    public DashbordDto getDashbordInformation() {
        Date currentDate= Calendar.getInstance().getTime();
        DashbordDto dashbordDto=new DashbordDto();
        List<OperationTreserorieDto> operationTreserorieDtos=supervisionTresorerieService.globalSupervision(currentDate,currentDate,false);
        double collection=0;
        double disbursement=0;
        for (OperationTreserorieDto operationTreserorieDto : operationTreserorieDtos) {
            if(operationTreserorieDto.getOpperationType() == OperationType.ENCAISSEMENT){
               collection+=operationTreserorieDto.getOperationAmount();
            }
            else if(operationTreserorieDto.getOpperationType() == OperationType.DECAISSEMENT){
                disbursement+=operationTreserorieDto.getOperationAmount();
            }
        }
        double initialAmount=bankAccountRepository.findAll().stream().mapToDouble(account -> account.getAccountInitialAmount()).sum();

        List<Facture> factures = this.factureRepository.findByFactureDateBetweenOrderByFactureDate(currentDate,currentDate);

        double turvoner=factures.stream().mapToDouble(facture -> facture.getTotalHT()).sum();
        int factureNb=factures.size();
        BigDecimal bd = null;

        // les factures de chaque client
        List<CustomerSaleDto> customerSales = new ArrayList<>();
        for (Customer customer : customerRepository.findAllByOrderByCustomerLabel()) {
            double total = this.factureRepository.findByCustomerAndFactureDateBetweenOrderByFactureDate(customer, currentDate, currentDate).stream().mapToDouble(facture -> facture.getTotalHT()).sum();
            if(total > 0){
                CustomerSaleDto customerSale = new CustomerSaleDto();
                customerSale.setCustomerLabel(customer.getCustomerLabel());
                bd = new BigDecimal(total).setScale(3, RoundingMode.HALF_UP);
                customerSale.setValueTotal(bd.doubleValue());
                customerSale.setValueTotalS(Utils.convertAmountToStringWithSeperator(bd.doubleValue()));
                customerSales.add(customerSale);
            }
        }

        List<ProductSaleDto> productSales = new ArrayList<>();
        List<ProductGroup> productGroups=productGroupRepository.findAll();
        for (ProductGroup productGroup : productGroups) {
            for (Product product : productGroup.getProductList()) {
                List<Facture> ProductFactures = this.factureRepository.findByFactureDateBetweenOrderByFactureDate(currentDate, currentDate);
                double totalProduct = this.factureLineRepository.findByFactureInAndProductAndProductGroup(factures, product, productGroup).stream().mapToDouble(fl -> fl.getMontantHt()).sum();
                if(totalProduct > 0){
                    ProductSaleDto productSale = new ProductSaleDto();
                    productSale.setProductGroupLabel(productGroup.getProductGroupLabel());
                    productSale.setProductLabel(product.getProductReference());
                    bd = new BigDecimal(totalProduct).setScale(3, RoundingMode.HALF_UP);
                    productSale.setValueTotal(bd.doubleValue());
                    productSale.setValueTotalS(Utils.convertAmountToStringWithSeperator(bd.doubleValue()));
                    productSales.add(productSale);
                }
            }
        }
                // List<ProductSaleDto> productSaleDtos=supervisionTresorerieService.getProductsSales(currentDate,currentDate)
           //     .stream().filter(productSale -> {return productSale.getValueTotal() > 0;})
           //     .collect(Collectors.toList());
        dashbordDto.setCollectionToReconcile(collection);
        dashbordDto.setDisbursementToReconcile(disbursement);
        dashbordDto.setActualBalance(initialAmount);
        dashbordDto.setExpectedBalance(initialAmount - disbursement + collection);
        dashbordDto.setTurnover(turvoner);
        dashbordDto.setFactureNb(factureNb);
        dashbordDto.setCustomerSales(customerSales);
        dashbordDto.setProductSales(productSales);
        dashbordDto.setCollectionToReconcileS(Utils.convertAmountToStringWithSeperator(collection));
        dashbordDto.setDisbursementToReconcileS(Utils.convertAmountToStringWithSeperator(disbursement));
        dashbordDto.setActualBalanceS(Utils.convertAmountToStringWithSeperator(initialAmount));
        dashbordDto.setExpectedBalanceS(Utils.convertAmountToStringWithSeperator(initialAmount - disbursement + collection));
        dashbordDto.setTurnoverS(Utils.convertAmountToStringWithSeperator(turvoner));

        return dashbordDto;
    }

}
