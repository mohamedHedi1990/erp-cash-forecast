package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Bank;
import org.apac.erp.cach.forecast.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

}
