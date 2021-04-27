package org.apac.erp.cach.forecast.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusCashDto {
    private String heading;
    private Double cashBalanceMM;
    private Double encaissementEngaged;
    private Double decaissementEngaged;
    private Double committedCash;
    private Double cashBalanceM;
    private Double encaissementNotEngaged;
    private Double decaissementNotEngaged;
    private Double cashBalanceNotEngagedM;
    private Double cumulativeNetCash;
    private String cashBalanceMMS;
    private String encaissementEngagedS;
    private String decaissementEngagedS;
    private String committedCashS;
    private String cashBalanceMS;
    private String encaissementNotEngagedS;
    private String decaissementNotEngagedS;
    private String cashBalanceNotEngagedMS;
    private String cumulativeNetCashS;

}
