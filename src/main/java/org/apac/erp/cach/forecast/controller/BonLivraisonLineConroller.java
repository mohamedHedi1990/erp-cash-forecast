package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.persistence.entities.BonLivraison;
import org.apac.erp.cach.forecast.persistence.entities.BonLivraisonLine;
import org.apac.erp.cach.forecast.service.BonLivraisonLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/bonlivraisonline")
public class BonLivraisonLineConroller {

	@Autowired
	private BonLivraisonLineService bonLivraisonLineService;

	@CrossOrigin
	@GetMapping("/findallbybonlivraison")
	public List<BonLivraisonLine> findAllByBonLivraison(@RequestBody BonLivraison bonLivraison) {
		return this.bonLivraisonLineService.findByBonLivraison(bonLivraison);
	}

	@CrossOrigin
	@PostMapping()
	public BonLivraisonLine saveBonLivraisonLine(@RequestBody BonLivraisonLine  bonLivraisonLine) {
		return bonLivraisonLineService.saveBonLivraisonLine(bonLivraisonLine);
	}

	@CrossOrigin
	@GetMapping("/findbyline/{bonLivraisonLineId}")
	public BonLivraisonLine getBonLivraisonLineById(@PathVariable("bonLivraisonLineId") Long bonLivraisonLineId) {
		return this.bonLivraisonLineService.findById(bonLivraisonLineId);
	}

	@CrossOrigin
	@DeleteMapping("/{bonLivraisonLineId}")
	public void deleteBonLivraisonLine(@PathVariable("bonLivraisonLineId") Long bonLivraisonLineId) {
		bonLivraisonLineService.deleteBonLivraisonLine(bonLivraisonLineId);
	}

}
