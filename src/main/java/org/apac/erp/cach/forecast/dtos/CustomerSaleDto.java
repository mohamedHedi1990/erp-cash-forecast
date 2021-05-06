package org.apac.erp.cach.forecast.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerSaleDto {
    private String customerLabel;
    private List<CustomerMonthSaleDto> monthSales=new ArrayList<>();
    private Double valueTotal=0D;
    private String valueTotalS;
}
