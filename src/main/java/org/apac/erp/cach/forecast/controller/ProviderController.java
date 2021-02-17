package org.apac.erp.cach.forecast.controller;

import java.io.IOException;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.ProviderDTO;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.entities.Provider;
import org.apac.erp.cach.forecast.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
	@GetMapping("/{providerId}")
	public Provider getProviderById(@PathVariable("providerId") Long providerId) {
		return this.providerService.getProviderById(providerId);
	}
	@CrossOrigin
	@DeleteMapping("/{providerId}")
	public void deleteProvider(@PathVariable("providerId") Long providerId) {
		providerService.deleteProvider(providerId);
	}

	@CrossOrigin
	@PostMapping(value = "/import",headers = {"content-type=multipart/mixed", "content-type=multipart/form-data"},consumes = {"multipart/form-data"})
	public void importProviderssFromExcelFile(@RequestParam("file") MultipartFile file) throws IOException {
		providerService.importProvidersFromExcelFile(file);
	}
}
