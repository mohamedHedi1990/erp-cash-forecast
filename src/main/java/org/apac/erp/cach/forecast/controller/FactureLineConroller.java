package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.persistence.entities.Facture;
import org.apac.erp.cach.forecast.persistence.entities.FactureLine;
import org.apac.erp.cach.forecast.service.FactureLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/factureline")
public class FactureLineConroller {

	@Autowired
	private FactureLineService factureLineService;

	@CrossOrigin
	@GetMapping("/findallbybonlivraison")
	public List<FactureLine> findAllByFacture(@RequestBody Facture facture) {
		return this.factureLineService.findByFacture(facture);
	}

	@CrossOrigin
	@PostMapping()
	public FactureLine saveFactureLine(@RequestBody FactureLine  factureLine) {
		return factureLineService.saveFactureLine(factureLine);
	}

	@CrossOrigin
	@GetMapping("/findbyline/{factureLineId}")
	public FactureLine getFactureLineById(@PathVariable("factureLineId") Long factureLineId) {
		return this.factureLineService.findById(factureLineId);
	}

	@CrossOrigin
	@DeleteMapping("/{factureLineId}")
	public void deleteFactureLine(@PathVariable("factureLineId") Long factureLineId) {
		factureLineService.deleteFactureLine(factureLineId);
	}

}
