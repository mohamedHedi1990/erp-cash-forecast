package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.apac.erp.cach.forecast.enumeration.Role;

import lombok.Data;

@Entity
@Table(name = "erp_user")
@Data
public class User extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	private String userLogin;

	private String userPassword;

	@Enumerated(EnumType.STRING)
	private Role userRole;

	@PrePersist
	private void persistId() {
		if (this.createdAt == null) {
			this.createdAt = new Date();
		}
		this.updatedAt = new Date();

	}

}
