package org.apac.erp.cach.forecast.service;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.persistence.repositories.InvoiceRepository;
import org.apac.erp.cach.forecast.persistence.repositories.PaymentRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {
	
	@Autowired
	private InvoiceRepository invoiceRepo;
    @Autowired
	private PaymentRuleRepository paymentRuleRepository;
	
	public long betweenDates(java.util.Date date, java.util.Date date2) throws IOException {
		return ChronoUnit.DAYS.between(date.toInstant(), date2.toInstant());
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

	public Invoice addPaymentRuleForInvoice(Long invoiceId, PaymentRule paymentRule) {
		paymentRule.setPaymentRuleInvoices(""+invoiceId);
		paymentRule.setInvoice(invoiceRepo.findOne(invoiceId));
		Invoice  invoice = findInvoiceById(invoiceId);
		List<PaymentRule> paymentRules = invoice.getInvoicePaymentRules();
		if(paymentRules == null) {
			paymentRules = new ArrayList<PaymentRule>();
		}
		paymentRules.add(paymentRule);
		invoice.setInvoicePaymentRules(paymentRules);
		invoice.setInvoicePayment(invoice.getInvoicePayment() + paymentRule.getPaymentRuleAmount());
		if(Double.compare(invoice.getInvoicePayment(),invoice.getInvoiceTotalAmount()) == 0) {
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
		System.out.println("older payment invoice"+paymentOlderInvoice);
		System.out.println("payment older"+paymentolders);
		System.out.println("payment new:"+paymentNew);
		Double newMontant=paymentOlderInvoice-paymentolders;
		System.out.println("montant without payment older :"+paymentOlderInvoice+"-"+paymentolders+"="+newMontant);
		newMontant=newMontant+ paymentRule.getPaymentRuleAmount();
		System.out.println("montant with new  payment  "+newMontant);

		System.out.println("payment Update:"+paymentRule.getPaymentRuleAmount());
		System.out.println("payment rule older"+paymentRule);
		System.out.println("payment rule new "+paymentRuleUpdate);
		System.out.println("new payment "+newMontant);
		invoice.setInvoicePayment(newMontant);
		if(Double.compare(invoice.getInvoicePayment(),invoice.getInvoiceTotalAmount()) == 0) {
			invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
		}
		System.out.println("invoiceto save "+invoice.toString());
		Invoice savedInvoice =  this.invoiceRepo.save(invoice);
		System.out.println("invoice saved "+invoice.toString());

		return savedInvoice;
	}
}
