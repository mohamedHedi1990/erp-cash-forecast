package org.apac.erp.cach.forecast.controller;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Decaissement;
import org.apac.erp.cach.forecast.service.DecaissementService;
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
	
	@Autowired
	private DecaissementService decaissementService;
	
	@CrossOrigin
	@GetMapping()
	public List<Decaissement> findAll() {
		return decaissementService.findAllDecaissements();
	}

	@CrossOrigin
	@GetMapping("/{startDate}/{endDate}")
	public List<Decaissement> findAllDecaissementsBetweenTwoDates(@PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
			@PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {
		return decaissementService.findAllDecaissementsBetweenTwoDates(startDate, endDate);
	}

	@CrossOrigin
	@PostMapping()
	public Decaissement saveNewDecaissement(@RequestBody Decaissement encDec) {
		return decaissementService.saveNewDecaissement(encDec);
	}

}
