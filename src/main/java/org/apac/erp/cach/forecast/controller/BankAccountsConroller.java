package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.dtos.BankAccountDTO;
import org.apac.erp.cach.forecast.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/bankAccount")
public class BankAccountsConroller {

	@Autowired
	private BankAccountService bankAccountService;

	@CrossOrigin
	@GetMapping()
	public List<BankAccountDTO> findAllBankAccounts() {
		return bankAccountService.findAllBankAccounts();
	}

}
