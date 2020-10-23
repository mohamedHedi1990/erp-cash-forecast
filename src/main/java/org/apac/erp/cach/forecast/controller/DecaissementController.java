package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Decaissement;
import org.apac.erp.cach.forecast.persistence.entities.Encaissement;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.service.DecaissementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	/*
	 * @CrossOrigin
	 * 
	 * @GetMapping("/{startDate}/{endDate}") public
	 * List<EncaissementDecaissement> findAllEncaissementsBetweenTwoDates(
	 * 
	 * @PathVariable("startDate") @DateTimeFormat(pattern =
	 * "yyyy-MM-dd HH:mm:ss") Date startDate,
	 * 
	 * @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	 * Date endDate) { return
	 * encaissementDecaissementService.findAllEncaissementsBetweenTwoDates(
	 * startDate, endDate); }
	 */
	@CrossOrigin
	@PostMapping()
	public Decaissement saveDecaissement(@RequestBody Decaissement encDec) {
		return decaissementService.saveDecaissement(encDec);
	}

	@CrossOrigin
	@GetMapping("/{decaissementId}")
	public Decaissement findDecaissementBYId(@PathVariable("decaissementId") Long decaissementId) {
		return decaissementService.getDecaissementById(decaissementId);
	}

	@CrossOrigin
	@DeleteMapping("/{decaissementId}")
	public void deleteDecaissement(@PathVariable("decaissementId") Long decaissementId) {
		decaissementService.deleteDecaissement(decaissementId);
	}
	
	@CrossOrigin
	@PutMapping("/{decaissementId}")
	public Decaissement validateDecaissement(@PathVariable("decaissementId") Long decaissementId) {
		return decaissementService.validateDecaissement(decaissementId);
	}
}
