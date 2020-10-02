package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.EncaissementType;
import org.apac.erp.cach.forecast.service.EncaissementTypeService;
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
@RequestMapping("/api/encaissement-type")
public class EncaissementTypeController {

	@Autowired
	private EncaissementTypeService encaissementTypeService;

	@CrossOrigin
	@GetMapping()
	public List<EncaissementType> findAll() {
		return encaissementTypeService.findAllEncaissementTypes();
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
	public EncaissementType saveNewEncaissement(@RequestBody EncaissementType encaissementType) {
		return encaissementTypeService.saveEncaissementType(encaissementType);
	}

	@CrossOrigin
	@GetMapping("/{encaissementTypeId}")
	public EncaissementType findEncaissementBYId(@PathVariable("encaissementTypeId") Long encaissementTypeId) {
		return encaissementTypeService.getEncaissementTypeById(encaissementTypeId);
	}

	@CrossOrigin
	@DeleteMapping("/{encaissementTypeId}")
	public void deleteEncaissement(@PathVariable("encaissementTypeId") Long encaissementTypeId) {
		encaissementTypeService.deleteEncaissementType(encaissementTypeId);
	}
}
