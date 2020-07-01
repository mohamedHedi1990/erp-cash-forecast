package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.dtos.ProviderDTO;
import org.apac.erp.cach.forecast.persistence.entities.Provider;
import org.apac.erp.cach.forecast.service.ProviderService;
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
@RequestMapping("/api/provider")
public class ProviderController {

	@Autowired
	private ProviderService providerService;

	@CrossOrigin
	@GetMapping()
	public List<Provider> findAllUsers() {
		return providerService.findAllProvides();
	}

	@CrossOrigin
	@PostMapping()
	public Provider saveNewProvider(@RequestBody Provider provider) {
		return providerService.saveNewProvider(provider);
	}

	@CrossOrigin
	@GetMapping("by-provider-id/{providerId}")
	public Provider findProviderById(@PathVariable("providerId") Long providerId) {
		return providerService.findProviderById(providerId);
	}

	@CrossOrigin
	@DeleteMapping("/{providerId}")
	public void deleteCompany(@PathVariable("providerId") Long providerId) {
		providerService.deleteCompany(providerId);
	}

}
