package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.repositories.ProviderInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderInvoiceService {

	@Autowired
	private ProviderInvoiceRepository providerInvoiceRepo;

}
