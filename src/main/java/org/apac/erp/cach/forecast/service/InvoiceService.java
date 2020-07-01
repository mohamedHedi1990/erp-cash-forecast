package org.apac.erp.cach.forecast.service;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.persistence.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

	@Autowired
	private InvoiceRepository invoiceRepo;

	public List<Invoice> findAllInvoices() {
		return invoiceRepo.findAll();
	}

	public Invoice saveNewInvoice(Invoice invoice) {
		return invoiceRepo.save(invoice);
	}

	public Invoice findInvoiceById(Long invoiceId) {
		return invoiceRepo.findOne(invoiceId);
	}
	
	public long betweenDates(java.util.Date date, java.util.Date date2) throws IOException
	{
	    return ChronoUnit.DAYS.between(date.toInstant(), date2.toInstant());
	}

}
