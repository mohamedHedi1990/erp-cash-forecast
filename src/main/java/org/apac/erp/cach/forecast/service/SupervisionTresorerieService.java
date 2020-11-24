package org.apac.erp.cach.forecast.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apac.erp.cach.forecast.constants.Utils;
import org.apac.erp.cach.forecast.dtos.OperationTreserorieDto;
import org.apac.erp.cach.forecast.enumeration.Operation;
import org.apac.erp.cach.forecast.enumeration.OperationType;
import org.apac.erp.cach.forecast.enumeration.PaymentMethod;
import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.Comission;
import org.apac.erp.cach.forecast.persistence.entities.CustomerInvoice;
import org.apac.erp.cach.forecast.persistence.entities.Decaissement;
import org.apac.erp.cach.forecast.persistence.entities.Encaissement;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.persistence.entities.ProviderInvoice;
import org.apac.erp.cach.forecast.persistence.entities.TimeLine;
import org.apac.erp.cach.forecast.persistence.entities.TimeLineEntry;
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

	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private TarifBancaireService tarifBancaireService;

	@Autowired
	private TimeLineService timeLineService;

	public List<OperationTreserorieDto> globalSupervisionEngage(Long accountId, Date startDate, Date endDate,
			Boolean isValidated) {
		List<OperationTreserorieDto> operations = new ArrayList<OperationTreserorieDto>();

		BankAccount bankAccount = bankAccountService.getAccountById(accountId);
		Double initialAmount = bankAccount.getAccountInitialAmount();
		List<Comission> comissions = new ArrayList<Comission>();
		if (bankAccount.getAccountComissions() != null) {
			comissions = bankAccount.getAccountComissions();
		}
		// if(this.tarifBancaireService.findByTarifAccount(bankAccount) != null &&
		// !this.tarifBancaireService.findByTarifAccount(bankAccount).isEmpty()) {
		// comissions =
		// this.tarifBancaireService.findByTarifAccount(bankAccount).get(0).getComissions();
		// }

		// Trouver tout kles réglements sur cette intervalle
		List<PaymentRule> paymentRules = paymentRuleService.getAllPaymentRuleBetwwenTwoDates(bankAccount, startDate,
				endDate);
		if (isValidated) {
			paymentRules = paymentRules.stream().filter(paymentRule -> paymentRule.getIsValidated() == isValidated)
					.collect(Collectors.toList());

		}
		List<OperationTreserorieDto> paymentRuleOperations = convertPaymentRulesToOperationTreserorieList(paymentRules,
				comissions);
		operations.addAll(paymentRuleOperations);

		// Trouver tout les decaissements sur cette période
		List<Decaissement> decaissements = decaissementService.findDecaissementsBetwwenTwoDates(bankAccount, startDate,
				endDate);
		if (isValidated) {
			decaissements = decaissements.stream().filter(decaissement -> decaissement.getIsValidated() == isValidated)
					.collect(Collectors.toList());
		}
		List<OperationTreserorieDto> decaissementOperations = convertDecaissementsToOperationTreserorieList(
				decaissements, comissions);
		operations.addAll(decaissementOperations);

		// Trouver tout les encaissements sur cette période
		List<Encaissement> encaissements = encaissementService.findEncaissementsBetwwenTwoDates(bankAccount, startDate,
				endDate);
		if (isValidated) {
			encaissements = encaissements.stream().filter(encaissement -> encaissement.getIsValidated() == isValidated)
					.collect(Collectors.toList());
		}
		List<OperationTreserorieDto> encaissementOperations = convertEncaissementsToOperationTreserorieList(
				encaissements, comissions);
		operations.addAll(encaissementOperations);

		// Trouver la liste des time line entries
		List<TimeLine> timeLines = timeLineService.findByTimeLineAccount(bankAccount);
		for (TimeLine timeLine : timeLines) {
			List<TimeLineEntry> entries = timeLine.getTimeLineTable();
			entries = entries.stream().filter(entry -> entry.getLineDate().compareTo(startDate) >= 0
					&& entry.getLineDate().compareTo(endDate) <= 0).collect(Collectors.toList());
			List<OperationTreserorieDto> timeLineEntriesOperations = convertTimeLineEntriesToOperationsTreserorieList(
					entries);
			operations.addAll(timeLineEntriesOperations);
		}

		operations = operations.stream().sorted(Comparator.comparing(OperationTreserorieDto::getOperationDate))
				.collect(Collectors.toList());
		
		for (int i=0; i<operations.size(); i++) {
		 
			if(i == 0) {
				if(operations.get(i).getOpperationType() == OperationType.ENCAISSEMENT)
				operations.get(i).setProgressiveAmount(initialAmount + operations.get(i).getOperationAmount());
				else
					operations.get(i).setProgressiveAmount(initialAmount - operations.get(i).getOperationAmount());
			} else {
				if(operations.get(i).getOpperationType() == OperationType.ENCAISSEMENT)
					operations.get(i).setProgressiveAmount(operations.get(i-1).getOperationAmount() + operations.get(i).getOperationAmount());
					else
						operations.get(i).setProgressiveAmount(operations.get(i-1).getOperationAmount() - operations.get(i).getOperationAmount());
			}
			operations.get(i).setOperationAmountS( Utils.convertAmountToString(operations.get(i).getOperationAmount()));
		}

		return operations;
	}

	private List<OperationTreserorieDto> convertTimeLineEntriesToOperationsTreserorieList(List<TimeLineEntry> entries) {
		List<OperationTreserorieDto> operations = new ArrayList<OperationTreserorieDto>();
		entries.stream().forEach(entry -> {
			OperationTreserorieDto operation = new OperationTreserorieDto();
			operation.setOpperationType(OperationType.DECAISSEMENT);
			operation.setOperationDate(entry.getLineDate());
			operation.setOperationAmountS(entry.getInitialAmountS());
			operation.setOpperationLabel("PAIEMENT ECHEANCE PRINCIPALE CREDIT N° " + entry.getTimeLineCreditNumber());
			List<String> detailsPayements = new ArrayList<String>();
			detailsPayements.add("Paiement écheance principal - Crédit No " + entry.getTimeLineCreditNumber());
			operation.setOpperationDetails(detailsPayements);
			operation.setOperationAmount(entry.getInitialAmount());
			operations.add(operation);

			OperationTreserorieDto operation2 = new OperationTreserorieDto();
			operation2.setOpperationType(OperationType.DECAISSEMENT);
			operation2.setOperationDate(entry.getLineDate());
			operation2.setOperationAmountS(entry.getInterestsS());
			operation2.setOperationAmount(entry.getInterests());
			operation2.setOpperationLabel("PAIEMENT ECHEANCE INTERETS CREDIT N° " + entry.getTimeLineCreditNumber());
			//operation2.setOpperationLabel("Paiement écheance intérets");
			List<String> detailsPayements2 = new ArrayList<String>();
			detailsPayements2.add("Paiement écheance intérets - Crédit No " + entry.getTimeLineCreditNumber());
			operation2.setOpperationDetails(detailsPayements2);
			operations.add(operation2);
		});
		return operations;
	}

	private List<OperationTreserorieDto> convertPaymentRulesToOperationTreserorieList(List<PaymentRule> paymentRules,
			List<Comission> comissions) {
		List<OperationTreserorieDto> operations = new ArrayList<OperationTreserorieDto>();
		paymentRules.forEach(paymentRule -> {
			OperationTreserorieDto operation = new OperationTreserorieDto();
			operation.setOpperationType(paymentRule.getPaymentRuleOperationType());
			operation.setOperationDate(paymentRule.getPaymentRuleDeadlineDate());
			operation.setOperationAmountS(paymentRule.getPaymentRuleAmountS());
			operation.setOperationAmount(paymentRule.getPaymentRuleAmount());

			List<String> detailsPayements = new ArrayList<String>();
			detailsPayements.add(paymentRule.getPaymentRulePaymentMethod().toString());
			detailsPayements.add(paymentRule.getPaymentRuleNumber());
			detailsPayements.add(paymentRule.getPaymentRuleDetails());
			operation.setOpperationDetails(detailsPayements);

			if (paymentRule.getPaymentRuleOperationType() == OperationType.ENCAISSEMENT) {
				operation.setOpperationLabel(
						"ENCAISSEMENT " + paymentRule.getPaymentRulePaymentMethod().toString().toUpperCase() + " N° "
								+ paymentRule.getPaymentRuleNumber().toUpperCase());
			} else {
				operation.setOpperationLabel(
						"DECAISSEMENT " + paymentRule.getPaymentRulePaymentMethod().toString().toUpperCase() + " N° "
								+ paymentRule.getPaymentRuleNumber().toUpperCase());
			}

			// Ajouter une information sur la facture payé dans le label
			/*
			 * String label = "Payement de la facture "; String invoicesIds =
			 * paymentRule.getPaymentRuleInvoices(); List<Invoice> invoices = new
			 * ArrayList<Invoice>(); String[] ids = invoicesIds.split(",");
			 * 
			 * Arrays.stream(ids).forEach(invoiceId -> { if
			 * (paymentRule.getPaymentRuleOperationType() == OperationType.DECAISSEMENT) {
			 * ProviderInvoice providerInvoice = providerInvoiceService
			 * .getProviderInvoiceById(Long.parseLong(invoiceId));
			 * operation.setOpperationCurrency(providerInvoice.getInvoiceCurrency());
			 * operation.setOpperationType(OperationType.DECAISSEMENT);
			 * invoices.add(providerInvoice); } else { CustomerInvoice customerInvoice =
			 * customerInvoiceService .getCustomerInvoiceById(Long.parseLong(invoiceId));
			 * operation.setOpperationCurrency(customerInvoice.getInvoiceCurrency());
			 * operation.setOpperationType(OperationType.ENCAISSEMENT);
			 * invoices.add(customerInvoice); }
			 * 
			 * });
			 * 
			 * for (int i = 0; i < invoices.size(); i++) { label = label +
			 * invoices.get(i).getInvoiceNumber(); if (i < invoices.size() - 1) { label =
			 * label + " et "; } }
			 * 
			 * operation.setOpperationLabel(label);
			 */

			operations.add(operation);

			// Ajouter une opération rélative au comission de type de paiement
			if (paymentRule.getPaymentRuleOperationType() == OperationType.DECAISSEMENT) {
				if (paymentRule.getPaymentRulePaymentMethod() == PaymentMethod.CHEQUE) {
					List<Comission> chequeComissions = comissions.stream()
							.filter(comission -> comission.getComissionOperation() == Operation.REMISE_CHHEQUE)
							.collect(Collectors.toList());

					chequeComissions.stream().forEach(com -> {
						OperationTreserorieDto operationCom = new OperationTreserorieDto();
						operationCom.setOpperationType(OperationType.DECAISSEMENT);
						operationCom.setOperationDate(paymentRule.getPaymentRuleDeadlineDate());
						operationCom.setOperationAmountS(com.getComissionValueS());
						operationCom.setOperationAmount(com.getComissionValue());
						operationCom
								.setOpperationLabel("COMISSION REMISE CHEQUE N° " + paymentRule.getPaymentRuleNumber());
						operationCom.setOpperationCurrency(operation.getOpperationCurrency());
						List<String> detailsPayementsCom = new ArrayList<String>();
						detailsPayements
								.add("Comission de remise de chèque numéro " + paymentRule.getPaymentRuleNumber());
						operationCom.setOpperationDetails(detailsPayements);
						operations.add(operationCom);
					});

				} else if (paymentRule.getPaymentRulePaymentMethod() == PaymentMethod.VIREMENT) {
					List<Comission> virementComissions = comissions.stream()
							.filter(comission -> comission.getComissionOperation() == Operation.VIREMENT)
							.collect(Collectors.toList());

					virementComissions.stream().forEach(com -> {
						OperationTreserorieDto operationCom = new OperationTreserorieDto();
						operationCom.setOpperationType(OperationType.DECAISSEMENT);
						operationCom.setOperationDate(paymentRule.getPaymentRuleDeadlineDate());
						operationCom.setOperationAmountS(com.getComissionValueS());
						operationCom.setOperationAmount(com.getComissionValue());
						operationCom.setOpperationLabel("COMISSION VIREMENT");
						operationCom.setOpperationCurrency(operation.getOpperationCurrency());
						List<String> detailsPayementsCom = new ArrayList<String>();
						detailsPayements.add("Comission de virement ");
						operationCom.setOpperationDetails(detailsPayements);
						operations.add(operationCom);
					});

				} else if (paymentRule.getPaymentRulePaymentMethod() == PaymentMethod.TRAITE) {
					List<Comission> traiteComissions = comissions.stream()
							.filter(comission -> comission.getComissionOperation() == Operation.TRAITE)
							.collect(Collectors.toList());

					traiteComissions.stream().forEach(com -> {
						OperationTreserorieDto operationCom = new OperationTreserorieDto();
						operationCom.setOpperationType(OperationType.DECAISSEMENT);
						operationCom.setOperationDate(paymentRule.getPaymentRuleDeadlineDate());
						operationCom.setOperationAmountS(com.getComissionValueS());
						operationCom.setOperationAmount(com.getComissionValue());
						operationCom
								.setOpperationLabel("COMISSION REMISE TRAITE N° " + paymentRule.getPaymentRuleNumber());
						operationCom.setOpperationCurrency(operation.getOpperationCurrency());
						List<String> detailsPayementsCom = new ArrayList<String>();
						detailsPayements
								.add("Comission de remise de traite numéro " + paymentRule.getPaymentRuleNumber());
						operationCom.setOpperationDetails(detailsPayements);
						operations.add(operationCom);
					});

				}
				/*
				 * else if (paymentRule.getPaymentRulePaymentMethod() ==
				 * PaymentMethod.EFFET_ENCAISSEMENT) { List<Comission>
				 * effetEncaissementsComissions = comissions.stream() .filter(comission ->
				 * comission.getComissionOperation() == Operation.REMISE_EFFET_ENCAISSEMENT)
				 * .collect(Collectors.toList());
				 * 
				 * effetEncaissementsComissions.stream().forEach(com -> { OperationTreserorieDto
				 * operationCom = new OperationTreserorieDto();
				 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
				 * operationCom.setOperationDate(paymentRule.getPaymentRuleDeadlineDate());
				 * operationCom.setOperationAmountS(com.getComissionValueS());
				 * operationCom.setOperationAmount(com.getComissionValue());
				 * operationCom.setOpperationLabel("Comission remise effet d'encaissement");
				 * operationCom.setOpperationCurrency(operation.getOpperationCurrency());
				 * List<String> detailsPayementsCom = new ArrayList<String>();
				 * detailsPayements.add("Comission de remise d'effet d'encaissement");
				 * operationCom.setOpperationDetails(detailsPayements);
				 * operations.add(operationCom); });
				 * 
				 * } else if (paymentRule.getPaymentRulePaymentMethod() ==
				 * PaymentMethod.EFFET_ESCOMPTE) { List<Comission> effetEscomptesComissions =
				 * comissions.stream() .filter(comission -> comission.getComissionOperation() ==
				 * Operation.REMISE_EFFET_ESCOMPTE) .collect(Collectors.toList());
				 * 
				 * effetEscomptesComissions.stream().forEach(com -> { OperationTreserorieDto
				 * operationCom = new OperationTreserorieDto();
				 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
				 * operationCom.setOperationDate(paymentRule.getPaymentRuleDeadlineDate());
				 * operationCom.setOperationAmountS(com.getComissionValueS());
				 * operationCom.setOperationAmount(com.getComissionValue());
				 * operationCom.setOpperationLabel("Comission remise effet d'escompte");
				 * operationCom.setOpperationCurrency(operation.getOpperationCurrency());
				 * List<String> detailsPayementsCom = new ArrayList<String>();
				 * detailsPayements.add("Comission de remise d'effet d'escompte");
				 * operationCom.setOpperationDetails(detailsPayements);
				 * operations.add(operationCom); });
				 * 
				 * }
				 */
			}

		});

		return operations;
	}

	private List<OperationTreserorieDto> convertDecaissementsToOperationTreserorieList(List<Decaissement> decaissements,
			List<Comission> comissions) {

		List<OperationTreserorieDto> operations = new ArrayList<OperationTreserorieDto>();

		decaissements.forEach(decaissement -> {
			OperationTreserorieDto operation = new OperationTreserorieDto();
			operation.setOpperationType(OperationType.DECAISSEMENT);
			operation.setOperationDate(decaissement.getDecaissementDeadlineDate());
			operation.setOperationAmountS(decaissement.getDecaissementAmountS());
			operation.setOperationAmount(decaissement.getDecaissementAmount());
			operation.setOpperationCurrency(decaissement.getDecaissementCurrency());

			List<String> detailsPayements = new ArrayList<String>();
			detailsPayements.add(decaissement.getDecaissementPaymentType().toString());
			detailsPayements.add(decaissement.getDecaissementPaymentRuleNumber());
			detailsPayements.add(decaissement.getDecaissementDetails());

			operation.setOpperationDetails(detailsPayements);
			operation.setOpperationLabel(
					"DECAISSEMENT " + decaissement.getDecaissementPaymentType().toString().toUpperCase() + " N° "
							+ decaissement.getDecaissementPaymentRuleNumber().toUpperCase());
			// operation.setOpperationLabel(decaissement.getDecaissementType().getDecaissementTypeLabel());

			operation.setOpperationFacultatifLabel(decaissement.getDecaissementLabel());

			operations.add(operation);

			// Ajouter une opération rélative au comission de type de paiement

			if (decaissement.getDecaissementPaymentType() == PaymentMethod.CHEQUE) {
				List<Comission> chequeComissions = comissions.stream()
						.filter(comission -> comission.getComissionOperation() == Operation.REMISE_CHHEQUE)
						.collect(Collectors.toList());

				chequeComissions.stream().forEach(com -> {
					OperationTreserorieDto operationCom = new OperationTreserorieDto();
					operationCom.setOpperationType(OperationType.DECAISSEMENT);
					operationCom.setOperationDate(decaissement.getDecaissementDeadlineDate());
					operationCom.setOperationAmountS(com.getComissionValueS());
					operationCom.setOperationAmount(com.getComissionValue());
					operationCom.setOpperationLabel(
							"COMISSION REMISE CHEQUE N° " + decaissement.getDecaissementPaymentRuleNumber());
					operationCom.setOpperationCurrency(operation.getOpperationCurrency());
					List<String> detailsPayementsCom = new ArrayList<String>();
					detailsPayements.add(
							"Comission de remise de chèque numéro " + decaissement.getDecaissementPaymentRuleNumber());
					operationCom.setOpperationDetails(detailsPayements);
					operations.add(operationCom);
				});

			} else if (decaissement.getDecaissementPaymentType() == PaymentMethod.VIREMENT) {
				List<Comission> virementComissions = comissions.stream()
						.filter(comission -> comission.getComissionOperation() == Operation.VIREMENT)
						.collect(Collectors.toList());

				virementComissions.stream().forEach(com -> {
					OperationTreserorieDto operationCom = new OperationTreserorieDto();
					operationCom.setOpperationType(OperationType.DECAISSEMENT);
					operationCom.setOperationDate(decaissement.getDecaissementDeadlineDate());
					operationCom.setOperationAmountS(com.getComissionValueS());
					operationCom.setOperationAmount(com.getComissionValue());
					operationCom.setOpperationLabel("COMISSION VIREMENT");
					operationCom.setOpperationCurrency(operation.getOpperationCurrency());
					List<String> detailsPayementsCom = new ArrayList<String>();
					detailsPayements.add("Comission de virement ");
					operationCom.setOpperationDetails(detailsPayements);
					operations.add(operationCom);
				});

			} else if (decaissement.getDecaissementPaymentType() == PaymentMethod.TRAITE) {
				List<Comission> traiteComissions = comissions.stream()
						.filter(comission -> comission.getComissionOperation() == Operation.TRAITE)
						.collect(Collectors.toList());

				traiteComissions.stream().forEach(com -> {
					OperationTreserorieDto operationCom = new OperationTreserorieDto();
					operationCom.setOpperationType(OperationType.DECAISSEMENT);
					operationCom.setOperationDate(decaissement.getDecaissementDeadlineDate());
					operationCom.setOperationAmountS(com.getComissionValueS());
					operationCom.setOperationAmount(com.getComissionValue());
					operationCom.setOpperationLabel(
							"COMISSION REMISE TRAITE N° " + decaissement.getDecaissementPaymentRuleNumber());
					operationCom.setOpperationCurrency(operation.getOpperationCurrency());
					List<String> detailsPayementsCom = new ArrayList<String>();
					detailsPayements.add(
							"Comission de remise de traite numéro " + decaissement.getDecaissementPaymentRuleNumber());
					operationCom.setOpperationDetails(detailsPayements);
					operations.add(operationCom);
				});

			} /*
				 * else if (decaissement.getDecaissementPaymentType() ==
				 * PaymentMethod.EFFET_ENCAISSEMENT) { List<Comission>
				 * effetEncaissementsComissions = comissions.stream() .filter(comission ->
				 * comission.getComissionOperation() == Operation.REMISE_EFFET_ENCAISSEMENT)
				 * .collect(Collectors.toList());
				 * 
				 * effetEncaissementsComissions.stream().forEach(com -> { OperationTreserorieDto
				 * operationCom = new OperationTreserorieDto();
				 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
				 * operationCom.setOperationDate(decaissement.getDecaissementDeadlineDate());
				 * operationCom.setOperationAmountS(com.getComissionValueS());
				 * operationCom.setOperationAmount(com.getComissionValue());
				 * operationCom.setOpperationLabel("Comission remise effet d'encaissement");
				 * operationCom.setOpperationCurrency(operation.getOpperationCurrency());
				 * List<String> detailsPayementsCom = new ArrayList<String>();
				 * detailsPayements.add("Comission de remise d'effet d'encaissement");
				 * operationCom.setOpperationDetails(detailsPayements);
				 * operations.add(operationCom); });
				 * 
				 * } else if (decaissement.getDecaissementPaymentType() ==
				 * PaymentMethod.EFFET_ESCOMPTE) { List<Comission> effetEscomptesComissions =
				 * comissions.stream() .filter(comission -> comission.getComissionOperation() ==
				 * Operation.REMISE_EFFET_ESCOMPTE) .collect(Collectors.toList());
				 * 
				 * effetEscomptesComissions.stream().forEach(com -> { OperationTreserorieDto
				 * operationCom = new OperationTreserorieDto();
				 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
				 * operationCom.setOperationDate(decaissement.getDecaissementDeadlineDate());
				 * operationCom.setOperationAmountS(com.getComissionValueS());
				 * operationCom.setOperationAmount(com.getComissionValue());
				 * operationCom.setOpperationLabel("Comission remise effet d'escompte");
				 * operationCom.setOpperationCurrency(operation.getOpperationCurrency());
				 * List<String> detailsPayementsCom = new ArrayList<String>();
				 * detailsPayements.add("Comission de remise d'effet d'escompte");
				 * operationCom.setOpperationDetails(detailsPayements);
				 * operations.add(operationCom); });
				 * 
				 * }
				 */

		});

		return operations;
	}

	private List<OperationTreserorieDto> convertEncaissementsToOperationTreserorieList(List<Encaissement> encaissements,
			List<Comission> comissions) {

		List<OperationTreserorieDto> operations = new ArrayList<OperationTreserorieDto>();

		encaissements.forEach(encaissement -> {
			OperationTreserorieDto operation = new OperationTreserorieDto();
			operation.setOpperationType(OperationType.ENCAISSEMENT);
			operation.setOperationDate(encaissement.getEncaissementDeadlineDate());
			operation.setOperationAmountS(encaissement.getEncaissementAmountS());
			operation.setOperationAmount(encaissement.getEncaissementAmount());
			List<String> detailsPayements = new ArrayList<String>();
			detailsPayements.add(encaissement.getEncaissementPaymentType().toString());
			detailsPayements.add(encaissement.getEncaissementPaymentRuleNumber());
			detailsPayements.add(encaissement.getEncaissementDetails());

			operation.setOpperationDetails(detailsPayements);

			operation.setOpperationLabel(
					"ENCAISSEMENT " + encaissement.getEncaissementPaymentType().toString().toUpperCase() + " N° "
							+ encaissement.getEncaissementPaymentRuleNumber().toUpperCase());
			// operation.setOpperationLabel(encaissement.getEncaissementType().getEncaissementTypeLabel());

			operation.setOpperationFacultatifLabel(encaissement.getEncaissementLabel());

			operations.add(operation);

			// Ajouter une opération rélative au comission de type de paiement

			/*
			 * if (encaissement.getEncaissementPaymentType() == PaymentMethod.CHEQUE) {
			 * List<Comission> chequeComissions = comissions.stream() .filter(comission ->
			 * comission.getComissionOperation() == Operation.REMISE_CHHEQUE)
			 * .collect(Collectors.toList());
			 * 
			 * chequeComissions.stream().forEach(com -> { OperationTreserorieDto
			 * operationCom = new OperationTreserorieDto();
			 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
			 * operationCom.setOperationDate(encaissement.getEncaissementDeadlineDate());
			 * operationCom.setOperationAmountS(com.getComissionValueS());
			 * operationCom.setOperationAmount(com.getComissionValue());
			 * operationCom.setOpperationLabel("Comission remise chèque");
			 * operationCom.setOpperationCurrency(operation.getOpperationCurrency());
			 * List<String> detailsPayementsCom = new ArrayList<String>();
			 * detailsPayements.add( "Comission de remise de chèque numéro " +
			 * encaissement.getEncaissementPaymentRuleNumber());
			 * operationCom.setOpperationDetails(detailsPayements);
			 * operations.add(operationCom); });
			 * 
			 * } else if (encaissement.getEncaissementPaymentType() ==
			 * PaymentMethod.VIREMENT) { List<Comission> virementComissions =
			 * comissions.stream() .filter(comission -> comission.getComissionOperation() ==
			 * Operation.VIREMENT) .collect(Collectors.toList());
			 * 
			 * virementComissions.stream().forEach(com -> { OperationTreserorieDto
			 * operationCom = new OperationTreserorieDto();
			 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
			 * operationCom.setOperationDate(encaissement.getEncaissementDeadlineDate());
			 * operationCom.setOperationAmountS(com.getComissionValueS());
			 * operationCom.setOperationAmount(com.getComissionValue());
			 * operationCom.setOpperationLabel("Comission virement");
			 * operationCom.setOpperationCurrency(operation.getOpperationCurrency());
			 * List<String> detailsPayementsCom = new ArrayList<String>();
			 * detailsPayements.add("Comission de virement ");
			 * operationCom.setOpperationDetails(detailsPayements);
			 * operations.add(operationCom); });
			 * 
			 * } else if (encaissement.getEncaissementPaymentType() == PaymentMethod.TRAITE)
			 * { List<Comission> traiteComissions = comissions.stream() .filter(comission ->
			 * comission.getComissionOperation() == Operation.TRAITE)
			 * .collect(Collectors.toList());
			 * 
			 * traiteComissions.stream().forEach(com -> { OperationTreserorieDto
			 * operationCom = new OperationTreserorieDto();
			 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
			 * operationCom.setOperationDate(encaissement.getEncaissementDeadlineDate());
			 * operationCom.setOperationAmountS(com.getComissionValueS());
			 * operationCom.setOperationAmount(com.getComissionValue());
			 * operationCom.setOpperationLabel("Comission remise traite");
			 * operationCom.setOpperationCurrency(operation.getOpperationCurrency());
			 * List<String> detailsPayementsCom = new ArrayList<String>();
			 * detailsPayements.add( "Comission de remise de traite numéro " +
			 * encaissement.getEncaissementPaymentRuleNumber());
			 * operationCom.setOpperationDetails(detailsPayements);
			 * operations.add(operationCom); });
			 * 
			 * } else if (encaissement.getEncaissementPaymentType() ==
			 * PaymentMethod.EFFET_ENCAISSEMENT) { List<Comission>
			 * effetEncaissementsComissions = comissions.stream() .filter(comission ->
			 * comission.getComissionOperation() == Operation.REMISE_EFFET_ENCAISSEMENT)
			 * .collect(Collectors.toList());
			 * 
			 * effetEncaissementsComissions.stream().forEach(com -> { OperationTreserorieDto
			 * operationCom = new OperationTreserorieDto();
			 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
			 * operationCom.setOperationDate(encaissement.getEncaissementDeadlineDate());
			 * operationCom.setOperationAmountS(com.getComissionValueS());
			 * operationCom.setOperationAmount(com.getComissionValue());
			 * operationCom.setOpperationLabel("Comission remise effet d'encaissement");
			 * operationCom.setOpperationCurrency(operation.getOpperationCurrency());
			 * List<String> detailsPayementsCom = new ArrayList<String>();
			 * detailsPayements.add("Comission de remise d'effet d'encaissement");
			 * operationCom.setOpperationDetails(detailsPayements);
			 * operations.add(operationCom); });
			 * 
			 * } else if (encaissement.getEncaissementPaymentType() ==
			 * PaymentMethod.EFFET_ESCOMPTE) { List<Comission> effetEscomptesComissions =
			 * comissions.stream() .filter(comission -> comission.getComissionOperation() ==
			 * Operation.REMISE_EFFET_ESCOMPTE) .collect(Collectors.toList());
			 * 
			 * effetEscomptesComissions.stream().forEach(com -> { OperationTreserorieDto
			 * operationCom = new OperationTreserorieDto();
			 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
			 * operationCom.setOperationDate(encaissement.getEncaissementDeadlineDate());
			 * operationCom.setOperationAmountS(com.getComissionValueS());
			 * operationCom.setOperationAmount(com.getComissionValue());
			 * operationCom.setOpperationLabel("Comission remise effet d'escompte");
			 * operationCom.setOpperationCurrency(operation.getOpperationCurrency());
			 * List<String> detailsPayementsCom = new ArrayList<String>();
			 * detailsPayements.add("Comission de remise d'effet d'escompte");
			 * operationCom.setOpperationDetails(detailsPayements);
			 * operations.add(operationCom); });
			 * 
			 * }
			 */

		});

		return operations;
	}

}
