package org.apac.erp.cach.forecast.persistence.entities;

import lombok.Data;
import org.apac.erp.cach.forecast.enumeration.Type;
import org.apac.erp.cach.forecast.enumeration.Unite;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "erp_product")
@Data
public class Product  extends  AuditableSql implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productLabel;
    private String productReference;
    private String productDescription;
    private String productUrlImage;
    private Double productPrixHT;
    private Double productFaudec;
    private Double productTVA;
    private Double productTTC;
    @Enumerated(EnumType.STRING)
    private Unite productUnite;
    @Enumerated(EnumType.STRING)
    private Type productType;


}
