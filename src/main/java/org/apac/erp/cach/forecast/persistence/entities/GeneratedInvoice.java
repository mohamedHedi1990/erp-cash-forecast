package org.apac.erp.cach.forecast.persistence.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.apac.erp.cach.forecast.constants.Utils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@Entity
@Table(name = "erp_generatedInvoice")
@Data
public class GeneratedInvoice  extends  AuditableSql implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long generatedInvoiceId;
    private String generatedInvoiceNumber;
    private Integer generatedInvoiceDeadlineInNumberOfDays;
    private String generatedInvoiceCurrency;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
    @Temporal(TemporalType.DATE)
    private Date generatedInvoiceDeadlineDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
    @Temporal(TemporalType.DATE)
    private Date generatedInvoiceDate;
    @ManyToOne
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generatedInvoice")
    private List<GeneratedInvoiceLine> generatedInvoiceLines = new ArrayList<>();

    private Double totalHTBrut;
    private String totalHTBrutS;

    private Double remise;
    private String remiseS;

    private Double totalHT;
    private String totalHTS;

    private Double totalTVA;
    private String totalTVAS;

    private Double totalFodec;
    private String totalFodecS;

    private Double totalTaxe;
    private String totalTaxeS;

    private Double timbreFiscal;
    private String timbreFiscalS;

    private Double totalTTC;
    private String totalTTCS;

    private Double montantFacture;
    private String montantFactureS;
    private Long invoiceCustomerId;

    @PrePersist
    public void prePersist()
    {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setGroupingSeparator(',');
        simbolos.setDecimalSeparator('.');
        this.totalHTBrut =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.totalHTBrut));
        this.totalHTBrutS = Utils.convertAmountToStringWithSeperator(this.totalHTBrut);
        this.remise =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.remise));
        this.remiseS = Utils.convertAmountToStringWithSeperator(this.remise);
        this.totalHT =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.totalHT));
        this.totalHTS = Utils.convertAmountToStringWithSeperator(this.totalHT);
        this.totalTVA =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.totalTVA));
        this.totalTVAS = Utils.convertAmountToStringWithSeperator(this.totalTVA);
        this.totalFodec =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.totalFodec));
        this.totalFodecS = Utils.convertAmountToStringWithSeperator(this.totalFodec);
        this.totalTaxe =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.totalTaxe));
        this.totalTaxeS = Utils.convertAmountToStringWithSeperator(this.totalTaxe);
        this.timbreFiscal =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.timbreFiscal));
        this.timbreFiscalS = Utils.convertAmountToStringWithSeperator(this.timbreFiscal);
        this.totalTTC =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.totalTTC));
        this.totalTTCS = Utils.convertAmountToStringWithSeperator(this.totalTTC);
        this.montantFacture =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.montantFacture));
        this.montantFactureS = Utils.convertAmountToStringWithSeperator(this.montantFacture);
    }

    @PreUpdate
    public void preUpdate()
    {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setGroupingSeparator(',');
        simbolos.setDecimalSeparator('.');
        this.totalHTBrut =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.totalHTBrut));
        this.totalHTBrutS = Utils.convertAmountToStringWithSeperator(this.totalHTBrut);
        this.remise =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.remise));
        this.remiseS = Utils.convertAmountToStringWithSeperator(this.remise);
        this.totalHT =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.totalHT));
        this.totalHTS = Utils.convertAmountToStringWithSeperator(this.totalHT);
        this.totalTVA =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.totalTVA));
        this.totalTVAS = Utils.convertAmountToStringWithSeperator(this.totalTVA);
        this.totalFodec =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.totalFodec));
        this.totalFodecS = Utils.convertAmountToStringWithSeperator(this.totalFodec);
        this.totalTaxe =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.totalTaxe));
        this.totalTaxeS = Utils.convertAmountToStringWithSeperator(this.totalTaxe);
        this.timbreFiscal =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.timbreFiscal));
        this.timbreFiscalS = Utils.convertAmountToStringWithSeperator(this.timbreFiscal);
        this.totalTTC =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.totalTTC));
        this.totalTTCS = Utils.convertAmountToStringWithSeperator(this.totalTTC);
        this.montantFacture =Double.parseDouble(new DecimalFormat("##.###",simbolos).format(this.montantFacture));
        this.montantFactureS = Utils.convertAmountToStringWithSeperator(this.montantFacture);
    }
}
