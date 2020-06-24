package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.ProviderInvoice;
import org.apac.erp.cach.forecast.persistence.repositories.ProviderInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderInvoiceService {

	@Autowired
	private ProviderInvoiceRepository providerInvoiceRepo;

	public List<ProviderInvoice> findAllProviderInvoices() {
		return providerInvoiceRepo.findAll();
	}

}
