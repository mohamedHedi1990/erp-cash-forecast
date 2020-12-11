package org.apac.erp.cach.forecast.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.enumeration.OperationType;
import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.HistoricAccountSold;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.persistence.repositories.PaymentRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentRuleService {

	@Autowired
	private PaymentRuleRepository paymentRuleRepo;

	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private BankAccountService accounttService;
	
	@Autowired
	private HistoricAccountSoldService historicAccountSoldService;

	
	public PaymentRule findPaymentRuleBYId(Long paymentRuleId) {
		return paymentRuleRepo.findOne(paymentRuleId);
	}
	
	public PaymentRule validatePaymentRule(Long paymentRuleId) {
		
		PaymentRule paymentRule = findPaymentRuleBYId(paymentRuleId);
		if(paymentRule != null) {
			
			paymentRule.setValidated(true);
			paymentRule = this.paymentRuleRepo.save(paymentRule);
			
			BankAccount account = paymentRule.getPaymentRuleAccount();
			if(paymentRule.getPaymentRuleOperationType() == OperationType.DECAISSEMENT) {
				account.setAccountInitialAmount(account.getAccountInitialAmount() - paymentRule.getPaymentRuleAmount());
			} else {
				account.setAccountInitialAmount(account.getAccountInitialAmount() + paymentRule.getPaymentRuleAmount());
			}
			accounttService.saveAccount(account);
			HistoricAccountSold historicSolde = new HistoricAccountSold(account, account.getAccountInitialAmount(), new Date());
			historicAccountSoldService.saveHistoricSolde(historicSolde);
			
			return paymentRule;
		}
		return null;
		
	}
	
	public PaymentRule modifyPaymentRule(PaymentRule newPaymentRule) {
		PaymentRule paymentRule = findPaymentRuleBYId(newPaymentRule.getPaymentRuleId());
		if(paymentRule != null ) {
			if(paymentRule.getPaymentRuleAmount() != newPaymentRule.getPaymentRuleAmount()) {
				String paymentRuleInvoices = paymentRule.getPaymentRuleInvoices();
				String [] splitTab = paymentRuleInvoices.split(",");
				Arrays.stream(splitTab).forEach(invoiceIdS -> {
					long invoiceId = Long.parseLong(invoiceIdS);
					Invoice invoice = invoiceService.findInvoiceById(invoiceId);
					invoice.setInvoicePayment(invoice.getInvoicePayment() - paymentRule.getPaymentRuleAmount());
					invoice.setInvoicePayment(invoice.getInvoicePayment() + newPaymentRule.getPaymentRuleAmount());
					if(invoice.getInvoicePayment() == invoice.getInvoiceTotalAmount()) {
						invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
					} else {
						invoice.setInvoiceStatus(InvoiceStatus.OPENED);
					}
					invoiceService.saveInvoice(invoice);
				});
			
				/*
				
				BankAccount account = paymentRule.getPaymentRuleAccount();
				BankAccount newAccount = newPaymentRule.getPaymentRuleAccount();
				if(account.getAccountId() != newAccount.getAccountId()) {
					account.setAccountInitialAmount(account.getAccountInitialAmount() - paymentRule.getPaymentRuleAmount());
					newAccount.setAccountInitialAmount(newAccount.getAccountInitialAmount() + paymentRule.getPaymentRuleAmount());
					accounttService.saveAccount(account);
					accounttService.saveAccount(newAccount);

				} else {
					account.setAccountInitialAmount(account.getAccountInitialAmount() - paymentRule.getPaymentRuleAmount());
					account.setAccountInitialAmount(account.getAccountInitialAmount() + paymentRule.getPaymentRuleAmount());
					accounttService.saveAccount(account);
					accounttService.saveAccount(account);
				}
				
				*/
				
				
			}
		}
		
		
		return paymentRuleRepo.save(newPaymentRule);
	}
	
	public void deletePaymentRule(Long paymentRuleId) {
		PaymentRule paymentRule = findPaymentRuleBYId(paymentRuleId);
		if(paymentRule != null ) {
			String paymentRuleInvoices = paymentRule.getPaymentRuleInvoices();
			String [] splitTab = paymentRuleInvoices.split(",");
			Arrays.stream(splitTab).forEach(invoiceIdS -> {
				long invoiceId = Long.parseLong(invoiceIdS);
				Invoice invoice = invoiceService.findInvoiceById(invoiceId);
				invoice.setInvoicePayment(invoice.getInvoicePayment() - paymentRule.getPaymentRuleAmount());
				if(invoice.getInvoicePayment() == invoice.getInvoiceTotalAmount()) {
					invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
				} else {
					invoice.setInvoiceStatus(InvoiceStatus.OPENED);
				}
				invoiceService.saveInvoice(invoice);
			});
				
			
		}
		
		
		paymentRuleRepo.delete(paymentRuleId);
	}
	
	public List<PaymentRule> getAllPaymentRuleBetwwenTwoDates(BankAccount bankAccount, Date startDate, Date endDate) {
		return this.paymentRuleRepo.findByPaymentRuleAccountAndPaymentRuleDeadlineDateBetweenOrderByPaymentRuleDeadlineDateAsc(bankAccount,startDate, endDate);
	}
	
	/*

	public PaymentRule saveNewPaymentRuleToInvoice(PaymentRule paymentRule, Long invoiceId, Long accountId) {
		Invoice invoice = invoiceService.findInvoiceById(invoiceId);
		paymentRule.setInvoice(invoice);
		// TODO use correct formula of commissions applications
		invoiceService.updateInvoiceWithPaymentRule(invoice, InvoiceType.CUSTOMER, paymentRule, accountId);
		return paymentRuleRepo.save(paymentRule);
	}

	public List<PaymentRuleDTO> findAllCustomerInvoicesPaymentRules() {
		ArrayList<PaymentRuleDTO> paymentRules = new ArrayList<>();
		List<CustomerInvoice> customerInvoices = customerInvoiceService.findAllCustomerInvoices();
		// TODO to be refactored
		customerInvoices.stream().forEach(invoice -> {
			List<PaymentRule> invoicePaymentRules = new ArrayList<>();
			List<PaymentRule> paymenRules = paymentRuleRepo.findAll();
			paymenRules.stream().forEach(payment -> {
				if (payment.getInvoice() == invoice) {
					invoicePaymentRules.add(payment);
				}
			});
			
			
			
			PaymentRuleDTO paymentRuleDTO = new PaymentRuleDTO(invoice.getInvoiceId(), invoice.getInvoiceNumber(),
					invoice.getInvoiceDeadlineInNumberOfDays(), invoice.getInvoiceDeadlineDate(),
					invoice.getInvoiceDate(), invoice.getInvoiceTotalAmount(), invoice.getInvoiceRs(),
					invoice.getInvoiceNet(), invoice.getInvoicePayment(), 
					invoice.getCustomer().getCustomerLabel(),invoice.getCustomer().getCustomerId(),
					invoice.getCreatedAt(), invoice.getUpdatedAt(), invoice.getInvoiceStatus());
			paymentRuleDTO.setPayments(invoicePaymentRules);
			paymentRules.add(paymentRuleDTO);

		});

		return paymentRules;
	}

	public List<PaymentRuleDTO> findAllProviderInvoicesPaymentRules() {
		ArrayList<PaymentRuleDTO> paymentRules = new ArrayList<>();
		List<ProviderInvoice> providerInvoices = providerInvoiceService.findAllProviderInvoices();
		providerInvoices.stream().forEach(invoice -> {
			List<PaymentRule> invoicePaymentRules = new ArrayList<>();
			List<PaymentRule> paymenRules = paymentRuleRepo.findAll();
			paymenRules.stream().forEach(payment -> {
				if (payment.getInvoice() == invoice) {
					invoicePaymentRules.add(payment);
				}
			});
			PaymentRuleDTO paymentRuleDTO = new PaymentRuleDTO(invoice.getInvoiceId(), invoice.getInvoiceNumber(),
					invoice.getInvoiceDeadlineInNumberOfDays(), invoice.getInvoiceDeadlineDate(),
					invoice.getInvoiceDate(), invoice.getInvoiceTotalAmount(), invoice.getInvoiceRs(),
					invoice.getInvoiceNet(), invoice.getInvoicePayment(), 
					invoice.getProvider().getProviderLabel(),invoice.getProvider().getProviderId(),
					invoice.getCreatedAt(), invoice.getUpdatedAt(), invoice.getInvoiceStatus());
			paymentRuleDTO.setPayments(invoicePaymentRules);
			paymentRules.add(paymentRuleDTO);
		});
		return paymentRules;
	}

	public List<PaymentMethod> findAllPaymentMethods() {	
		PaymentMethod[] methodsArray = PaymentMethod.values();
		List<PaymentMethod> list = new ArrayList<PaymentMethod>();
		Collections.addAll(list, methodsArray);
		return list;
	}
*/
}
