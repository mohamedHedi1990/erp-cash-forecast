package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.service.ProviderAttachedInvoicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/provider-attahced-invoices")
public class ProviderAttachedInvoices {
	
	@Autowired
	private ProviderAttachedInvoicesService providerAttachedInvoicesService;
	
	@CrossOrigin
	@DeleteMapping("/{id}")
	public void deleteProviderAttachedInvoices(@PathVariable("id") Long id) {
		this.providerAttachedInvoicesService.deleteAttachedInvoices(id);
	}

}
