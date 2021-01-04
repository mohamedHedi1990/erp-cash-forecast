package org.apac.erp.cach.forecast.service;

import java.util.ArrayList;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.CustomerDTO;
import org.apac.erp.cach.forecast.dtos.ProviderDTO;
import org.apac.erp.cach.forecast.persistence.entities.Company;
import org.apac.erp.cach.forecast.persistence.entities.Contact;
import org.apac.erp.cach.forecast.persistence.entities.Customer;
import org.apac.erp.cach.forecast.persistence.entities.Provider;
import org.apac.erp.cach.forecast.persistence.repositories.ContactRepository;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerInvoiceRepository;
import org.apac.erp.cach.forecast.persistence.repositories.ProviderInvoiceRepository;
import org.apac.erp.cach.forecast.persistence.repositories.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderService {

	@Autowired
	private ProviderRepository providerRepo;

	@Autowired
	private ContactRepository contactRepository;
	@Autowired
	private ProviderInvoiceRepository providerInvoiceRepository;

	public Provider saveProvider(Provider provider) {
		return this.providerRepo.save(provider);
	}
	
	public List<Provider> getAllProviders() {
		return this.providerRepo.findAllByOrderByProviderLabel();
	}
	
	public Provider getProviderById(Long providerId) {
		return this.providerRepo.findOne(providerId);
	}

	public void deleteProvider(Long providerId) {
		Provider provider=this.providerRepo.findOne(providerId);
		provider.getProviderContacts().forEach(c -> {
			this.contactRepository.delete(c);
		});
		this.providerInvoiceRepository.findByProvider(provider).forEach(PI->{
			this.providerInvoiceRepository.delete(PI);
		});
		 this.providerRepo.delete(providerId);
		
	}
	
}
