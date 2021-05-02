package org.apac.erp.cach.forecast.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductMonthSaleDto {
    private String heading;
    private Double value;
    private String valueS;
    private double quantity;
}
