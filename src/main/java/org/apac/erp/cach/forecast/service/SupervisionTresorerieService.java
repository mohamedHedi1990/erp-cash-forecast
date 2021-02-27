package org.apac.erp.cach.forecast.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apac.erp.cach.forecast.constants.Constants;
import org.apac.erp.cach.forecast.constants.Utils;
import org.apac.erp.cach.forecast.dtos.OperationTreserorieDto;
import org.apac.erp.cach.forecast.enumeration.Operation;
import org.apac.erp.cach.forecast.enumeration.OperationDtoType;
import org.apac.erp.cach.forecast.enumeration.OperationType;
import org.apac.erp.cach.forecast.enumeration.PaymentMethod;
import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.Comission;
import org.apac.erp.cach.forecast.persistence.entities.Decaissement;
import org.apac.erp.cach.forecast.persistence.entities.Encaissement;
import org.apac.erp.cach.forecast.persistence.entities.HistoricAccountSold;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.persistence.entities.TimeLine;
import org.apac.erp.cach.forecast.persistence.entities.TimeLineEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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

	@Autowired
	private ComissionService comissionService;

	@Autowired
	private TimeLineEntryService timeLineEntryService;
	
	@Autowired
	private HistoricAccountSoldService historicAccountSoldService;
	

	public List<OperationTreserorieDto> globalSupervisionEngage(Long accountId, Date startDate, Date endDate,
			Boolean isValidated) {
		
		//test
		
		System.out.println("start date --------------------------- " + startDate);
		
		System.out.println("end date ----------------------------- " + endDate);
		ZoneId z = ZoneId.of("Africa/Tunis");
		LocalDate localStartDate = startDate.toInstant().atZone(z).toLocalDate();
		LocalDate localEndDate = endDate.toInstant().atZone(z).toLocalDate();
		
		Date startDate1 = Date.from(localStartDate.atStartOfDay(z).toInstant());
		Date endDate1 = Date.from(localEndDate.atStartOfDay(z).toInstant());
		
		System.out.println("start date --------------------------- " + startDate1);
		
		System.out.println("end date ----------------------------- " + endDate1);
		
		//fin test
		
		
		
		
		List<OperationTreserorieDto> operations = new ArrayList<OperationTreserorieDto>();

		BankAccount bankAccount = bankAccountService.getAccountById(accountId);
		HistoricAccountSold initialAmountInTheBeginingOfThePeriod = this.historicAccountSoldService.findFirst(bankAccount.getAccountId(), startDate);
		HistoricAccountSold lastAmountOfAccount = this.historicAccountSoldService.findLast(bankAccount.getAccountId());
		//Double initialAmount = bankAccount.getAccountInitialAmount();
		Double initialAmount = 0.0;
		if(lastAmountOfAccount != null) {
			initialAmount = lastAmountOfAccount.getSolde();
		} 
		List<Comission> comissions = new ArrayList<Comission>();
		if (bankAccount.getAccountComissions() != null) {
			comissions = bankAccount.getAccountComissions();
		}
		// if(this.tarifBancaireService.findByTarifAccount(bankAccount) != null
		// &&
		// !this.tarifBancaireService.findByTarifAccount(bankAccount).isEmpty())
		// {
		// comissions =
		// this.tarifBancaireService.findByTarifAccount(bankAccount).get(0).getComissions();
		// }
		
		
		//Trouver toutes les opérations non validés avant cette date
		
		
		List<PaymentRule> paymentRulesNV = paymentRuleService.getAllNonValidatedBeforeDate(bankAccount, startDate);
		List<OperationTreserorieDto> paymentRuleOperationsNV = convertPaymentRulesToOperationTreserorieList(paymentRulesNV,
				comissions, false);
		operations.addAll(paymentRuleOperationsNV);
		List<Decaissement> decaissementsNV = decaissementService.findAllNonValidatedBeforeDate(bankAccount, startDate);
		List<OperationTreserorieDto> decaissementOperationsNV = convertDecaissementsToOperationTreserorieList(
				decaissementsNV, comissions, false);
		operations.addAll(decaissementOperationsNV);
		List<Encaissement> encaissementsNV = encaissementService.findAllNonValidatedBeforeDate(bankAccount, startDate);
		List<OperationTreserorieDto> encaissementOperationsNV = convertEncaissementsToOperationTreserorieList(
				encaissementsNV, comissions, true);
		operations.addAll(encaissementOperationsNV);
		

		// Trouver tout kles réglements sur cette intervalle
		List<PaymentRule> paymentRules = paymentRuleService.getAllPaymentRuleBetwwenTwoDates(bankAccount, startDate,
				endDate);
		/*
		 * if (isValidated) { paymentRules =
		 * paymentRules.stream().filter(paymentRule ->
		 * paymentRule.getIsValidated() == isValidated)
		 * .collect(Collectors.toList());
		 * 
		 * }
		 */
		List<OperationTreserorieDto> paymentRuleOperations = convertPaymentRulesToOperationTreserorieList(paymentRules,
				comissions, true);
		operations.addAll(paymentRuleOperations);

		// Trouver tout les decaissements sur cette période
		List<Decaissement> decaissements = decaissementService.findDecaissementsBetwwenTwoDates(bankAccount, startDate,
				endDate);
		/*
		 * if (isValidated) { decaissements =
		 * decaissements.stream().filter(decaissement ->
		 * decaissement.getIsValidated() == isValidated)
		 * .collect(Collectors.toList()); }
		 */
		List<OperationTreserorieDto> decaissementOperations = convertDecaissementsToOperationTreserorieList(
				decaissements, comissions, true);
		operations.addAll(decaissementOperations);

		// Trouver tout les encaissements sur cette période
		List<Encaissement> encaissements = encaissementService.findEncaissementsBetwwenTwoDates(bankAccount, startDate,
				endDate);
		/*
		 * if (isValidated) { encaissements =
		 * encaissements.stream().filter(encaissement ->
		 * encaissement.getIsValidated() == isValidated)
		 * .collect(Collectors.toList()); }
		 */
		List<OperationTreserorieDto> encaissementOperations = convertEncaissementsToOperationTreserorieList(
				encaissements, comissions, true);
		operations.addAll(encaissementOperations);

		// Trouver la liste des time line entries
		List<TimeLine> timeLines = timeLineService.findByTimeLineAccount(bankAccount);
		if (timeLines != null) {
			for (TimeLine timeLine : timeLines) {
				List<TimeLineEntry> entries = timeLine.getTimeLineTable();
				entries = entries.stream().filter(entry -> entry.getLineDate().compareTo(startDate) >= 0
						&& entry.getLineDate().compareTo(endDate) <= 0).collect(Collectors.toList());
				List<OperationTreserorieDto> timeLineEntriesOperations = convertTimeLineEntriesToOperationsTreserorieList(
						entries, timeLine.getTimeLineCreditNumber(), timeLine.getCreditInstitution(), true);
				operations.addAll(timeLineEntriesOperations);
			}
		}

		operations = operations.stream().sorted(Comparator.comparing(OperationTreserorieDto::getOperationDate))
				.collect(Collectors.toList());

		for (int i = 0; i < operations.size(); i++) {

			if (i == 0) {
				if (isValidated) {
					if (operations.get(i).isValidated()) {
						if (operations.get(i).getOpperationType() == OperationType.ENCAISSEMENT)
							operations.get(i)
									.setProgressiveAmount(initialAmount + operations.get(i).getOperationAmount());
						else
							operations.get(i)
									.setProgressiveAmount(initialAmount - operations.get(i).getOperationAmount());
					} else {
						operations.get(i).setProgressiveAmount(initialAmount);
					}
				} else {
					if (operations.get(i).getOpperationType() == OperationType.ENCAISSEMENT)
						operations.get(i).setProgressiveAmount(initialAmount + operations.get(i).getOperationAmount());
					else
						operations.get(i).setProgressiveAmount(initialAmount - operations.get(i).getOperationAmount());
				}

			} else {
				if (isValidated) {
					if (operations.get(i).isValidated()) {
						if (operations.get(i).getOpperationType() == OperationType.ENCAISSEMENT)
							operations.get(i).setProgressiveAmount(operations.get(i - 1).getProgressiveAmount()
									+ operations.get(i).getOperationAmount());
						else
							operations.get(i).setProgressiveAmount(operations.get(i - 1).getProgressiveAmount()
									- operations.get(i).getOperationAmount());
					} else {
						operations.get(i).setProgressiveAmount(operations.get(i - 1).getProgressiveAmount());
					}
				} else {
					if (operations.get(i).getOpperationType() == OperationType.ENCAISSEMENT)
						operations.get(i).setProgressiveAmount(
								operations.get(i - 1).getProgressiveAmount() + operations.get(i).getOperationAmount());
					else
						operations.get(i).setProgressiveAmount(
								operations.get(i - 1).getProgressiveAmount() - operations.get(i).getOperationAmount());
				}

			}
			double progressiveAmount = (double) Math.round(operations.get(i).getProgressiveAmount() * 100) / 100;
			operations.get(i).setProgressiveAmount(progressiveAmount);
			operations.get(i)
					.setProgressiveAmountS(Utils.convertAmountToString(operations.get(i).getProgressiveAmount()));
		}

		return operations;
	}

	private List<OperationTreserorieDto> convertTimeLineEntriesToOperationsTreserorieList(List<TimeLineEntry> entries,
			String creditNumber, String creditInstitution, Boolean isInTheSimulatedPeriod) {
		List<OperationTreserorieDto> operations = new ArrayList<OperationTreserorieDto>();
		entries.stream().forEach(entry -> {

			// une seule opération qui donne le montant total
			OperationTreserorieDto operation = new OperationTreserorieDto();
			operation.setOpperationType(OperationType.DECAISSEMENT);
			operation.setOperationDate(entry.getLineDate());
			operation.setOperationAmountS(entry.getTotalS());
			operation.setOpperationLabel("PAIEMENT ECHEANCE CREDIT N° " + creditNumber);
			operation.setOperationAmount(entry.getTotal());
			operation.setValidated(entry.getIsValidated());
			operation.setOperationRealId(entry.getTimeLineEntryId());
			operation.setOperationRealType(OperationDtoType.ECHEANCHIER);
			operation.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
			operation.setBeneficiaryName(creditInstitution);
			operations.add(operation);

			// code dedidé pour avoir deux opérations pour chaque entrée
			// (unepour l'échénace princiaple et une autre pour le sinterets)
			/*
			 * OperationTreserorieDto operation = new OperationTreserorieDto();
			 * operation.setOpperationType(OperationType.DECAISSEMENT);
			 * operation.setOperationDate(entry.getLineDate());
			 * operation.setOperationAmountS(entry.getInitialAmountS());
			 * operation.
			 * setOpperationLabel("PAIEMENT ECHEANCE PRINCIPALE CREDIT N° " +
			 * entry.getTimeLineCreditNumber()); List<String> detailsPayements =
			 * new ArrayList<String>();
			 * detailsPayements.add("Paiement écheance principal - Crédit No " +
			 * entry.getTimeLineCreditNumber());
			 * operation.setOpperationDetails(detailsPayements);
			 * operation.setOperationAmount(entry.getInitialAmount());
			 * operation.setIsValidated(entry.getIsValidated());
			 * operations.add(operation);
			 * 
			 * OperationTreserorieDto operation2 = new OperationTreserorieDto();
			 * operation2.setOpperationType(OperationType.DECAISSEMENT);
			 * operation2.setOperationDate(entry.getLineDate());
			 * operation2.setOperationAmountS(entry.getInterestsS());
			 * operation2.setOperationAmount(entry.getInterests()); operation2.
			 * setOpperationLabel("PAIEMENT ECHEANCE INTERETS CREDIT N° " +
			 * entry.getTimeLineCreditNumber());
			 * //operation2.setOpperationLabel("Paiement écheance intérets");
			 * List<String> detailsPayements2 = new ArrayList<String>();
			 * detailsPayements2.add("Paiement écheance intérets - Crédit No " +
			 * entry.getTimeLineCreditNumber());
			 * operation2.setOpperationDetails(detailsPayements2);
			 * operations.add(operation2);
			 */
		});
		return operations;
	}

	private List<OperationTreserorieDto> convertPaymentRulesToOperationTreserorieList(List<PaymentRule> paymentRules,
			List<Comission> comissions, Boolean isInTheSimulatedPeriod) {
		List<OperationTreserorieDto> operations = new ArrayList<OperationTreserorieDto>();
		paymentRules.forEach(paymentRule -> {
			OperationTreserorieDto operation = new OperationTreserorieDto();
			operation.setOpperationType(paymentRule.getPaymentRuleOperationType());
			operation.setOperationDate(paymentRule.getPaymentRuleDeadlineDate());
			operation.setOperationAmountS(paymentRule.getPaymentRuleAmountS());
			operation.setOperationAmount(paymentRule.getPaymentRuleAmount());
			operation.setOperationRealId(paymentRule.getPaymentRuleId());
			operation.setValidated(paymentRule.isValidated());
			if(paymentRule.getCustomer() != null) {
				operation.setBeneficiaryName(paymentRule.getCustomer().getCustomerLabel());
			} else if (paymentRule.getProvider() != null) {
				operation.setBeneficiaryName(paymentRule.getProvider().getProviderLabel());
			}
			
			operation.setOperationAccount(paymentRule.getPaymentRuleAccount());
			operation.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
			
			List<String> detailsPayements = new ArrayList<String>();
			detailsPayements.add(paymentRule.getPaymentRulePaymentMethod().toString());
			detailsPayements.add(paymentRule.getPaymentRuleNumber());
			detailsPayements.add(paymentRule.getPaymentRuleDetails());
			operation.setOpperationDetails(detailsPayements);
			String label = "";
			if (paymentRule.getPaymentRuleOperationType() == OperationType.ENCAISSEMENT) {
				label = "ENCAISSEMENT " + paymentRule.getPaymentRulePaymentMethod().toString().toUpperCase();
				operation.setOperationRealType(OperationDtoType.REGLEMENT_FACTURE_CLIENT);
				operation.setOpperationType(OperationType.ENCAISSEMENT);
			} else {
				label = "DECAISSEMENT " + paymentRule.getPaymentRulePaymentMethod().toString().toUpperCase();
				operation.setOperationRealType(OperationDtoType.PAIEMENT_FACTURE_FOURNISSEUR);
				operation.setOpperationType(OperationType.DECAISSEMENT);
			}

			if (paymentRule.getPaymentRuleNumber() != null) {
				label = label + " N° " + paymentRule.getPaymentRuleNumber().toUpperCase();
			} else if (paymentRule.getPaymentRuleDetails() != null) {
				label = label + " N° " + paymentRule.getPaymentRuleDetails().toUpperCase();
			}
			operation.setOpperationLabel(label);
			// Ajouter une information sur la facture payé dans le label
			/*
			 * String label = "Payement de la facture "; String invoicesIds =
			 * paymentRule.getPaymentRuleInvoices(); List<Invoice> invoices =
			 * new ArrayList<Invoice>(); String[] ids = invoicesIds.split(",");
			 * 
			 * Arrays.stream(ids).forEach(invoiceId -> { if
			 * (paymentRule.getPaymentRuleOperationType() ==
			 * OperationType.DECAISSEMENT) { ProviderInvoice providerInvoice =
			 * providerInvoiceService
			 * .getProviderInvoiceById(Long.parseLong(invoiceId));
			 * operation.setOpperationCurrency(providerInvoice.
			 * getInvoiceCurrency());
			 * operation.setOpperationType(OperationType.DECAISSEMENT);
			 * invoices.add(providerInvoice); } else { CustomerInvoice
			 * customerInvoice = customerInvoiceService
			 * .getCustomerInvoiceById(Long.parseLong(invoiceId));
			 * operation.setOpperationCurrency(customerInvoice.
			 * getInvoiceCurrency());
			 * operation.setOpperationType(OperationType.ENCAISSEMENT);
			 * invoices.add(customerInvoice); }
			 * 
			 * });
			 * 
			 * for (int i = 0; i < invoices.size(); i++) { label = label +
			 * invoices.get(i).getInvoiceNumber(); if (i < invoices.size() - 1)
			 * { label = label + " et "; } }
			 * 
			 * operation.setOpperationLabel(label);
			 */

			operations.add(operation);

			// Ajouter une opération rélative au comission de type de paiement
			if (paymentRule.getPaymentRuleOperationType() == OperationType.ENCAISSEMENT) {
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
						operationCom.setOperationRealId(paymentRule.getPaymentRuleId());
						operationCom.setOperationRealType(OperationDtoType.COMISSION);
						operationCom.setValidated(paymentRule.isRelatedComissionValidated());
						operationCom.setOperationAccount(paymentRule.getPaymentRuleAccount());
						operationCom.setDecaissementType(Constants.PAYMENT_RULE);
						operationCom.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
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
						operationCom.setOperationRealId(paymentRule.getPaymentRuleId());
						operationCom.setOperationRealType(OperationDtoType.COMISSION);
						operationCom.setOperationAccount(paymentRule.getPaymentRuleAccount());
						operationCom.setValidated(paymentRule.isRelatedComissionValidated());
						operationCom.setDecaissementType(Constants.PAYMENT_RULE);
						operationCom.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
						List<String> detailsPayementsCom = new ArrayList<String>();
						detailsPayements.add("Comission de virement ");
						operationCom.setOpperationDetails(detailsPayements);
						operations.add(operationCom);
					});

				} else if (paymentRule.getPaymentRulePaymentMethod() == PaymentMethod.TRAITE) {
					List<Comission> traiteComissions = comissions.stream()
							.filter(comission -> comission.getComissionOperation() == Operation.REMISE_EFFET_ENCAISSEMENT)
							.collect(Collectors.toList());

					traiteComissions.stream().forEach(com -> {
						OperationTreserorieDto operationCom = new OperationTreserorieDto();
						operationCom.setOpperationType(OperationType.DECAISSEMENT);
						operationCom.setOperationDate(paymentRule.getPaymentRuleDeadlineDate());
						operationCom.setOperationAmountS(com.getComissionValueS());
						operationCom.setOperationAmount(com.getComissionValue());
						operationCom.setOperationRealId(paymentRule.getPaymentRuleId());
						operationCom.setValidated(paymentRule.isRelatedComissionValidated());
						operationCom.setDecaissementType(Constants.PAYMENT_RULE);
						operationCom.setOperationRealType(OperationDtoType.COMISSION);
						operationCom.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
						operationCom.setOperationAccount(paymentRule.getPaymentRuleAccount());

						operationCom
								.setOpperationLabel("COMISSION REMISE TRAITE N° " + paymentRule.getPaymentRuleNumber());
						operationCom.setOpperationCurrency(operation.getOpperationCurrency());
						List<String> detailsPayementsCom = new ArrayList<String>();
						detailsPayements
								.add("Comission de remise de traite numéro " + paymentRule.getPaymentRuleNumber());
						operationCom.setOpperationDetails(detailsPayements);
						operations.add(operationCom);
					});

				} else if (paymentRule.getPaymentRulePaymentMethod() == PaymentMethod.EFFET_ESCOMPTE) {
					List<Comission> traiteComissions = comissions.stream()
							.filter(comission -> comission.getComissionOperation() == Operation.REMISE_EFFET_ESCOMPTE)
							.collect(Collectors.toList());

					traiteComissions.stream().forEach(com -> {
						OperationTreserorieDto operationCom = new OperationTreserorieDto();
						operationCom.setOpperationType(OperationType.DECAISSEMENT);
						operationCom.setOperationDate(paymentRule.getPaymentRuleDeadlineDate());
						operationCom.setOperationAmountS(com.getComissionValueS());
						operationCom.setOperationAmount(com.getComissionValue());
						operationCom.setOperationRealId(paymentRule.getPaymentRuleId());
						operationCom.setValidated(paymentRule.isRelatedComissionValidated());
						operationCom.setDecaissementType(Constants.PAYMENT_RULE);
						operationCom.setOperationRealType(OperationDtoType.COMISSION);
						operationCom.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
						operationCom.setOperationAccount(paymentRule.getPaymentRuleAccount());

						operationCom
								.setOpperationLabel("COMISSION REMISE TRAITE ESCOMPTE N° " + paymentRule.getPaymentRuleNumber());
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
				 * effetEncaissementsComissions = comissions.stream()
				 * .filter(comission -> comission.getComissionOperation() ==
				 * Operation.REMISE_EFFET_ENCAISSEMENT)
				 * .collect(Collectors.toList());
				 * 
				 * effetEncaissementsComissions.stream().forEach(com -> {
				 * OperationTreserorieDto operationCom = new
				 * OperationTreserorieDto();
				 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
				 * operationCom.setOperationDate(paymentRule.
				 * getPaymentRuleDeadlineDate());
				 * operationCom.setOperationAmountS(com.getComissionValueS());
				 * operationCom.setOperationAmount(com.getComissionValue());
				 * operationCom.
				 * setOpperationLabel("Comission remise effet d'encaissement");
				 * operationCom.setOpperationCurrency(operation.
				 * getOpperationCurrency()); List<String> detailsPayementsCom =
				 * new ArrayList<String>(); detailsPayements.
				 * add("Comission de remise d'effet d'encaissement");
				 * operationCom.setOpperationDetails(detailsPayements);
				 * operations.add(operationCom); });
				 * 
				 * } else if (paymentRule.getPaymentRulePaymentMethod() ==
				 * PaymentMethod.EFFET_ESCOMPTE) { List<Comission>
				 * effetEscomptesComissions = comissions.stream()
				 * .filter(comission -> comission.getComissionOperation() ==
				 * Operation.REMISE_EFFET_ESCOMPTE)
				 * .collect(Collectors.toList());
				 * 
				 * effetEscomptesComissions.stream().forEach(com -> {
				 * OperationTreserorieDto operationCom = new
				 * OperationTreserorieDto();
				 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
				 * operationCom.setOperationDate(paymentRule.
				 * getPaymentRuleDeadlineDate());
				 * operationCom.setOperationAmountS(com.getComissionValueS());
				 * operationCom.setOperationAmount(com.getComissionValue());
				 * operationCom.
				 * setOpperationLabel("Comission remise effet d'escompte");
				 * operationCom.setOpperationCurrency(operation.
				 * getOpperationCurrency()); List<String> detailsPayementsCom =
				 * new ArrayList<String>();
				 * detailsPayements.add("Comission de remise d'effet d'escompte"
				 * ); operationCom.setOpperationDetails(detailsPayements);
				 * operations.add(operationCom); });
				 * 
				 * }
				 */
			}else if (paymentRule.getPaymentRuleOperationType() == OperationType.DECAISSEMENT) {
				if (paymentRule.getPaymentRulePaymentMethod() == PaymentMethod.TRAITE) {
					List<Comission> traiteComissions = comissions.stream()
							.filter(comission -> comission.getComissionOperation() == Operation.REMISE_EFFET)
							.collect(Collectors.toList());

					traiteComissions.stream().forEach(com -> {
						OperationTreserorieDto operationCom = new OperationTreserorieDto();
						operationCom.setOpperationType(OperationType.DECAISSEMENT);
						operationCom.setOperationDate(paymentRule.getPaymentRuleDeadlineDate());
						operationCom.setOperationAmountS(com.getComissionValueS());
						operationCom.setOperationAmount(com.getComissionValue());
						operationCom.setOperationRealId(paymentRule.getPaymentRuleId());
						operationCom.setValidated(paymentRule.isRelatedComissionValidated());
						operationCom.setDecaissementType(Constants.PAYMENT_RULE);
						operationCom.setOperationRealType(OperationDtoType.COMISSION);
						operationCom.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
						operationCom.setOperationAccount(paymentRule.getPaymentRuleAccount());

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
			}

		});

		return operations;
	}

	private List<OperationTreserorieDto> convertDecaissementsToOperationTreserorieList(List<Decaissement> decaissements,
			List<Comission> comissions, Boolean isInTheSimulatedPeriod) {

		List<OperationTreserorieDto> operations = new ArrayList<OperationTreserorieDto>();

		decaissements.forEach(decaissement -> {
			OperationTreserorieDto operation = new OperationTreserorieDto();
			operation.setOpperationType(OperationType.DECAISSEMENT);
			operation.setOperationDate(decaissement.getDecaissementDeadlineDate());
			operation.setOperationAmountS(decaissement.getDecaissementAmountS());
			operation.setOperationAmount(decaissement.getDecaissementAmount());
			operation.setOpperationCurrency(decaissement.getDecaissementCurrency());
			operation.setOperationRealId(decaissement.getDecaissementId());
			operation.setOperationRealType(OperationDtoType.DECAISSEMENT);
			operation.setValidated(decaissement.isValidated());
			operation.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
			operation.setOperationAccount(decaissement.getDecaissementBankAccount());
			if(decaissement.getDecaissementProvider() != null) {
				operation.setBeneficiaryName(decaissement.getDecaissementProvider().getProviderLabel());
			} else {
				operation.setBeneficiaryName(decaissement.getBeneficaryName());
			}
			List<String> detailsPayements = new ArrayList<String>();
			detailsPayements.add(decaissement.getDecaissementPaymentType().toString());
			detailsPayements.add(decaissement.getDecaissementPaymentRuleNumber());
			detailsPayements.add(decaissement.getDecaissementDetails());

			operation.setOpperationDetails(detailsPayements);
			String label = "DECAISSEMENT " + decaissement.getDecaissementPaymentType().toString().toUpperCase();
			if (decaissement.getDecaissementPaymentRuleNumber() != null) {
				label = label + " N° " + decaissement.getDecaissementPaymentRuleNumber().toUpperCase();
			} else if (decaissement.getDecaissementPaymentRuleDetails() != null) {
				label = label + " " + decaissement.getDecaissementPaymentRuleDetails().toUpperCase();
			}
			operation.setOpperationLabel(label);
			// operation.setOpperationLabel(decaissement.getDecaissementType().getDecaissementTypeLabel());

			operation.setOpperationFacultatifLabel(decaissement.getDecaissementLabel());

			operations.add(operation);

			// Ajouter une opération rélative au comission de type de paiement
			
			if (decaissement.getDecaissementPaymentType() == PaymentMethod.TRAITE) {
				List<Comission> traiteComissions = comissions.stream()
						.filter(comission -> comission.getComissionOperation() == Operation.REMISE_EFFET)
						.collect(Collectors.toList());

				traiteComissions.stream().forEach(com -> {
					OperationTreserorieDto operationCom = new OperationTreserorieDto();
					operationCom.setOpperationType(OperationType.DECAISSEMENT);
					operationCom.setOperationDate(decaissement.getDecaissementDeadlineDate());
					operationCom.setOperationAmountS(com.getComissionValueS());
					operationCom.setOperationAmount(com.getComissionValue());
					operationCom.setOperationRealId(decaissement.getDecaissementId());
					operationCom.setValidated(decaissement.isRelatedComissionValidated());
					operationCom.setDecaissementType(Constants.DECAISSEMENT);
					operationCom.setOperationRealType(OperationDtoType.COMISSION);
					operationCom.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
					operationCom.setOperationAccount(decaissement.getDecaissementBankAccount());

					operationCom
							.setOpperationLabel("COMISSION REMISE TRAITE N° " + decaissement.getDecaissementPaymentRuleNumber());
					operationCom.setOpperationCurrency(operation.getOpperationCurrency());
					List<String> detailsPayementsCom = new ArrayList<String>();
					detailsPayements
							.add("Comission de remise de traite numéro " + decaissement.getDecaissementPaymentRuleNumber());
					operationCom.setOpperationDetails(detailsPayements);
					operations.add(operationCom);
				});

			} 
			

			/*if (decaissement.getDecaissementPaymentType() == PaymentMethod.CHEQUE) {
				List<Comission> chequeComissions = comissions.stream()
						.filter(comission -> comission.getComissionOperation() == Operation.REMISE_CHHEQUE)
						.collect(Collectors.toList());

				chequeComissions.stream().forEach(com -> {
					OperationTreserorieDto operationCom = new OperationTreserorieDto();
					operationCom.setOpperationType(OperationType.DECAISSEMENT);
					operationCom.setOperationDate(decaissement.getDecaissementDeadlineDate());
					operationCom.setOperationAmountS(com.getComissionValueS());
					operationCom.setOperationAmount(com.getComissionValue());
					operationCom.setOperationRealId(decaissement.getDecaissementId());
					operationCom.setOperationRealType(OperationDtoType.COMISSION);
					operationCom.setValidated(decaissement.isRelatedComissionValidated());
					operationCom.setDecaissementType(Constants.DECAISSEMENT);
					operationCom.setOpperationLabel(
							"COMISSION REMISE CHEQUE N° " + decaissement.getDecaissementPaymentRuleNumber());
					operationCom.setOpperationCurrency(operation.getOpperationCurrency());
					operationCom.setOperationAccount(decaissement.getDecaissementBankAccount());

					List<String> detailsPayementsCom = new ArrayList<String>();
					detailsPayements.add(
							"Comission de remise de chèque numéro " + decaissement.getDecaissementPaymentRuleNumber());
					operationCom.setOpperationDetails(detailsPayements);
					operationCom.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);

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
					operationCom.setValidated(decaissement.isRelatedComissionValidated());
					operationCom.setDecaissementType(Constants.DECAISSEMENT);
					operationCom.setOperationAccount(decaissement.getDecaissementBankAccount());
					operationCom.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
					List<String> detailsPayementsCom = new ArrayList<String>();
					detailsPayements.add("Comission de virement ");
					operationCom.setOperationRealId(decaissement.getDecaissementId());
					operationCom.setOperationRealType(OperationDtoType.COMISSION);
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
					operationCom.setOperationAccount(decaissement.getDecaissementBankAccount());
					operationCom.setOperationRealId(decaissement.getDecaissementId());
					operationCom.setOperationRealType(OperationDtoType.COMISSION);
					operationCom.setValidated(decaissement.isRelatedComissionValidated());
					operationCom.setDecaissementType(Constants.DECAISSEMENT);
					operationCom.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
					operationCom.setOpperationLabel(
							"COMISSION REMISE TRAITE N° " + decaissement.getDecaissementPaymentRuleNumber());
					operationCom.setOpperationCurrency(operation.getOpperationCurrency());
					List<String> detailsPayementsCom = new ArrayList<String>();
					detailsPayements.add(
							"Comission de remise de traite numéro " + decaissement.getDecaissementPaymentRuleNumber());
					operationCom.setOpperationDetails(detailsPayements);
					operations.add(operationCom);
				});

			} */
			/*
				 * else if (decaissement.getDecaissementPaymentType() ==
				 * PaymentMethod.EFFET_ENCAISSEMENT) { List<Comission>
				 * effetEncaissementsComissions = comissions.stream()
				 * .filter(comission -> comission.getComissionOperation() ==
				 * Operation.REMISE_EFFET_ENCAISSEMENT)
				 * .collect(Collectors.toList());
				 * 
				 * effetEncaissementsComissions.stream().forEach(com -> {
				 * OperationTreserorieDto operationCom = new
				 * OperationTreserorieDto();
				 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
				 * operationCom.setOperationDate(decaissement.
				 * getDecaissementDeadlineDate());
				 * operationCom.setOperationAmountS(com.getComissionValueS());
				 * operationCom.setOperationAmount(com.getComissionValue());
				 * operationCom.
				 * setOpperationLabel("Comission remise effet d'encaissement");
				 * operationCom.setOpperationCurrency(operation.
				 * getOpperationCurrency()); List<String> detailsPayementsCom =
				 * new ArrayList<String>(); detailsPayements.
				 * add("Comission de remise d'effet d'encaissement");
				 * operationCom.setOpperationDetails(detailsPayements);
				 * operations.add(operationCom); });
				 * 
				 * } else if (decaissement.getDecaissementPaymentType() ==
				 * PaymentMethod.EFFET_ESCOMPTE) { List<Comission>
				 * effetEscomptesComissions = comissions.stream()
				 * .filter(comission -> comission.getComissionOperation() ==
				 * Operation.REMISE_EFFET_ESCOMPTE)
				 * .collect(Collectors.toList());
				 * 
				 * effetEscomptesComissions.stream().forEach(com -> {
				 * OperationTreserorieDto operationCom = new
				 * OperationTreserorieDto();
				 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
				 * operationCom.setOperationDate(decaissement.
				 * getDecaissementDeadlineDate());
				 * operationCom.setOperationAmountS(com.getComissionValueS());
				 * operationCom.setOperationAmount(com.getComissionValue());
				 * operationCom.
				 * setOpperationLabel("Comission remise effet d'escompte");
				 * operationCom.setOpperationCurrency(operation.
				 * getOpperationCurrency()); List<String> detailsPayementsCom =
				 * new ArrayList<String>();
				 * detailsPayements.add("Comission de remise d'effet d'escompte"
				 * ); operationCom.setOpperationDetails(detailsPayements);
				 * operations.add(operationCom); });
				 * 
				 * }
				 */

		});

		return operations;
	}

	private List<OperationTreserorieDto> convertEncaissementsToOperationTreserorieList(List<Encaissement> encaissements,
			List<Comission> comissions, Boolean isInTheSimulatedPeriod) {

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
			operation.setOperationAccount(encaissement.getEncaissementBankAccount());
			operation.setOperationRealId(encaissement.getEncaissementId());
			operation.setOperationRealType(OperationDtoType.ENCAISSEMENT);
			operation.setValidated(encaissement.isValidated());
			if(encaissement.getEncaissementCustomer() != null) {
				operation.setBeneficiaryName(encaissement.getEncaissementCustomer().getCustomerLabel());
			} else {
				operation.setBeneficiaryName(encaissement.getBeneficaryName());
			}
			operation.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);

			operation.setOpperationDetails(detailsPayements);
			String label = "ENCAISSEMENT " + encaissement.getEncaissementPaymentRuleNumber().toString().toUpperCase();
			if (encaissement.getEncaissementPaymentRuleNumber() != null) {
				label = label + " N° " + encaissement.getEncaissementPaymentRuleNumber().toUpperCase();
			} else if (encaissement.getEncaissementPaymentRuleDetails() != null) {
				label = label + " " + encaissement.getEncaissementPaymentRuleDetails().toUpperCase();
			}
			operation.setOpperationLabel(label);

			operation.setOpperationFacultatifLabel(encaissement.getEncaissementLabel());

			operations.add(operation);
			
			
			// Ajouter une opération rélative au comission de type de paiement
				if (encaissement.getEncaissementPaymentType() == PaymentMethod.CHEQUE) {
					List<Comission> chequeComissions = comissions.stream()
							.filter(comission -> comission.getComissionOperation() == Operation.REMISE_CHHEQUE)
							.collect(Collectors.toList());

					chequeComissions.stream().forEach(com -> {
						OperationTreserorieDto operationCom = new OperationTreserorieDto();
						operationCom.setOpperationType(OperationType.DECAISSEMENT);
						operationCom.setOperationDate(encaissement.getEncaissementDeadlineDate());
						operationCom.setOperationAmountS(com.getComissionValueS());
						operationCom.setOperationAmount(com.getComissionValue());
						operationCom.setOperationRealId(encaissement.getEncaissementId());
						operationCom.setOperationRealType(OperationDtoType.COMISSION);
						operationCom.setValidated(encaissement.isRelatedComissionValidated());
						operationCom.setOperationAccount(encaissement.getEncaissementBankAccount());
						operationCom.setDecaissementType(Constants.ENCAISSEMENT);
						operationCom.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
						operationCom
								.setOpperationLabel("COMISSION REMISE CHEQUE N° " + encaissement.getEncaissementPaymentRuleNumber());
						operationCom.setOpperationCurrency(operation.getOpperationCurrency());
						List<String> detailsPayementsCom = new ArrayList<String>();
						detailsPayements
								.add("Comission de remise de chèque numéro " + encaissement.getEncaissementPaymentRuleNumber());
						operationCom.setOpperationDetails(detailsPayements);
						operations.add(operationCom);
					});

				} else if (encaissement.getEncaissementPaymentType() == PaymentMethod.VIREMENT) {
					List<Comission> virementComissions = comissions.stream()
							.filter(comission -> comission.getComissionOperation() == Operation.VIREMENT)
							.collect(Collectors.toList());

					virementComissions.stream().forEach(com -> {
						OperationTreserorieDto operationCom = new OperationTreserorieDto();
						operationCom.setOpperationType(OperationType.DECAISSEMENT);
						operationCom.setOperationDate(encaissement.getEncaissementDeadlineDate());
						operationCom.setOperationAmountS(com.getComissionValueS());
						operationCom.setOperationAmount(com.getComissionValue());
						operationCom.setOpperationLabel("COMISSION VIREMENT");
						operationCom.setOpperationCurrency(operation.getOpperationCurrency());
						operationCom.setOperationRealId(encaissement.getEncaissementId());
						operationCom.setOperationRealType(OperationDtoType.COMISSION);
						operationCom.setOperationAccount(encaissement.getEncaissementBankAccount());
						operationCom.setValidated(encaissement.isRelatedComissionValidated());
						operationCom.setDecaissementType(Constants.ENCAISSEMENT);
						operationCom.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
						List<String> detailsPayementsCom = new ArrayList<String>();
						detailsPayements.add("Comission de virement ");
						operationCom.setOpperationDetails(detailsPayements);
						operations.add(operationCom);
					});

				} else if (encaissement.getEncaissementPaymentType() == PaymentMethod.TRAITE) {
					List<Comission> traiteComissions = comissions.stream()
							.filter(comission -> comission.getComissionOperation() == Operation.REMISE_EFFET_ENCAISSEMENT)
							.collect(Collectors.toList());

					traiteComissions.stream().forEach(com -> {
						OperationTreserorieDto operationCom = new OperationTreserorieDto();
						operationCom.setOpperationType(OperationType.DECAISSEMENT);
						operationCom.setOperationDate(encaissement.getEncaissementDeadlineDate());
						operationCom.setOperationAmountS(com.getComissionValueS());
						operationCom.setOperationAmount(com.getComissionValue());
						operationCom.setOperationRealId(encaissement.getEncaissementId());
						operationCom.setValidated(encaissement.isRelatedComissionValidated());
						operationCom.setDecaissementType(Constants.ENCAISSEMENT);
						operationCom.setOperationRealType(OperationDtoType.COMISSION);
						operationCom.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
						operationCom.setOperationAccount(encaissement.getEncaissementBankAccount());

						operationCom
								.setOpperationLabel("COMISSION REMISE TRAITE N° " + encaissement.getEncaissementPaymentRuleNumber());
						operationCom.setOpperationCurrency(operation.getOpperationCurrency());
						List<String> detailsPayementsCom = new ArrayList<String>();
						detailsPayements
								.add("Comission de remise de traite numéro " + encaissement.getEncaissementPaymentRuleNumber());
						operationCom.setOpperationDetails(detailsPayements);
						operations.add(operationCom);
					});

				} else if (encaissement.getEncaissementPaymentType() == PaymentMethod.EFFET_ESCOMPTE) {
					List<Comission> traiteComissions = comissions.stream()
							.filter(comission -> comission.getComissionOperation() == Operation.REMISE_EFFET_ESCOMPTE)
							.collect(Collectors.toList());

					traiteComissions.stream().forEach(com -> {
						OperationTreserorieDto operationCom = new OperationTreserorieDto();
						operationCom.setOpperationType(OperationType.DECAISSEMENT);
						operationCom.setOperationDate(encaissement.getEncaissementDeadlineDate());
						operationCom.setOperationAmountS(com.getComissionValueS());
						operationCom.setOperationAmount(com.getComissionValue());
						operationCom.setOperationRealId(encaissement.getEncaissementId());
						operationCom.setValidated(encaissement.isRelatedComissionValidated());
						operationCom.setDecaissementType(Constants.ENCAISSEMENT);
						operationCom.setOperationRealType(OperationDtoType.COMISSION);
						operationCom.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
						operationCom.setOperationAccount(encaissement.getEncaissementBankAccount());

						operationCom
								.setOpperationLabel("COMISSION REMISE TRAITE ESCOMPTE N° " + encaissement.getEncaissementPaymentRuleNumber());
						operationCom.setOpperationCurrency(operation.getOpperationCurrency());
						List<String> detailsPayementsCom = new ArrayList<String>();
						detailsPayements
								.add("Comission de remise de traite numéro " + encaissement.getEncaissementPaymentRuleNumber());
						operationCom.setOpperationDetails(detailsPayements);
						operations.add(operationCom);
					});

				}
			
				 
			
			
			
			
			

			// Ajouter une opération rélative au comission de type de paiement

			/*
			 * if (encaissement.getEncaissementPaymentType() ==
			 * PaymentMethod.CHEQUE) { List<Comission> chequeComissions =
			 * comissions.stream() .filter(comission ->
			 * comission.getComissionOperation() == Operation.REMISE_CHHEQUE)
			 * .collect(Collectors.toList());
			 * 
			 * chequeComissions.stream().forEach(com -> { OperationTreserorieDto
			 * operationCom = new OperationTreserorieDto();
			 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
			 * operationCom.setOperationDate(encaissement.
			 * getEncaissementDeadlineDate());
			 * operationCom.setOperationAmountS(com.getComissionValueS());
			 * operationCom.setOperationAmount(com.getComissionValue());
			 * operationCom.setOpperationLabel("Comission remise chèque");
			 * operationCom.setOpperationCurrency(operation.
			 * getOpperationCurrency()); List<String> detailsPayementsCom = new
			 * ArrayList<String>(); detailsPayements.add(
			 * "Comission de remise de chèque numéro " +
			 * encaissement.getEncaissementPaymentRuleNumber());
			 * operationCom.setOpperationDetails(detailsPayements);
			 * operations.add(operationCom); });
			 * 
			 * } else if (encaissement.getEncaissementPaymentType() ==
			 * PaymentMethod.VIREMENT) { List<Comission> virementComissions =
			 * comissions.stream() .filter(comission ->
			 * comission.getComissionOperation() == Operation.VIREMENT)
			 * .collect(Collectors.toList());
			 * 
			 * virementComissions.stream().forEach(com -> {
			 * OperationTreserorieDto operationCom = new
			 * OperationTreserorieDto();
			 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
			 * operationCom.setOperationDate(encaissement.
			 * getEncaissementDeadlineDate());
			 * operationCom.setOperationAmountS(com.getComissionValueS());
			 * operationCom.setOperationAmount(com.getComissionValue());
			 * operationCom.setOpperationLabel("Comission virement");
			 * operationCom.setOpperationCurrency(operation.
			 * getOpperationCurrency()); List<String> detailsPayementsCom = new
			 * ArrayList<String>();
			 * detailsPayements.add("Comission de virement ");
			 * operationCom.setOpperationDetails(detailsPayements);
			 * operations.add(operationCom); });
			 * 
			 * } else if (encaissement.getEncaissementPaymentType() ==
			 * PaymentMethod.TRAITE) { List<Comission> traiteComissions =
			 * comissions.stream() .filter(comission ->
			 * comission.getComissionOperation() == Operation.TRAITE)
			 * .collect(Collectors.toList());
			 * 
			 * traiteComissions.stream().forEach(com -> { OperationTreserorieDto
			 * operationCom = new OperationTreserorieDto();
			 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
			 * operationCom.setOperationDate(encaissement.
			 * getEncaissementDeadlineDate());
			 * operationCom.setOperationAmountS(com.getComissionValueS());
			 * operationCom.setOperationAmount(com.getComissionValue());
			 * operationCom.setOpperationLabel("Comission remise traite");
			 * operationCom.setOpperationCurrency(operation.
			 * getOpperationCurrency()); List<String> detailsPayementsCom = new
			 * ArrayList<String>(); detailsPayements.add(
			 * "Comission de remise de traite numéro " +
			 * encaissement.getEncaissementPaymentRuleNumber());
			 * operationCom.setOpperationDetails(detailsPayements);
			 * operations.add(operationCom); });
			 * 
			 * } else if (encaissement.getEncaissementPaymentType() ==
			 * PaymentMethod.EFFET_ENCAISSEMENT) { List<Comission>
			 * effetEncaissementsComissions = comissions.stream()
			 * .filter(comission -> comission.getComissionOperation() ==
			 * Operation.REMISE_EFFET_ENCAISSEMENT)
			 * .collect(Collectors.toList());
			 * 
			 * effetEncaissementsComissions.stream().forEach(com -> {
			 * OperationTreserorieDto operationCom = new
			 * OperationTreserorieDto();
			 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
			 * operationCom.setOperationDate(encaissement.
			 * getEncaissementDeadlineDate());
			 * operationCom.setOperationAmountS(com.getComissionValueS());
			 * operationCom.setOperationAmount(com.getComissionValue());
			 * operationCom.
			 * setOpperationLabel("Comission remise effet d'encaissement");
			 * operationCom.setOpperationCurrency(operation.
			 * getOpperationCurrency()); List<String> detailsPayementsCom = new
			 * ArrayList<String>();
			 * detailsPayements.add("Comission de remise d'effet d'encaissement"
			 * ); operationCom.setOpperationDetails(detailsPayements);
			 * operations.add(operationCom); });
			 * 
			 * } else if (encaissement.getEncaissementPaymentType() ==
			 * PaymentMethod.EFFET_ESCOMPTE) { List<Comission>
			 * effetEscomptesComissions = comissions.stream() .filter(comission
			 * -> comission.getComissionOperation() ==
			 * Operation.REMISE_EFFET_ESCOMPTE) .collect(Collectors.toList());
			 * 
			 * effetEscomptesComissions.stream().forEach(com -> {
			 * OperationTreserorieDto operationCom = new
			 * OperationTreserorieDto();
			 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
			 * operationCom.setOperationDate(encaissement.
			 * getEncaissementDeadlineDate());
			 * operationCom.setOperationAmountS(com.getComissionValueS());
			 * operationCom.setOperationAmount(com.getComissionValue());
			 * operationCom.
			 * setOpperationLabel("Comission remise effet d'escompte");
			 * operationCom.setOpperationCurrency(operation.
			 * getOpperationCurrency()); List<String> detailsPayementsCom = new
			 * ArrayList<String>();
			 * detailsPayements.add("Comission de remise d'effet d'escompte");
			 * operationCom.setOpperationDetails(detailsPayements);
			 * operations.add(operationCom); });
			 * 
			 * }
			 */

		});

		return operations;
	}

	public void rapprochementBancaireModifyOperation(@RequestBody OperationTreserorieDto operation) {
		if (operation.getOperationRealType() == OperationDtoType.REGLEMENT_FACTURE_CLIENT
				|| operation.getOperationRealType() == OperationDtoType.PAIEMENT_FACTURE_FOURNISSEUR) {
			PaymentRule paymentRule = paymentRuleService.findPaymentRuleBYId(operation.getOperationRealId());
			paymentRule.setPaymentRuleAmount(operation.getOperationAmount());
			paymentRuleService.modifyPaymentRule(paymentRule);

		} else if (operation.getOperationRealType() == OperationDtoType.DECAISSEMENT) {
			Decaissement decaissement = decaissementService.getDecaissementById(operation.getOperationRealId());
			decaissement.setDecaissementAmount(operation.getOperationAmount());
			decaissementService.saveDecaissement(decaissement);

		} else if (operation.getOperationRealType() == OperationDtoType.ENCAISSEMENT) {
			Encaissement encaissement = encaissementService.getEncaissementById(operation.getOperationRealId());
			encaissement.setEncaissementAmount(operation.getOperationAmount());
			encaissementService.saveEncaissement(encaissement);

		} else if (operation.getOperationRealType() == OperationDtoType.COMISSION) {
			Comission comission = comissionService.getComissionById(operation.getOperationRealId());
			comission.setComissionValue(operation.getOperationAmount());
			comissionService.saveComission(comission);

		} else if (operation.getOperationRealType() == OperationDtoType.ECHEANCHIER) {
			TimeLine timeLine = timeLineService.findTimeLineById(operation.getOperationRealId());
			timeLine.setTimeLineInitialAmount(operation.getOperationAmount());
			timeLineService.saveTimeLine(timeLine);
		}
	}

	public void validate(OperationDtoType operationRealType, Long operationRealId, OperationTreserorieDto operation) {
		if (operationRealType == OperationDtoType.REGLEMENT_FACTURE_CLIENT
				|| operationRealType == OperationDtoType.PAIEMENT_FACTURE_FOURNISSEUR) {
			PaymentRule paymentRule = paymentRuleService.findPaymentRuleBYId(operationRealId);
			paymentRule.setValidated(true);
			paymentRuleService.modifyPaymentRule(paymentRule);
			BankAccount account = paymentRule.getPaymentRuleAccount();
			if(operationRealType == OperationDtoType.REGLEMENT_FACTURE_CLIENT) {
				account.setAccountInitialAmount(account.getAccountInitialAmount() + paymentRule.getPaymentRuleAmount());
			} else if(operationRealType == OperationDtoType.PAIEMENT_FACTURE_FOURNISSEUR) {
				account.setAccountInitialAmount(account.getAccountInitialAmount() - paymentRule.getPaymentRuleAmount());
			}
			
			bankAccountService.saveAccount(account);

		} else if (operationRealType == OperationDtoType.DECAISSEMENT) {
			Decaissement decaissement = decaissementService.getDecaissementById(operationRealId);
			decaissement.setValidated(true);
			decaissementService.saveDecaissement(decaissement);
			BankAccount account = decaissement.getDecaissementBankAccount();
			account.setAccountInitialAmount(account.getAccountInitialAmount() - decaissement.getDecaissementAmount());
			bankAccountService.saveAccount(account);

		} else if (operationRealType == OperationDtoType.ENCAISSEMENT) {
			Encaissement encaissement = encaissementService.getEncaissementById(operationRealId);
			encaissement.setValidated(true);
			encaissementService.saveEncaissement(encaissement);
			BankAccount account = encaissement.getEncaissementBankAccount();
			account.setAccountInitialAmount(account.getAccountInitialAmount() + encaissement.getEncaissementAmount());
			bankAccountService.saveAccount(account);

		} else if (operationRealType == OperationDtoType.COMISSION) {
			if (operation.getDecaissementType().equals(Constants.PAYMENT_RULE)) {
				PaymentRule paymentRule = paymentRuleService.findPaymentRuleBYId(operationRealId);
				paymentRule.setRelatedComissionValidated(true);
				paymentRuleService.modifyPaymentRule(paymentRule);
				BankAccount account = operation.getOperationAccount();
				BankAccount persistedAccound = bankAccountService.getAccountById(account.getAccountId());
				persistedAccound.setAccountInitialAmount(account.getAccountInitialAmount() - operation.getOperationAmount());
				bankAccountService.saveAccount(persistedAccound);
			} else if (operation.getDecaissementType().equals(Constants.DECAISSEMENT)) {
				Decaissement decaissement = decaissementService.getDecaissementById(operationRealId);
				decaissement.setRelatedComissionValidated(true);
				decaissementService.saveDecaissement(decaissement);
				BankAccount account = operation.getOperationAccount();
				BankAccount persistedAccound = bankAccountService.getAccountById(account.getAccountId());
				persistedAccound.setAccountInitialAmount(account.getAccountInitialAmount() - operation.getOperationAmount());
				bankAccountService.saveAccount(persistedAccound);
			} else if (operation.getDecaissementType().equals(Constants.ENCAISSEMENT)) {
				Encaissement encaissement = encaissementService.getEncaissementById(operationRealId);
				encaissement.setRelatedComissionValidated(true);
				encaissementService.saveEncaissement(encaissement);
				BankAccount account = operation.getOperationAccount();
				BankAccount persistedAccound = bankAccountService.getAccountById(account.getAccountId());
				persistedAccound.setAccountInitialAmount(account.getAccountInitialAmount() - operation.getOperationAmount());
				bankAccountService.saveAccount(persistedAccound);
			}

		} else if (operationRealType == OperationDtoType.ECHEANCHIER) {
			TimeLineEntry entry = timeLineEntryService.findTimeLineEntryById(operationRealId);
			entry.setIsValidated(true);
			timeLineEntryService.saveTimeLineEntry(entry);
			BankAccount account = operation.getOperationAccount();
			account.setAccountInitialAmount(account.getAccountInitialAmount() - operation.getOperationAmount());
			bankAccountService.saveAccount(account);

		}

	}
}
