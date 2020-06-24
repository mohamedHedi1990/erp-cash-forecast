package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.repositories.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderService {

	@Autowired
	private ProviderRepository providerRepo;

}
