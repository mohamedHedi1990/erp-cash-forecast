package org.apac.erp.cach.forecast.service;

import java.util.ArrayList;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.AccountsByBankDTO;
import org.apac.erp.cach.forecast.dtos.BankAccountDTO;
import org.apac.erp.cach.forecast.persistence.entities.Bank;
import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

	@Autowired
	private BankAccountRepository bankAccountRepo;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private BankService bankService;

	@Autowired
	private AgencyService agencyService;

	public List<BankAccountDTO> findAllBankAccounts() {
		List<BankAccount> accounts = bankAccountRepo.findAll();
		List<BankAccountDTO> dtos = new ArrayList<>();
		accounts.stream().forEach(account -> {
			BankAccountDTO dto = new BankAccountDTO(account.getBankAccountId(), account.getBankAccountLabel(),
					account.getBankAccountNumber(), account.getBankAccountChargeCustomerName(),
					account.getBankAccountChargeCustomerPhoneNumber(), account.getBankAccountAgency().getAgencyName(),
					account.getCreatedAt(), account.getUpdatedAt());
			dtos.add(dto);
		});
		return dtos;
	}
	
	public BankAccount findById(Long accountId) {
		return bankAccountRepo.findOne(accountId);
	}

	public void deleteAccount(Long accountId) {
		bankAccountRepo.delete(accountId);
	}

	public BankAccount saveNewAgencyToGivenAgency(BankAccount account, Long agencyId) {

		account.setBankAccountAgency(agencyService.findOneById(agencyId));
		return bankAccountRepo.save(account);
	}

	public List<AccountsByBankDTO> findBankAccountsByBank() {
		List<AccountsByBankDTO> dtos = new ArrayList<>();
		List<Bank> banks = bankService.findAllBanks();
		banks.stream().forEach(bank -> {
			AccountsByBankDTO accountsByBankDTO = new AccountsByBankDTO(bank.getBankId(), bank.getBankName(),
					bank.getBankCheckPermissionCommission(), bank.getBankRemittanceOfDiscountCommission(),
					bank.getBankTransferCommission(), bank.getBankInterestRateCommission());
			List<BankAccount> accounts = new ArrayList<>();
			// TODO to be refactored with bidirectional relation
			List<BankAccount> allAccounts = bankAccountRepo.findAll();
			allAccounts.stream().forEach(acc -> {
				if (acc.getBankAccountAgency().getAgencyBank() == bank) {
					accounts.add(acc);
				}
			});
			
			accountsByBankDTO.setAccounts(accounts);
			dtos.add(accountsByBankDTO);
		});

		return dtos;
	}

}