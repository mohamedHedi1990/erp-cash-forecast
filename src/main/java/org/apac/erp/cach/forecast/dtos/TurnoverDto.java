package org.apac.erp.cach.forecast.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TurnoverDto {
    private String heading;
    private Double turnover;
    private Double evolution;
}
