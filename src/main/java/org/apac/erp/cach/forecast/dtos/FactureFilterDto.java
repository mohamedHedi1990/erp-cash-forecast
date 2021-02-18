package org.apac.erp.cach.forecast.dtos;

import lombok.Data;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.entities.Product;

import java.util.Date;
import java.util.List;

@Data
public class FactureFilterDto {

    List<Product> productList;
    List<Customer> customerList;
    Date beginDate;
    Date endDate;


}
