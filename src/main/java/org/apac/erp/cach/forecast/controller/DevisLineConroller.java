package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.persistence.entities.Devis;
import org.apac.erp.cach.forecast.persistence.entities.DevisLine;
import org.apac.erp.cach.forecast.service.DevisLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/devisline")
public class DevisLineConroller {

	@Autowired
	private DevisLineService devisLineService;

	@CrossOrigin
	@GetMapping("/findallbydevis")
	public List<DevisLine> findAllByDevis(@RequestBody Devis devis) {
		return this.devisLineService.findByDevis(devis);
	}

	@CrossOrigin
	@PostMapping()
	public DevisLine saveDevisLine(@RequestBody DevisLine  devisLine) {
		return devisLineService.saveDevisLine(devisLine);
	}

	@CrossOrigin
	@GetMapping("/findbyline/{devisLineId}")
	public DevisLine getDevisLineById(@PathVariable("devisLineId") Long devisLineId) {
		return this.devisLineService.findById(devisLineId);
	}

	@CrossOrigin
	@DeleteMapping("/{devisLineId}")
	public void deleteDevisLine(@PathVariable("devisLineId") Long devisLineId) {
		devisLineService.deleteDevisLine(devisLineId);
	}

}
