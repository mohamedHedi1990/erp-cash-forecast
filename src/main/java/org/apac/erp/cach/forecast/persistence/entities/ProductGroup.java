package org.apac.erp.cach.forecast.persistence.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "erp_product_group")
@Data
public class ProductGroup extends AuditableSql implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productGroupId;
    private String productGroupLabel;
    private String productGroupCode;
    @OneToMany(cascade= CascadeType.ALL, mappedBy="productGroup")
    List<Product> productList=new ArrayList<>();
    @PrePersist
    public void prePersist() {
    }
    @PreUpdate
    public void preUpdate() {
    }

}
