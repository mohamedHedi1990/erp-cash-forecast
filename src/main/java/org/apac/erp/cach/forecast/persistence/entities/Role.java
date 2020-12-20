package org.apac.erp.cach.forecast.persistence.entities;
import lombok.Data;
import org.apac.erp.cach.forecast.enumeration.ERole;

import javax.persistence.*;

@Entity
@Data
@Table(name = "erp_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ERole name;

}
