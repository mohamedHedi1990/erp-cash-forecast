package org.apac.erp.cach.forecast.persistence.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "erp_devisLine")
@Data
public class DevisLine extends  AuditableSql implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long devisLineId;
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
    private ProductGroup productGroup;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Devis devis;
}
