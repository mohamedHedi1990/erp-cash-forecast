package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "erp_bank")
@Data
public class Bank extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bankId;

	private String bankName;

	private String bankAddress;

	private String bankPhoneNumber;

	private String bankEmail;

	private double bankCheckPermissionCommission;

	private double bankRemittanceOfDiscountCommission;

	private double bankTransferCommission;

	private double bankInterestRateCommission;

	@OneToMany
	private List<Agency> bankAgencies;

	@OneToMany
	private List<Timeline> bankTimelines;

	@PrePersist
	private void persistId() {
		if (this.createdAt == null) {
			this.createdAt = new Date();
		}
		this.updatedAt = new Date();

	}

}
