package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Provider;
import org.apac.erp.cach.forecast.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

}
