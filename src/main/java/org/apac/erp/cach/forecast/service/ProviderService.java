package org.apac.erp.cach.forecast.service;

import java.util.ArrayList;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.CustomerDTO;
import org.apac.erp.cach.forecast.dtos.ProviderDTO;
import org.apac.erp.cach.forecast.persistence.entities.Company;
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
	private CompanyService companyService;

	public List<ProviderDTO> findAllProvides() {

		List<ProviderDTO> dtos = new ArrayList<ProviderDTO>();
		List<Provider> providers = providerRepo.findAll();
		providers.stream().forEach(provider -> {
			ProviderDTO dto = new ProviderDTO(provider.getProviderId(), provider.getProviderLabel(),
					provider.getProviderAddress(), provider.getProviderUniqueIdentifier(),
					provider.getProviderManagerName(), provider.getProviderContactNumber(), provider.getCreatedAt(),
					provider.getUpdatedAt(), provider.getProviderCompany().getCampanyName());
			dtos.add(dto);
		});

		return dtos;
	}

	public Provider saveNewProviderToGvenCompany(Provider provider, Long companyId) {
		Company company = companyService.findCompanyById(companyId);
		if (company != null) {
			provider.setProviderCompany(company);
			return providerRepo.save(provider);
		} else {
			return null;
		}

	}

	public Provider findProviderById(Long providerId) {
		return providerRepo.findOne(providerId);
	}

	public void deleteCompany(Long providerId) {
		providerRepo.delete(providerId);
	}
}
