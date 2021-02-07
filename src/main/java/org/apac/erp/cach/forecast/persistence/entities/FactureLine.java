package org.apac.erp.cach.forecast.persistence.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apac.erp.cach.forecast.constants.Utils;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@Entity
@Table(name = "erp_factureLine")
@Data
public class FactureLine extends  AuditableSql implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long factureLineId;
    private int quantity;
    private Double montantHt;
    private String montantHtS;
    private Double montantHtBrut;
    private String montantHtBrutS;
    private Double remiseTaux;
    private Double remiseValeur;
    private String remiseValeurS;
    private Double montantFaudec;
    private String montantFaudecS;
    private Double montantTva;
    private String montantTvaS;

    @ManyToOne
    private Product product;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Facture facture;

    @PrePersist
    public void prePersist() {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setGroupingSeparator(',');
        simbolos.setDecimalSeparator('.');
        this.montantHt = Double.parseDouble(new DecimalFormat("##.###", simbolos).format(this.montantHt));
        this.montantHtS = Utils.convertAmountToStringWithSeperator(this.montantHt);
        this.montantTva = Double.parseDouble(new DecimalFormat("##.###", simbolos).format(this.montantTva));
        this.montantTvaS = Utils.convertAmountToStringWithSeperator(this.montantTva);
        this.montantFaudec = Double.parseDouble(new DecimalFormat("##.###", simbolos).format(this.montantFaudec));
        this.montantFaudecS = Utils.convertAmountToStringWithSeperator(this.montantFaudec);
        this.remiseValeur = Double.parseDouble(new DecimalFormat("##.###", simbolos).format(this.remiseValeur));
        this.remiseValeurS = Utils.convertAmountToStringWithSeperator(this.remiseValeur);
        this.montantHtBrut = Double.parseDouble(new DecimalFormat("##.###", simbolos).format(this.montantHtBrut));
        this.montantHtBrutS = Utils.convertAmountToStringWithSeperator(this.montantHtBrut);
    }


    @PreUpdate
    public void preUpdate() {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setGroupingSeparator(',');
        simbolos.setDecimalSeparator('.');
        this.montantHt = Double.parseDouble(new DecimalFormat("##.###", simbolos).format(this.montantHt));
        this.montantHtS = Utils.convertAmountToStringWithSeperator(this.montantHt);
        this.montantTva = Double.parseDouble(new DecimalFormat("##.###", simbolos).format(this.montantTva));
        this.montantTvaS = Utils.convertAmountToStringWithSeperator(this.montantTva);
        this.montantFaudec = Double.parseDouble(new DecimalFormat("##.###", simbolos).format(this.montantFaudec));
        this.montantFaudecS = Utils.convertAmountToStringWithSeperator(this.montantFaudec);
        this.remiseValeur = Double.parseDouble(new DecimalFormat("##.###", simbolos).format(this.remiseValeur));
        this.remiseValeurS = Utils.convertAmountToStringWithSeperator(this.remiseValeur);
        this.montantHtBrut = Double.parseDouble(new DecimalFormat("##.###", simbolos).format(this.montantHtBrut));
        this.montantHtBrutS = Utils.convertAmountToStringWithSeperator(this.montantHtBrut);
    }
}
