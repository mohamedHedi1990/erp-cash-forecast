package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Decaissement;
import org.apac.erp.cach.forecast.persistence.entities.Encaissement;
import org.apac.erp.cach.forecast.service.EncaissementService;
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
@RequestMapping("/api/encaissement")
public class EncaissementController {

	@Autowired
	private EncaissementService encaissementService;

	@CrossOrigin
	@GetMapping()
	public List<Encaissement> findAll() {
		return encaissementService.findAllEncaissements();
	}

	@CrossOrigin
	@PostMapping()
	public Encaissement saveNewEncaissement(@RequestBody Encaissement encDec) {
		return encaissementService.saveEncaissement(encDec);
	}

	@CrossOrigin
	@GetMapping("/{encaissementId}")
	public Encaissement findEncaissementBYId(@PathVariable("encaissementId") Long encaissementId) {
		return encaissementService.getEncaissementById(encaissementId);
	}

	@CrossOrigin
	@DeleteMapping("/{encaissementId}")
	public void deleteEncaissement(@PathVariable("encaissementId") Long encaissementId) {
		encaissementService.deleteEncaissement(encaissementId);
	}
	
	@CrossOrigin
	@PutMapping("/{encaissementId}")
	public Encaissement validateEncaissement(@PathVariable("encaissementId") Long encaissementId) {
		return encaissementService.validateEncaissement(encaissementId);
	}
}
