package org.apac.erp.cach.forecast.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apac.erp.cach.forecast.constants.Utils;
import org.apac.erp.cach.forecast.dtos.InvoicesProviderPayment;
import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.persistence.entities.CustomerAttachedInvoices;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.persistence.entities.Provider;
import org.apac.erp.cach.forecast.persistence.entities.ProviderAttachedInvoices;
import org.apac.erp.cach.forecast.persistence.entities.ProviderInvoice;
import org.apac.erp.cach.forecast.persistence.repositories.PaymentRuleRepository;
import org.apac.erp.cach.forecast.persistence.repositories.ProviderAttachedInvoicesRepository;
import org.apac.erp.cach.forecast.persistence.repositories.ProviderInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
	
	@Autowired
	private ProviderAttachedInvoicesRepository providerAttachedInvoicesRepository;

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
					rule.setProvider(savedInvoice.getProvider());
					paymentRuleRepository.save(rule);
				});
			}
		}
		return savedInvoice;
	}
	
	/*public List<ProviderInvoice> payInvoices(InvoicesProviderPayment invoicePayment) {
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
	}*/


	public ProviderAttachedInvoices payInvoices(InvoicesProviderPayment invoicePayment) {
		
		String externalId = "";
		invoicePayment.setSelectedInvoices(invoicePayment.getSelectedInvoices().stream().sorted(Comparator.comparing(ProviderInvoice::getInvoiceId))
				.collect(Collectors.toList()));
		for(ProviderInvoice invoice: invoicePayment.getSelectedInvoices()) {
			if(externalId.isEmpty()) {
				externalId = ""+invoice.getInvoiceId();
			} else {
				externalId = externalId + "." + invoice.getInvoiceId();
			}
		}
		
		
		ProviderAttachedInvoices attachedInvoices = this.providerAttachedInvoicesRepository.findByExternalId(externalId);
		
		if(attachedInvoices == null) {
			attachedInvoices = new ProviderAttachedInvoices();
			attachedInvoices.setInvoices(new ArrayList<>());
			attachedInvoices.setPaymentRules(new ArrayList<>());
			for(ProviderInvoice invoice : invoicePayment.getSelectedInvoices()) {

				attachedInvoices.getInvoices().add(invoice);
				attachedInvoices.setTotalRequiredAmount(
						attachedInvoices.getTotalRequiredAmount() + invoice.getInvoiceNet());

			}
			attachedInvoices.setExternalId(externalId);
		}
		
		for(ProviderInvoice invoice : invoicePayment.getSelectedInvoices()) {

			List<PaymentRule> paymentRules = invoice.getInvoicePaymentRules();
			if (paymentRules != null) {
				for (PaymentRule paymentRule : paymentRules) {
					attachedInvoices.setTotalPaidAmount(
							attachedInvoices.getTotalPaidAmount() + paymentRule.getPaymentRuleAmount());
				}
				
			}
			invoice.setAssociationNumber(invoice.getAssociationNumber() + 1);
			this.invoiceService.saveInvoice(invoice);
		}
		attachedInvoices.setTotalPaidAmount(attachedInvoices.getTotalPaidAmount() + invoicePayment.getPaymentRule().getPaymentRuleAmount());
		if (attachedInvoices.getTotalPaidAmount() == attachedInvoices.getTotalRequiredAmount()) {
			attachedInvoices.getInvoices().stream().forEach(invoice -> {
			invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
			invoice.setInvoicePayment(invoice.getInvoiceTotalAmount());
			this.invoiceService.saveInvoice(invoice);
			});
		}
		invoicePayment.getPaymentRule().setRelatedToAnAttachedInvoices(true);
		attachedInvoices.getPaymentRules().add(invoicePayment.getPaymentRule());
		ProviderAttachedInvoices savedInvoices = this.providerAttachedInvoicesRepository.save(attachedInvoices);
		savedInvoices.getPaymentRules().get(savedInvoices.getPaymentRules().size() - 1).setAttachedInvoicesId(attachedInvoices.getAttachedInvoicesId());
		savedInvoices.getPaymentRules().get(savedInvoices.getPaymentRules().size() - 1).setProvider(invoicePayment.getSelectedInvoices().get(0).getProvider());
		paymentRuleRepository.save(savedInvoices.getPaymentRules().get(savedInvoices.getPaymentRules().size() - 1));
		return savedInvoices;
	}
	public List<ProviderInvoice> findAllProviderInvoicesByProviderId(Long providerId) {
		Provider provider = providerService.getProviderById(providerId);
		if(provider != null) {
			return this.providerInvoiceRepo.findByProvider(provider);
		}
		else return null;
	}
	public List<ProviderInvoice> findAllProviderInvoicesByProviderIdAndIsOpened(Long providerId) {
		Provider provider = providerService.getProviderById(providerId);
		if(provider != null) {
			return this.providerInvoiceRepo.findAllByProviderAndInvoiceStatus(provider,InvoiceStatus.OPENED);
		}
		else return null;
	}
	public ProviderInvoice getProviderInvoiceById(Long invoiceId) {
		return this.providerInvoiceRepo.findOne(invoiceId);
	}
	
	private List<ProviderInvoice> findAllProviderInvoicesAttached_private() {
		List<ProviderAttachedInvoices> providerAttachedInvoices = this.providerAttachedInvoicesRepository.findAll();
		List<ProviderInvoice> providerInvoices = new ArrayList<ProviderInvoice>();
		providerAttachedInvoices.forEach(attachedInvoice -> {
			ProviderInvoice invoice = new ProviderInvoice();
			invoice.setInvoiceNumber("");
			String invoiceNum = "";
			String invoiceDates = "";
			String invoiceDeadlineDates = "";
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			for(ProviderInvoice providerInvoice : attachedInvoice.getInvoices()) {
				if(invoiceNum.isEmpty()) {
					invoiceNum = providerInvoice.getInvoiceNumber();
					invoiceDates = formatter.format(providerInvoice.getInvoiceDate());
					invoiceDeadlineDates = formatter.format(providerInvoice.getInvoiceDeadlineDate());
				} else {
					invoiceNum = invoiceNum + " / " + providerInvoice.getInvoiceNumber();
					invoiceDates = invoiceDates + " / " + formatter.format(providerInvoice.getInvoiceDate());
					invoiceDeadlineDates = invoiceDeadlineDates + " / " + formatter.format(providerInvoice.getInvoiceDeadlineDate());

				}
				invoice.setProvider(providerInvoice.getProvider());
				invoice.setInvoiceDate(providerInvoice.getInvoiceDate());
				invoice.setInvoiceDeadlineDate(providerInvoice.getInvoiceDeadlineDate());
			}
			invoice.setInvoiceNumber(invoiceNum);
			invoice.setAssocited(true);
			invoice.setInvoiceStatus(attachedInvoice.getInvoices().get(0).getInvoiceStatus());
			invoice.setInvoiceNet(attachedInvoice.getTotalRequiredAmount());
			invoice.setInvoiceNetS(attachedInvoice.getTotalRequiredAmountS());
			invoice.setInvoicePayment(attachedInvoice.getTotalPaidAmount());
			invoice.setInvoiceTotalAmountS(attachedInvoice.getTotalRequiredAmountS());
			invoice.setInvoicePaymentS(attachedInvoice.getTotalPaidAmountS());
			invoice.setInvoiceCurrency(attachedInvoice.getInvoices().get(0).getInvoiceCurrency());
			invoice.setInvoicePaymentRules(attachedInvoice.getPaymentRules());
			providerInvoices.add(invoice);
			invoice.setInvoiceDates(invoiceDates);
			int rand = (int)(Math.random() * (10000 - 8050)) + 8050; //random start and end values
			invoice.setInvoiceId((long) rand);
			invoice.setInvoiceDeadlineDates(invoiceDeadlineDates);
			invoice.setAssociatedAttachedInvoicesId(attachedInvoice.getAttachedInvoicesId());
		});
		return providerInvoices;
	}
	
	public List<ProviderInvoice> findAllProviderInvoicesAttached() {
		List<ProviderInvoice> providerInvoices = this.findAllProviderInvoices();
		List<ProviderInvoice> attachedProviderInvoices = this.findAllProviderInvoicesAttached_private();

		List<ProviderInvoice> allInvoices = new ArrayList<>();
		allInvoices.addAll(providerInvoices);
		allInvoices.addAll(attachedProviderInvoices);

		allInvoices = allInvoices.stream().sorted(Comparator.comparing(ProviderInvoice::getInvoiceNumber))
				.collect(Collectors.toList());
		return allInvoices;
	}
	
	public List<ProviderInvoice> findByInvoiceStatusAndInvoiceDeadlineDate(InvoiceStatus invoiceStatus, Date startDate, Date endDate) {
		return this.providerInvoiceRepo.findByInvoiceStatusAndInvoiceDeadlineDateBetween(invoiceStatus, startDate, endDate);
	}
	
	public List<ProviderInvoice> findByInvoiceStatusAndInvoiceDeadlineDate(InvoiceStatus invoiceStatus, Date startDate) {
		return this.providerInvoiceRepo.findByInvoiceStatusAndInvoiceDeadlineDateBefore(invoiceStatus, startDate);
	}
}
