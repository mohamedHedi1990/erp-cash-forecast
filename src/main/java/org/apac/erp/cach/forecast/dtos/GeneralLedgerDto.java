package org.apac.erp.cach.forecast.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class GeneralLedgerDto {

    private Date date;
    private String label;
    private double debit;
    private String debitS;
    private String creditS;
    private double credit;
    private String progressiveAmountS;
    private double progressiveAmount;
}
