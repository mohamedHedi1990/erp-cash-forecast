package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Bank;
import org.apac.erp.cach.forecast.service.BankService;
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
@RequestMapping("/api/bank")
public class BankController {

	@Autowired
	private BankService bankService;

	@CrossOrigin
	@GetMapping()
	public List<Bank> findAllBanks() {
		return bankService.findAllBanks();
	}

	@CrossOrigin
	@PostMapping()
	public Bank saveNewBank(@RequestBody Bank bank) {
		return bankService.saveNewBank(bank);
	}

	@CrossOrigin
	@GetMapping("by-bank-id/{bankId}")
	public Bank findBankById(@PathVariable("bankId") Long bankId) {
		return bankService.findBankById(bankId);
	}
	
	@CrossOrigin
	@DeleteMapping("/{bankId}")
	public void deleteBank(@PathVariable("bankId") Long bankId) {
		bankService.deleteBank(bankId);
	}

}
