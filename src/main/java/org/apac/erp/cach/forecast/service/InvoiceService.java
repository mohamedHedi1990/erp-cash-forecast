package org.apac.erp.cach.forecast.service;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.apac.erp.cach.forecast.enumeration.InvoiceType;
import org.apac.erp.cach.forecast.persistence.entities.Bank;
import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.persistence.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

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
		
		invoice.setInvoicePayment(invoice.getInvoicePayment() + paymentAmountAfterCommission);
		invoiceRepo.save(invoice);
	}

}
