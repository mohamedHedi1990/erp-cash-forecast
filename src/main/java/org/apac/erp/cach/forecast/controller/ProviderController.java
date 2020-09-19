package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.dtos.ProviderDTO;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
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
	public List<Provider> getAllProviders() {
		return this.providerService.getAllProviders();
	}

	@CrossOrigin
	@PostMapping()
	public Provider saveProvider(@RequestBody Provider provider) {
		return providerService.saveProvider(provider);
	}

	@CrossOrigin
	@GetMapping("/{customerId}")
	public Provider getProviderById(@PathVariable("providerId") Long providerId) {
		return this.providerService.getProviderById(providerId);
	}
	@CrossOrigin
	@DeleteMapping("/{customerId}")
	public void deleteProvider(@PathVariable("providerId") Long providerId) {
		providerService.deleteProvider(providerId);
	}
}
