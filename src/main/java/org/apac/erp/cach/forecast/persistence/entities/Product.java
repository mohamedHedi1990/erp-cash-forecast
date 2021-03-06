package org.apac.erp.cach.forecast.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.apac.erp.cach.forecast.constants.Utils;
import org.apac.erp.cach.forecast.enumeration.Type;
import org.apac.erp.cach.forecast.enumeration.Unite;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

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
    private String productPrixHTS;

    private Double productFodec;
    private String productFodecS;

    private Double productTVA;
    private String productTVAS;

    private Double productTTC;
    private String productTTCS;

    @Enumerated(EnumType.STRING)
    private Unite productUnite;
    @Enumerated(EnumType.STRING)
    private Type productType;

    @ManyToOne
    @JsonIgnoreProperties("productList")
    private ProductGroup productGroup;
    @PrePersist
    public void prePersist() {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setGroupingSeparator(',');
        simbolos.setDecimalSeparator('.');
        this.productPrixHT =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.productPrixHT));
        this.productPrixHTS = Utils.convertAmountToStringWithSeperator(this.productPrixHT);

        this.productFodec =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.productFodec));
        this.productFodecS = Utils.convertAmountToStringWithSeperator(this.productFodec);

        this.productTVA =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.productTVA));
        this.productTVAS = Utils.convertAmountToStringWithSeperator(this.productTVA);

        this.productTTC =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.productTTC));
        this.productTTCS = Utils.convertAmountToStringWithSeperator(this.productTTC);

    }


    @PreUpdate
    public void preUpdate() {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setGroupingSeparator(',');
        simbolos.setDecimalSeparator('.');
        this.productPrixHT =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.productPrixHT));
        this.productPrixHTS = Utils.convertAmountToStringWithSeperator(this.productPrixHT);

        this.productFodec =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.productFodec));
        this.productFodecS = Utils.convertAmountToStringWithSeperator(this.productFodec);


        this.productTVA =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.productTVA));
        this.productTVAS = Utils.convertAmountToStringWithSeperator(this.productTVA);

        this.productTTC =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.productTTC));
        this.productTTCS = Utils.convertAmountToStringWithSeperator(this.productTTC);
    }

}
