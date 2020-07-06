package org.apac.erp.cach.forecast.dtos;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.BankAccount;

import lombok.Data;

@Data
public class AccountsByBankDTO {
	
	private Long bankId;

	public AccountsByBankDTO(Long bankId, String bankName, double bankCheckPermissionCommission, double bankRemittanceOfDiscountCommission,
			double bankTransferCommission, double bankInterestRateCommission) {
		super();
		this.bankId = bankId;
		this.bankName = bankName;
		this.bankCheckPermissionCommission = bankCheckPermissionCommission;
		this.bankRemittanceOfDiscountCommission = bankRemittanceOfDiscountCommission;
		this.bankTransferCommission = bankTransferCommission;
		this.bankInterestRateCommission = bankInterestRateCommission;
	}

	private String bankName;

	private double bankCheckPermissionCommission;

	private double bankRemittanceOfDiscountCommission;

	private double bankTransferCommission;

	private double bankInterestRateCommission;

	private List<BankAccount> accounts;
}
