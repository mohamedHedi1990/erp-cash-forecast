package org.apac.erp.cach.forecast.service;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;


import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.persistence.entities.Provider;
import org.apac.erp.cach.forecast.persistence.entities.ProviderInvoice;
import org.apac.erp.cach.forecast.persistence.repositories.PaymentRuleRepository;
import org.apac.erp.cach.forecast.persistence.repositories.ProviderInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apac.erp.cach.forecast.dtos.InvoicesProviderPayment;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;


@Service
public class ProviderInvoiceService {
	@Autowired
	private ProviderInvoiceRepository providerInvoiceRepo;

	@Autowired
	private ProviderService providerService;

	@Autowired
	private InvoiceService invoiceService;
	@Autowired
	private PaymentRuleRepository paymentRuleRepository;

	public List<ProviderInvoice> findAllProviderInvoices() {
		return providerInvoiceRepo.findAllByOrderByInvoiceDateDesc();
	}

	public ProviderInvoice saveProviderInvoice(ProviderInvoice invoice) {
		invoice.setInvoiceStatus(InvoiceStatus.OPENED);
        ProviderInvoice providerInvoice=invoice;
		try {
			long days = invoiceService.betweenDates(invoice.getInvoiceDate(), invoice.getInvoiceDeadlineDate());
			invoice.setInvoiceDeadlineInNumberOfDays((int) days);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(invoice.getInvoiceId()!=null)
		{

			if(invoice.getInvoiceTotalAmount().compareTo(invoice.getInvoicePayment())==0) {

				invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
			}
		}

		ProviderInvoice savedInvoice = providerInvoiceRepo.save(invoice);
		if(providerInvoice.getInvoiceId()!=null) {
			if(providerInvoice.getInvoicePaymentRules()!=null) {
				providerInvoice.getInvoicePaymentRules().forEach(rule -> {
					rule.setInvoice(savedInvoice);
					paymentRuleRepository.save(rule);
				});
			}
		}
		return savedInvoice;
	}
	
	public List<ProviderInvoice> payInvoices(InvoicesProviderPayment invoicePayment) {
				invoicePayment.getSelectedInvoices().stream().forEach(invoice -> {
			List<PaymentRule> paymentRules = invoice.getInvoicePaymentRules();
			if(paymentRules == null) {
				paymentRules = new ArrayList<PaymentRule>();
			}
			if(invoicePayment.getPaymentRule().getPaymentRuleInvoices() == null) {
				invoicePayment.getPaymentRule().setPaymentRuleInvoices(""+invoice.getInvoiceId());
			} else {
				invoicePayment.getPaymentRule().setPaymentRuleInvoices(invoicePayment.getPaymentRule().getPaymentRuleInvoices() + ","+invoice.getInvoiceId());
			}
			paymentRules.add(invoicePayment.getPaymentRule());
			invoice.setInvoicePayment(invoice.getInvoiceTotalAmount());
			invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
			invoice.setInvoicePaymentRules(paymentRules);
		});

		return this.providerInvoiceRepo.save(invoicePayment.getSelectedInvoices());
	}

	public List<ProviderInvoice> findAllProviderInvoicesByProviderId(Long providerId) {
		Provider provider = providerService.getProviderById(providerId);
		if(provider != null) {
			return this.providerInvoiceRepo.findByProvider(provider);
		}
		else return null;
	}
	
	public ProviderInvoice getProviderInvoiceById(Long invoiceId) {
		return this.providerInvoiceRepo.findOne(invoiceId);
	}
}
