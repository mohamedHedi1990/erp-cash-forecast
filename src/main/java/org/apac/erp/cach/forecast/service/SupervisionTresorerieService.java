package org.apac.erp.cach.forecast.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apac.erp.cach.forecast.dtos.OperationTreserorieDto;
import org.apac.erp.cach.forecast.enumeration.OperationType;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.persistence.entities.Decaissement;
import org.apac.erp.cach.forecast.persistence.entities.Encaissement;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.persistence.entities.ProviderInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupervisionTresorerieService {

	@Autowired
	private PaymentRuleService paymentRuleService;

	@Autowired
	private ProviderInvoiceService providerInvoiceService;

	@Autowired
	private CustomerInvoiceService customerInvoiceService;

	@Autowired
	private DecaissementService decaissementService;

	@Autowired
	private EncaissementService encaissementService;

	public List<OperationTreserorieDto> globalSupervision(Date startDate, Date endDate) {
		List<OperationTreserorieDto> operations = new ArrayList<OperationTreserorieDto>();

		// Trouver tout kles réglements sur cette intervalle
		List<PaymentRule> paymentRules = paymentRuleService.getAllPaymentRuleBetwwenTwoDates(startDate, endDate);
		List<OperationTreserorieDto> paymentRuleOperations = convertPaymentRulesToOperationTreserorieList(paymentRules);
		operations.addAll(paymentRuleOperations);

		// Trouver tout les decaissements sur cette période
		List<Decaissement> decaissements = decaissementService.findDecaissementsBetwwenTwoDates(startDate, endDate);
		List<OperationTreserorieDto> decaissementOperations = convertDecaissementsToOperationTreserorieList(
				decaissements);
		operations.addAll(decaissementOperations);

		// Trouver tout les encaissements sur cette période
		List<Encaissement> encaissements = encaissementService.findEncaissementsBetwwenTwoDates(startDate, endDate);
		List<OperationTreserorieDto> encaissementOperations = convertEncaissementsToOperationTreserorieList(
				encaissements);
		operations.addAll(encaissementOperations);

		operations = operations.stream().sorted(Comparator.comparing(OperationTreserorieDto::getOperationDate))
				.collect(Collectors.toList());

		return operations;
	}

	private List<OperationTreserorieDto> convertPaymentRulesToOperationTreserorieList(List<PaymentRule> paymentRules) {
		List<OperationTreserorieDto> operations = new ArrayList<OperationTreserorieDto>();
		paymentRules.forEach(paymentRule -> {
			OperationTreserorieDto operation = new OperationTreserorieDto();
			operation.setOpperationType(paymentRule.getPaymentRuleOperationType());
			operation.setOperationDate(paymentRule.getCreatedAt());
			operation.setOperationAmountS(paymentRule.getPaymentRuleAmountS());

			List<String> detailsPayements = new ArrayList<String>();
			detailsPayements.add(paymentRule.getPaymentRulePaymentMethod().toString());
			detailsPayements.add(paymentRule.getPaymentRuleNumber());
			detailsPayements.add(paymentRule.getPaymentRuleDetails());
			operation.setOpperationDetails(detailsPayements);

			// Ajouter une information sur la facture payé dans le label
			String label = "Payement de la facture ";
			String invoicesIds = paymentRule.getPaymentRuleInvoices();
			List<Invoice> invoices = new ArrayList<Invoice>();
			String[] ids = invoicesIds.split(",");

			Arrays.stream(ids).forEach(invoiceId -> {
				if (paymentRule.getPaymentRuleOperationType() == OperationType.DECAISSEMENT) {
					ProviderInvoice providerInvoice = providerInvoiceService
							.getProviderInvoiceById(Long.parseLong(invoiceId));
					operation.setOpperationCurrency(providerInvoice.getInvoiceCurrency());
					invoices.add(providerInvoice);
				} else {
					CustomerInvoice customerInvoice = customerInvoiceService
							.getCustomerInvoiceById(Long.parseLong(invoiceId));
					operation.setOpperationCurrency(customerInvoice.getInvoiceCurrency());
					invoices.add(customerInvoice);
				}

			});

			for (int i = 0; i < invoices.size(); i++) {
				label = label + invoices.get(i).getInvoiceNumber();
				if (i < invoices.size() - 1) {
					label = label + " et ";
				}
			}

			operation.setOpperationLabel(label);

			operations.add(operation);

		});

		return operations;
	}

	private List<OperationTreserorieDto> convertDecaissementsToOperationTreserorieList(
			List<Decaissement> decaissements) {

		List<OperationTreserorieDto> operations = new ArrayList<OperationTreserorieDto>();

		decaissements.forEach(decaissement -> {
			OperationTreserorieDto operation = new OperationTreserorieDto();
			operation.setOpperationType(OperationType.DECAISSEMENT);
			operation.setOperationDate(decaissement.getCreatedAt());
			operation.setOperationAmountS(decaissement.getDecaissementAmountS());

			List<String> detailsPayements = new ArrayList<String>();
			detailsPayements.add(decaissement.getDecaissementPaymentType().toString());
			detailsPayements.add(decaissement.getDecaissementPaymentRuleNumber());
			detailsPayements.add(decaissement.getDecaissementDetails());

			operation.setOpperationDetails(detailsPayements);

			operation.setOpperationLabel(decaissement.getDecaissementType().getDecaissementTypeLabel());

			operation.setOpperationFacultatifLabel(decaissement.getDecaissementLabel());

			operations.add(operation);

		});

		return operations;
	}

	private List<OperationTreserorieDto> convertEncaissementsToOperationTreserorieList(
			List<Encaissement> encaissements) {

		List<OperationTreserorieDto> operations = new ArrayList<OperationTreserorieDto>();

		encaissements.forEach(encaissement -> {
			OperationTreserorieDto operation = new OperationTreserorieDto();
			operation.setOpperationType(OperationType.ENCAISSEMENT);
			operation.setOperationDate(encaissement.getCreatedAt());
			operation.setOperationAmountS(encaissement.getEncaissementAmountS());

			List<String> detailsPayements = new ArrayList<String>();
			detailsPayements.add(encaissement.getEncaissementPaymentType().toString());
			detailsPayements.add(encaissement.getEncaissementPaymentRuleNumber());
			detailsPayements.add(encaissement.getEncaissementDetails());

			operation.setOpperationDetails(detailsPayements);

			operation.setOpperationLabel(encaissement.getEncaissementType().getEncaissementTypeLabel());

			operation.setOpperationFacultatifLabel(encaissement.getEncaissementLabel());

			operations.add(operation);

		});

		return operations;
	}
}
