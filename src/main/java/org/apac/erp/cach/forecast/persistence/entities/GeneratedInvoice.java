package org.apac.erp.cach.forecast.persistence.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    private Set<GeneratedInvoiceLine> generatedInvoiceLines = new HashSet<>();

    private Double totalHTBrut;
    private Double remise;
    private Double totalHT;
    private Double totalTVA;
    private Double totalFodec;
    private Double totalTaxe;
    private Double timbreFiscal;
    private Double totalTTC;
    private Double montantFacture;
}
