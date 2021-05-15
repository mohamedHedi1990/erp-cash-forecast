package org.apac.erp.cach.forecast.dtos;

import lombok.Data;

import java.util.List;

@Data
public class DashbordDto {

    private double actualBalance;
    private String actualBalanceS;

    private double collectionToReconcile;
    private String collectionToReconcileS;

    private double disbursementToReconcile;
    private String disbursementToReconcileS;

    private double expectedBalance;
    private String expectedBalanceS;

    private double turnover;
    private String turnoverS;

    private int factureNb;

    List<CustomerSaleDto> customerSales;

    List<ProductSaleDto> productSales;

}
