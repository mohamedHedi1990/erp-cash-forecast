package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CutomerInvoiceService {
	
	@Autowired
	private CustomerInvoiceRepository customerInvoiceRepo;
	
	public List<CustomerInvoice> findAllCustomerInvoices() {
		return customerInvoiceRepo.findAll();
	}
}
