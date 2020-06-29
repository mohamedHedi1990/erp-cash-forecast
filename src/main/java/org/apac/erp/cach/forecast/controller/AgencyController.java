package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.dtos.AgencyDTO;
import org.apac.erp.cach.forecast.persistence.entities.Agency;
import org.apac.erp.cach.forecast.service.AgencyService;
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
@RequestMapping("/api/agency")
public class AgencyController {

	@Autowired
	private AgencyService agencyService;

	@CrossOrigin
	@GetMapping()
	public List<AgencyDTO> findAllAgencies() {
		return agencyService.findAllAgencies();
	}

	@CrossOrigin
	@PostMapping("/bank/{bankId}")
	public Agency saveNewAgency(@PathVariable("bankId") Long bankId, @RequestBody Agency agency) {
		return agencyService.saveNewAgencyToGivenBank(agency, bankId);
	}
	
	@CrossOrigin
	@DeleteMapping("/{agencyId}")
	public void deleteCompany(@PathVariable("agencyId") Long agencyId) {
		agencyService.deleteAgency(agencyId);
	}


}
