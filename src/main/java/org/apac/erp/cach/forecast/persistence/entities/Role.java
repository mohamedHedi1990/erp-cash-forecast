package org.apac.erp.cach.forecast.persistence.entities;
import lombok.Data;
import org.apac.erp.cach.forecast.enumeration.ERole;

import javax.persistence.*;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

}
