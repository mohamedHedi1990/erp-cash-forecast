package org.apac.erp.cach.forecast.persistence.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "erp_generatedInvoiceLine")
@Data
public class GeneratedInvoiceLine extends  AuditableSql implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long generatedInvoiceLineId;
    private int quantity;
    private Double montantHt;
    private Double montantHtBrut;
    private Double remiseTaux;
    private Double remiseValeur;
    private Double montantFaudec;
    private Double montantTva;
    @ManyToOne
    private Product product;
    @ManyToOne
    @JsonIgnoreProperties("generatedInvoiceLines")
    private GeneratedInvoice generatedInvoice;
}
