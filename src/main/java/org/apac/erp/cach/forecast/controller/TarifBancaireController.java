package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.TarifBancaire;
import org.apac.erp.cach.forecast.service.TarifBancaireService;
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
@RequestMapping("/api/tarif")
public class TarifBancaireController {

	@Autowired
	private TarifBancaireService tarifBancaireService;;

	@CrossOrigin
	@GetMapping()
	public List<TarifBancaire> findAlltarifs() {
		return this.tarifBancaireService.getAllTarifsBancaires();
	}

	@CrossOrigin
	@PostMapping()
	public TarifBancaire saveTarifBancaire(@RequestBody TarifBancaire tarif) {
		return tarifBancaireService.saveTarifBancaire(tarif);
	}

	@CrossOrigin
	@GetMapping("/{tarifId}")
	public TarifBancaire findBankById(@PathVariable("tarifId") Long tarifId) {
		return this.tarifBancaireService.getTarifById(tarifId);
	}
	@CrossOrigin
	@DeleteMapping("/{tarifId}")
	public void deleteTarifBancaire(@PathVariable("tarifId") Long tarifId) {
		tarifBancaireService.deleteTarifBancaire(tarifId);
	}


}
