package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

	@Autowired
	private InvoiceRepository invoiceRepo;

}
