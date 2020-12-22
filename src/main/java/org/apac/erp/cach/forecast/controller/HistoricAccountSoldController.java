package org.apac.erp.cach.forecast.controller;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.HistoricAccountSold;
import org.apac.erp.cach.forecast.service.HistoricAccountSoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/historic-account-sold")
public class HistoricAccountSoldController {
	
	@Autowired
	private HistoricAccountSoldService historicAccountSoldService;
	
	@CrossOrigin
	@GetMapping("/{accountId}/{startDate}")
	public HistoricAccountSold findTheBeginningSold(@PathVariable("accountId") Long accountId,  @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate) {
		return historicAccountSoldService.findFirst(accountId, startDate);
	}

}
