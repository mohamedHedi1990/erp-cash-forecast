package org.apac.erp.cach.forecast.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductSaleDto {
    private String productGroupLabel;
    private String productLabel;
    private List<ProductMonthSaleDto> monthSales=new ArrayList<>();
    private Double valueTotal=0D;
    private String valueTotalS;
}
