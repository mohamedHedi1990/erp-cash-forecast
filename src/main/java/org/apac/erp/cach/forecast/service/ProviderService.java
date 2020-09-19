package org.apac.erp.cach.forecast.service;

import java.util.ArrayList;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.CustomerDTO;
import org.apac.erp.cach.forecast.dtos.ProviderDTO;
import org.apac.erp.cach.forecast.persistence.entities.Company;
import org.apac.erp.cach.forecast.persistence.entities.Contact;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.entities.Provider;
import org.apac.erp.cach.forecast.persistence.repositories.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderService {

	@Autowired
	private ProviderRepository providerRepo;

	public Provider saveProvider(Provider provider) {
		return this.providerRepo.save(provider);
	}
	
	public List<Provider> getAllProviders() {
		return this.providerRepo.findAll();
	}
	
	public Provider getProviderById(Long providerId) {
		return this.providerRepo.findOne(providerId);
	}

	public void deleteProvider(Long providerId) {
		 this.providerRepo.delete(providerId);
		
	}
	
}
