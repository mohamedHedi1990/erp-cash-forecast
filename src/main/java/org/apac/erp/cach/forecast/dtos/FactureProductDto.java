package org.apac.erp.cach.forecast.dtos;

import lombok.Data;
import org.apac.erp.cach.forecast.persistence.entities.Facture;
import org.apac.erp.cach.forecast.persistence.entities.FactureLine;

@Data
public class FactureProductDto {

    Facture facture;
    FactureLine factureLine;
}
