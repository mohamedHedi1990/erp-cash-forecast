package org.apac.erp.cach.forecast.controller;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.EncaissementDecaissement;
import org.apac.erp.cach.forecast.service.EncaissementDecaissementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/decaissement")
public class DecaissementController {
	/*
	@Autowired
	private EncaissementDecaissementService encaissementDecaissementService;
	
	@CrossOrigin
	@GetMapping()
	public List<EncaissementDecaissement> findAll() {
		return encaissementDecaissementService.findAllDecaissements();
	}

	@CrossOrigin
	@GetMapping("/{startDate}/{endDate}")
	public List<EncaissementDecaissement> findAllDecaissementsBetweenTwoDates(@PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
			@PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {
		return encaissementDecaissementService.findAllDecaissementsBetweenTwoDates(startDate, endDate);
	}

	@CrossOrigin
	@PostMapping()
	public EncaissementDecaissement saveNewDecaissement(@RequestBody EncaissementDecaissement encDec) {
		return encaissementDecaissementService.saveNewEncaissementDecaissement(encDec);
	}
	*/

}
