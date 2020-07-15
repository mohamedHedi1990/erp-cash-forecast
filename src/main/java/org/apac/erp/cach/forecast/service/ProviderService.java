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

	@Autowired
	private ContactService contactService;

	public List<ProviderDTO> findAllProvides() {

		List<ProviderDTO> dtos = new ArrayList<ProviderDTO>();
		List<Provider> providers = providerRepo.findAll();
		providers.stream().forEach(provider -> {
			ProviderDTO dto = new ProviderDTO(provider.getProviderId(), provider.getProviderLabel(),
					provider.getProviderAddress(), provider.getProviderUniqueIdentifier(),
					provider.getProviderManagerName(), provider.getProviderContactNumber(), provider.getCreatedAt(),
					provider.getUpdatedAt());
			dtos.add(dto);
		});

		return dtos;
	}

	public Provider saveNewProvider(Provider provider) {
		List<Contact> contacts = provider.getProviderContacts();
		contacts.stream().forEach(contact -> contactService.saveNewContact(contact));
		
		return providerRepo.save(provider);
	}

	public Provider findProviderById(Long providerId) {
		return providerRepo.findOne(providerId);
	}

	public void deleteCompany(Long providerId) {
		providerRepo.delete(providerId);
	}
}
