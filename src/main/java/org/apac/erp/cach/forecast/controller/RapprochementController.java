package org.apac.erp.cach.forecast.controller;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.RapprochementDTO;
import org.apac.erp.cach.forecast.persistence.entities.User;
import org.apac.erp.cach.forecast.service.RapprochemntService;
import org.apac.erp.cach.forecast.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rapprochement")
public class RapprochementController {
	
	@Autowired
	private RapprochemntService rapprochementService;

	@CrossOrigin
	@GetMapping("/{startDate}/{endDate}/{accountId}")
	public List<RapprochementDTO> findRapprochementsByDateIntervalAndBankAccount(@PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
			@PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate, @PathVariable("accountId") Long accountId) {
		return rapprochementService.findRapprochementsByDateIntervalAndBankAccount(startDate, endDate, accountId);
	}

}
