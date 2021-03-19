package org.apac.erp.cach.forecast.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.enumeration.OperationDtoType;
import org.apac.erp.cach.forecast.persistence.entities.CustomerAttachedInvoices;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.persistence.entities.ProviderAttachedInvoices;
import org.apac.erp.cach.forecast.persistence.entities.ProviderInvoice;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerAttachedInvoicesRepository;
import org.apac.erp.cach.forecast.persistence.repositories.CustomerInvoiceRepository;
import org.apac.erp.cach.forecast.persistence.repositories.InvoiceRepository;
import org.apac.erp.cach.forecast.persistence.repositories.PaymentRuleRepository;
import org.apac.erp.cach.forecast.persistence.repositories.ProviderAttachedInvoicesRepository;
import org.apac.erp.cach.forecast.persistence.repositories.ProviderInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {
	
	@Autowired
	private InvoiceRepository invoiceRepo;
    @Autowired
	private PaymentRuleRepository paymentRuleRepository;
    
    @Autowired
    private PaymentRuleService paymentRuleService;
    
    @Autowired
    private CustomerInvoiceRepository customerInvoiceRepo;
    
    @Autowired
    private ProviderInvoiceRepository providerInvoiceRepo;
    
    @Autowired
	private CustomerAttachedInvoicesRepository customerAttachedInvocieRepo;

	@Autowired
	private ProviderAttachedInvoicesRepository providerAttachedInvocieRepo;
	
	public long betweenDates(Date date, Date date2) throws IOException {
		LocalDate localdate1 = date.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate();
		LocalDate localdate2 = date2.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate();
		long noOfDaysBetween = ChronoUnit.DAYS.between(localdate1, localdate2);
		return noOfDaysBetween;
	}
	
	public Invoice findInvoiceById(Long invoiceId) {
		return invoiceRepo.findOne(invoiceId);
	}
	
	public void deleteInvoice(Long invoiceId) {
		invoiceRepo.delete(invoiceId);
	}
	public Invoice saveInvoice(Invoice invoice) {
		return invoiceRepo.save(invoice);
	}
/*
	@Autowired
	private InvoiceRepository invoiceRepo;

	@Autowired
	private BankAccountService bankAccountService;

	public List<Invoice> findAllInvoices() {
		return invoiceRepo.findAll();
	}

	public Invoice saveNewInvoice(Invoice invoice) {
		return invoiceRepo.save(invoice);
	}

	public Invoice findInvoiceById(Long invoiceId) {
		return invoiceRepo.findOne(invoiceId);
	}

	public long betweenDates(java.util.Date date, java.util.Date date2) throws IOException {
		return ChronoUnit.DAYS.between(date.toInstant(), date2.toInstant());
	}

	public void updateInvoiceWithPaymentRule(Invoice invoice, InvoiceType invoiceType, PaymentRule paymentRule,
			Long accountId) {
		BankAccount bankAccount = bankAccountService.findById(accountId);
		Bank associatedBank = bankAccount.getBankAccountAgency().getAgencyBank();
		Double paymentAmountAfterCommission = paymentRule.getPaymentRuleAmount();
		
		//TODO update amount with correct formulas
		switch (paymentRule.getPaymentRulePaymentMethod()) {
			case CHEQUE:
				paymentAmountAfterCommission = paymentAmountAfterCommission - (associatedBank.getBankCheckPermissionCommission() * paymentAmountAfterCommission / 100);
				break;
			case TRAITE:
				paymentAmountAfterCommission = paymentAmountAfterCommission - (associatedBank.getBankRemittanceOfDiscountCommission() * paymentAmountAfterCommission / 100);
				break;
			case VIREMENT:
				paymentAmountAfterCommission = paymentAmountAfterCommission - (associatedBank.getBankTransferCommission() * paymentAmountAfterCommission / 100);
				break;
			case EFFET:
				paymentAmountAfterCommission = paymentAmountAfterCommission - (associatedBank.getBankInterestRateCommission() * paymentAmountAfterCommission / 100);
				break;
			default:
				break;
			}
		
		Double payment = invoice.getInvoicePayment() + paymentAmountAfterCommission;
		invoice.setInvoicePayment(payment);
		if (payment == invoice.getInvoiceTotalAmount()) {
			invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
		}
		invoiceRepo.save(invoice);
	}
*/

	public Invoice addPaymentRuleForInvoice(Long invoiceId, PaymentRule paymentRule, OperationDtoType operationType) {
		paymentRule.setPaymentRuleInvoices(""+invoiceId);
		if(operationType == OperationDtoType.REGLEMENT_FACTURE_CLIENT) {
			CustomerInvoice customerInvoice = this.customerInvoiceRepo.findOne(invoiceId);
			paymentRule.setCustomer(customerInvoice.getCustomer());
		} else {
			ProviderInvoice providerInvoice = this.providerInvoiceRepo.findOne(invoiceId);
			paymentRule.setProvider(providerInvoice.getProvider());
		}
		paymentRule.setInvoice(invoiceRepo.findOne(invoiceId));
		Invoice  invoice = findInvoiceById(invoiceId);
		List<PaymentRule> paymentRules = invoice.getInvoicePaymentRules();
		if(paymentRules == null) {
			paymentRules = new ArrayList<PaymentRule>();
		}
		paymentRules.add(paymentRule);
		invoice.setInvoicePaymentRules(paymentRules);
		double newAmount = invoice.getInvoicePayment() + paymentRule.getPaymentRuleAmount();
		newAmount = (double)(Math.round(newAmount * 1000))/1000;
		invoice.setInvoicePayment(newAmount);
		
		if(Double.compare(invoice.getInvoicePayment(),invoice.getInvoiceNet()) == 0) {
			invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
		}
		Invoice savedInvoice =  this.invoiceRepo.save(invoice);
		return savedInvoice;
	}

	public Invoice updatePaymentRuleForInvoice(Long invoiceId, PaymentRule paymentRule) {
		paymentRule.setPaymentRuleInvoices(""+invoiceId);
		paymentRule.setInvoice(invoiceRepo.findOne(invoiceId));
		PaymentRule paymentOlder=paymentRuleRepository.findOne(paymentRule.getPaymentRuleId());
		Double paymentolders= paymentOlder.getPaymentRuleAmount();
		Invoice  invoice = findInvoiceById(invoiceId);
		PaymentRule paymentRuleUpdate=paymentRuleRepository.save(paymentRule);
		Double paymentNew=paymentRule.getPaymentRuleAmount();
		Double paymentOlderInvoice=invoice.getInvoicePayment();
		Double newMontant=paymentOlderInvoice-paymentolders;
		newMontant=newMontant+ paymentRule.getPaymentRuleAmount();
		newMontant = (double)(Math.round(newMontant * 1000))/1000;
		
		invoice.setInvoicePayment(newMontant);
		if(Double.compare(invoice.getInvoicePayment(),invoice.getInvoiceNet()) == 0) {
			invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
		}
		else if(Double.compare(invoice.getInvoicePayment(),invoice.getInvoiceTotalAmount()) != 0) {
			invoice.setInvoiceStatus(InvoiceStatus.OPENED);
		}
		Invoice savedInvoice =  this.invoiceRepo.save(invoice);

		return savedInvoice;
	}
	
	public Invoice updatePaymentRule(Long invoiceId, PaymentRule paymentRule, String context) {
		PaymentRule oldPaymentRule = paymentRuleService.findPaymentRuleBYId(paymentRule.getPaymentRuleId());
		if (oldPaymentRule.isRelatedToAnAttachedInvoices() == true) {
			if (context.equals("CUSTOMER")) {
				CustomerAttachedInvoices attachedInvoices = this.customerAttachedInvocieRepo
						.findOne(oldPaymentRule.getAttachedInvoicesId());
				attachedInvoices.setTotalPaidAmount(
						attachedInvoices.getTotalPaidAmount() - oldPaymentRule.getPaymentRuleAmount());
				attachedInvoices.setTotalPaidAmount(
						attachedInvoices.getTotalPaidAmount() + paymentRule.getPaymentRuleAmount());
				attachedInvoices.getInvoices().stream().forEach(invoice -> {
					if (attachedInvoices.getTotalPaidAmount() == attachedInvoices.getTotalRequiredAmount()) {
						invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
						invoice.setInvoicePayment(invoice.getInvoiceTotalAmount());
						
					} else {
						invoice.setInvoiceStatus(InvoiceStatus.OPENED);
						invoice.setInvoicePayment(0.0);
					}
					this.saveInvoice(invoice);
				});
				this.customerAttachedInvocieRepo.save(attachedInvoices);
				this.paymentRuleRepository.save(paymentRule);
				return attachedInvoices.getInvoices().get(0);
			} else if (context.equals("PROVIDER")) {
				ProviderAttachedInvoices attachedInvoices = this.providerAttachedInvocieRepo
						.findOne(oldPaymentRule.getAttachedInvoicesId());
				attachedInvoices.setTotalPaidAmount(
						attachedInvoices.getTotalPaidAmount() - oldPaymentRule.getPaymentRuleAmount());
				attachedInvoices.setTotalPaidAmount(
						attachedInvoices.getTotalPaidAmount() + paymentRule.getPaymentRuleAmount());
				attachedInvoices.getInvoices().stream().forEach(invoice -> {
					if (attachedInvoices.getTotalPaidAmount() == attachedInvoices.getTotalRequiredAmount()) {
						invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
						invoice.setInvoicePayment(invoice.getInvoiceTotalAmount());
						
					} else {
						invoice.setInvoiceStatus(InvoiceStatus.OPENED);
						invoice.setInvoicePayment(0.0);
					}
					this.saveInvoice(invoice);
				});
				this.providerAttachedInvocieRepo.save(attachedInvoices);
				this.paymentRuleRepository.save(paymentRule);
				return attachedInvoices.getInvoices().get(0);
			}

			
			
		} else {
			return this.updatePaymentRuleForInvoice(invoiceId, paymentRule);
		}
		
		return null;
	}

	public Invoice closeInvoice(Long invoiceId) {
		Invoice invoice = findInvoiceById(invoiceId);
		invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
		return this.saveInvoice(invoice);
		
	}
}
