package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.DecaissementType;
import org.apac.erp.cach.forecast.persistence.entities.EncaissementType;
import org.apac.erp.cach.forecast.service.DecaissementTypeService;
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
@RequestMapping("/api/decaissement-type")
public class DecaissementTypeController {

	@Autowired
	private DecaissementTypeService decaissementTypeService;

	@CrossOrigin
	@GetMapping()
	public List<DecaissementType> findAll() {
		return decaissementTypeService.findAllDecaissementTypes();
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
	public DecaissementType saveDecaissementType(@RequestBody DecaissementType decaissementType) {
		return decaissementTypeService.saveDecaissementType(decaissementType);
	}

	@CrossOrigin
	@GetMapping("/{encaissementTypeId}")
	public DecaissementType findEncaissementBYId(@PathVariable("decaissementTypeId") Long decaissementTypeId) {
		return decaissementTypeService.getDecaissementTypeById(decaissementTypeId);
	}

	@CrossOrigin
	@DeleteMapping("/{decaissementTypeId}")
	public void deleteEncaissement(@PathVariable("decaissementTypeId") Long decaissementTypeId) {
		decaissementTypeService.deleteDecaissementType(decaissementTypeId);
	}
}
