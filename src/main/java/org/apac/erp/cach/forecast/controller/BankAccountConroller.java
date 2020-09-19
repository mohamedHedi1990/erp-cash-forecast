package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.dtos.AccountsByBankDTO;
import org.apac.erp.cach.forecast.dtos.BankAccountDTO;
import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.TarifBancaire;
import org.apac.erp.cach.forecast.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/account")
public class BankAccountConroller {

	@Autowired
	private BankAccountService bankAccountService;

	@CrossOrigin
	@GetMapping()
	public List<BankAccount> findAlltarifs() {
		return this.bankAccountService.getAllBankAccounts();
	}

	@CrossOrigin
	@PostMapping()
	public BankAccount saveAccount(@RequestBody BankAccount account) {
		return bankAccountService.saveAccount(account);
	}

	@CrossOrigin
	@GetMapping("/{accountId}")
	public BankAccount getAccountById(@PathVariable("tarifId") Long accountId) {
		return this.bankAccountService.getAccountById(accountId);
	}
	@CrossOrigin
	@DeleteMapping("/{accountId}")
	public void deleteAccount(@PathVariable("tarifId") Long accountId) {
		bankAccountService.deleteAccount(accountId);
	}
}
