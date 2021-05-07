package org.apac.erp.cach.forecast.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.apac.erp.cach.forecast.constants.Constants;
import org.apac.erp.cach.forecast.constants.Utils;
import org.apac.erp.cach.forecast.dtos.*;
import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.enumeration.Operation;
import org.apac.erp.cach.forecast.enumeration.OperationDtoType;
import org.apac.erp.cach.forecast.enumeration.OperationType;
import org.apac.erp.cach.forecast.enumeration.PaymentMethod;
import org.apac.erp.cach.forecast.persistence.entities.*;
import org.apac.erp.cach.forecast.persistence.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.thymeleaf.util.DateUtils;

@Service
public class SupervisionTresorerieService {

	@Autowired
	private PaymentRuleService paymentRuleService;

	@Autowired
	private ProviderInvoiceService providerInvoiceService;

	@Autowired
	private CustomerInvoiceService customerInvoiceService;
	@Autowired
	private ProductGroupRepository productGroupRepository;
	@Autowired
	private ProductRepository productRepository;

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
	@Autowired
	private HistoryOperationBankService historyOperationBankService;
	@Autowired
	FactureRepository factureRepository;
	@Autowired
	private FactureLineRepository factureLineRepository;
	@Autowired
	CustomerRepository customerRepository;


	public List<OperationTreserorieDto> globalSupervisionEngage(Long accountId, Date startDate, Date endDate,
																Boolean isValidated) {

		// test

		System.out.println("start date --------------------------- " + startDate);

		System.out.println("end date ----------------------------- " + endDate);
		ZoneId z = ZoneId.of("Africa/Tunis");
		LocalDate localStartDate = startDate.toInstant().atZone(z).toLocalDate();
		LocalDate localEndDate = endDate.toInstant().atZone(z).toLocalDate();

		Date startDate1 = Date.from(localStartDate.atStartOfDay(z).toInstant());
		Date endDate1 = Date.from(localEndDate.atStartOfDay(z).toInstant());

		System.out.println("start date --------------------------- " + startDate1);

		System.out.println("end date ----------------------------- " + endDate1);

		// fin test

		List<OperationTreserorieDto> operations = new ArrayList<OperationTreserorieDto>();

		BankAccount bankAccount = bankAccountService.getAccountById(accountId);
		HistoricAccountSold initialAmountInTheBeginingOfThePeriod = this.historicAccountSoldService
				.findFirst(bankAccount.getAccountId(), startDate);
		HistoricAccountSold lastAmountOfAccount = this.historicAccountSoldService.findLast(bankAccount.getAccountId());
		// Double initialAmount = bankAchttps://github.com/mohamedHedi1990/erp-cash-forecast/edit/etatVente/src/main/java/org/apac/erp/cach/forecast/service/SupervisionTresorerieService.java?pr=%2FmohamedHedi1990%2Ferp-cash-forecast%2Fpull%2F59count.getAccountInitialAmount();
		Double initialAmount = 0.0;
		if (lastAmountOfAccount != null) {
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

		// Trouver toutes les opérations non validés avant cette date

		List<PaymentRule> paymentRulesNV = paymentRuleService.getAllNonValidatedBeforeDate(bankAccount, startDate);
		List<OperationTreserorieDto> paymentRuleOperationsNV = convertPaymentRulesToOperationTreserorieList(
				paymentRulesNV, comissions, false);
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
				List<OperationTreserorieDto> timeLineEntriesOperations = convertTimeLineEntriesToOperationsTreserorieList(bankAccount,
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
					/*if (operations.get(i).getOpperationType() == OperationType.ENCAISSEMENT)
						operations.get(i).setProgressiveAmount(initialAmount + operations.get(i).getOperationAmount());
					else
						operations.get(i).setProgressiveAmount(initialAmount - operations.get(i).getOperationAmount());*/
					if (!operations.get(i).isValidated()) {
						if (operations.get(i).getOpperationType() == OperationType.ENCAISSEMENT)
							operations.get(i)
									.setProgressiveAmount(initialAmount + operations.get(i).getOperationAmount());
						else
							operations.get(i)
									.setProgressiveAmount(initialAmount - operations.get(i).getOperationAmount());
					} else {
						operations.get(i).setProgressiveAmount(initialAmount);
					}
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
					/*if (operations.get(i).getOpperationType() == OperationType.ENCAISSEMENT)
						operations.get(i).setProgressiveAmount(
								operations.get(i - 1).getProgressiveAmount() + operations.get(i).getOperationAmount());
					else
						operations.get(i).setProgressiveAmount(
								operations.get(i - 1).getProgressiveAmount() - operations.get(i).getOperationAmount());*/
					if (!operations.get(i).isValidated()) {
						if (operations.get(i).getOpperationType() == OperationType.ENCAISSEMENT)
							operations.get(i).setProgressiveAmount(operations.get(i - 1).getProgressiveAmount()
									+ operations.get(i).getOperationAmount());
						else
							operations.get(i).setProgressiveAmount(operations.get(i - 1).getProgressiveAmount()
									- operations.get(i).getOperationAmount());
					} else {
						operations.get(i).setProgressiveAmount(operations.get(i - 1).getProgressiveAmount());
					}
				}

			}
			double progressiveAmount = (double) Math.round(operations.get(i).getProgressiveAmount() * 100) / 100;
			operations.get(i).setProgressiveAmount(progressiveAmount);
			operations.get(i)
					.setProgressiveAmountS(Utils.convertAmountToStringWithSeperator(operations.get(i).getProgressiveAmount()));
		}
		return operations;
	}

	private List<OperationTreserorieDto> convertTimeLineEntriesToOperationsTreserorieList(BankAccount bankAccount, List<TimeLineEntry> entries,
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
			operation.setOperationAccount(bankAccount);
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
			if (paymentRule.getCustomer() != null) {
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
				label = "ENCAISSEMENT " + paymentRule.getPaymentRulePaymentMethod().toString().toUpperCase().replace("_", " ");
				operation.setOperationRealType(OperationDtoType.REGLEMENT_FACTURE_CLIENT);
				operation.setOpperationType(OperationType.ENCAISSEMENT);
			} else {
				label = "DECAISSEMENT " + paymentRule.getPaymentRulePaymentMethod().toString().toUpperCase().replace("_", " ");
				operation.setOperationRealType(OperationDtoType.PAIEMENT_FACTURE_FOURNISSEUR);
				operation.setOpperationType(OperationType.DECAISSEMENT);
			}

			if (paymentRule.getPaymentRuleNumber() != null) {
				label = label + " N° " + paymentRule.getPaymentRuleNumber().toUpperCase().replace("_", " ");;
			} else if (paymentRule.getPaymentRuleDetails() != null) {
				label = label + " N° " + paymentRule.getPaymentRuleDetails().toUpperCase().replace("_", " ");;
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
						operationCom.setBeneficiaryName("COMISSION BANCAIRE");
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
						operationCom.setBeneficiaryName("COMISSION BANCAIRE");

						List<String> detailsPayementsCom = new ArrayList<String>();
						detailsPayements.add("Comission de virement ");
						operationCom.setOpperationDetails(detailsPayements);
						operations.add(operationCom);
					});

				} else if (paymentRule.getPaymentRulePaymentMethod() == PaymentMethod.TRAITE) {
					List<Comission> traiteComissions = comissions.stream().filter(
							comission -> comission.getComissionOperation() == Operation.REMISE_EFFET_ENCAISSEMENT)
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
						operationCom.setBeneficiaryName("COMISSION BANCAIRE");

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
						operationCom.setBeneficiaryName("COMISSION BANCAIRE");

						operationCom.setOpperationLabel(
								"COMISSION REMISE TRAITE ESCOMPTE N° " + paymentRule.getPaymentRuleNumber());
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
			} else if (paymentRule.getPaymentRuleOperationType() == OperationType.DECAISSEMENT) {
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
						operationCom.setBeneficiaryName("COMISSION BANCAIRE");

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
			if (decaissement.getDecaissementProvider() != null) {
				operation.setBeneficiaryName(decaissement.getDecaissementProvider().getProviderLabel());
			} else {
				operation.setBeneficiaryName(decaissement.getBeneficaryName());
			}
			if(decaissement.getDecaissementPaymentType() == PaymentMethod.COMISSION_BANCAIRE &&
					decaissement.getBeneficaryName() == null) {
				operation.setBeneficiaryName("COMISSION BANCAIRE");
			}

			List<String> detailsPayements = new ArrayList<String>();
			detailsPayements.add(decaissement.getDecaissementPaymentType().toString());
			detailsPayements.add(decaissement.getDecaissementPaymentRuleNumber());
			detailsPayements.add(decaissement.getDecaissementDetails());

			operation.setOpperationDetails(detailsPayements);

			String label = "DECAISSEMENT " + decaissement.getDecaissementPaymentType().toString().toUpperCase().replace("_", " ");
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
					operationCom.setBeneficiaryName("COMISSION BANCAIRE");

					operationCom.setOpperationLabel(
							"COMISSION REMISE TRAITE N° " + decaissement.getDecaissementPaymentRuleNumber());
					operationCom.setOpperationCurrency(operation.getOpperationCurrency());
					List<String> detailsPayementsCom = new ArrayList<String>();
					detailsPayements.add(
							"Comission de remise de traite numéro " + decaissement.getDecaissementPaymentRuleNumber());
					operationCom.setOpperationDetails(detailsPayements);
					operations.add(operationCom);
				});

			}

			/*
			 * if (decaissement.getDecaissementPaymentType() ==
			 * PaymentMethod.CHEQUE) { List<Comission> chequeComissions =
			 * comissions.stream() .filter(comission ->
			 * comission.getComissionOperation() == Operation.REMISE_CHHEQUE)
			 * .collect(Collectors.toList());
			 *
			 * chequeComissions.stream().forEach(com -> { OperationTreserorieDto
			 * operationCom = new OperationTreserorieDto();
			 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
			 * operationCom.setOperationDate(decaissement.
			 * getDecaissementDeadlineDate());
			 * operationCom.setOperationAmountS(com.getComissionValueS());
			 * operationCom.setOperationAmount(com.getComissionValue());
			 * operationCom.setOperationRealId(decaissement.getDecaissementId())
			 * ; operationCom.setOperationRealType(OperationDtoType.COMISSION);
			 * operationCom.setValidated(decaissement.
			 * isRelatedComissionValidated());
			 * operationCom.setDecaissementType(Constants.DECAISSEMENT);
			 * operationCom.setOpperationLabel( "COMISSION REMISE CHEQUE N° " +
			 * decaissement.getDecaissementPaymentRuleNumber());
			 * operationCom.setOpperationCurrency(operation.
			 * getOpperationCurrency());
			 * operationCom.setOperationAccount(decaissement.
			 * getDecaissementBankAccount());
			 *
			 * List<String> detailsPayementsCom = new ArrayList<String>();
			 * detailsPayements.add( "Comission de remise de chèque numéro " +
			 * decaissement.getDecaissementPaymentRuleNumber());
			 * operationCom.setOpperationDetails(detailsPayements);
			 * operationCom.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
			 *
			 * operations.add(operationCom); });
			 *
			 * } else if (decaissement.getDecaissementPaymentType() ==
			 * PaymentMethod.VIREMENT) { List<Comission> virementComissions =
			 * comissions.stream() .filter(comission ->
			 * comission.getComissionOperation() == Operation.VIREMENT)
			 * .collect(Collectors.toList());
			 *
			 * virementComissions.stream().forEach(com -> {
			 * OperationTreserorieDto operationCom = new
			 * OperationTreserorieDto();
			 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
			 * operationCom.setOperationDate(decaissement.
			 * getDecaissementDeadlineDate());
			 * operationCom.setOperationAmountS(com.getComissionValueS());
			 * operationCom.setOperationAmount(com.getComissionValue());
			 * operationCom.setOpperationLabel("COMISSION VIREMENT");
			 * operationCom.setOpperationCurrency(operation.
			 * getOpperationCurrency()); operationCom.setValidated(decaissement.
			 * isRelatedComissionValidated());
			 * operationCom.setDecaissementType(Constants.DECAISSEMENT);
			 * operationCom.setOperationAccount(decaissement.
			 * getDecaissementBankAccount());
			 * operationCom.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
			 * List<String> detailsPayementsCom = new ArrayList<String>();
			 * detailsPayements.add("Comission de virement ");
			 * operationCom.setOperationRealId(decaissement.getDecaissementId())
			 * ; operationCom.setOperationRealType(OperationDtoType.COMISSION);
			 * operationCom.setOpperationDetails(detailsPayements);
			 * operations.add(operationCom); });
			 *
			 * } else if (decaissement.getDecaissementPaymentType() ==
			 * PaymentMethod.TRAITE) { List<Comission> traiteComissions =
			 * comissions.stream() .filter(comission ->
			 * comission.getComissionOperation() == Operation.TRAITE)
			 * .collect(Collectors.toList());
			 *
			 * traiteComissions.stream().forEach(com -> { OperationTreserorieDto
			 * operationCom = new OperationTreserorieDto();
			 * operationCom.setOpperationType(OperationType.DECAISSEMENT);
			 * operationCom.setOperationDate(decaissement.
			 * getDecaissementDeadlineDate());
			 * operationCom.setOperationAmountS(com.getComissionValueS());
			 * operationCom.setOperationAmount(com.getComissionValue());
			 * operationCom.setOperationAccount(decaissement.
			 * getDecaissementBankAccount());
			 * operationCom.setOperationRealId(decaissement.getDecaissementId())
			 * ; operationCom.setOperationRealType(OperationDtoType.COMISSION);
			 * operationCom.setValidated(decaissement.
			 * isRelatedComissionValidated());
			 * operationCom.setDecaissementType(Constants.DECAISSEMENT);
			 * operationCom.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);
			 * operationCom.setOpperationLabel( "COMISSION REMISE TRAITE N° " +
			 * decaissement.getDecaissementPaymentRuleNumber());
			 * operationCom.setOpperationCurrency(operation.
			 * getOpperationCurrency()); List<String> detailsPayementsCom = new
			 * ArrayList<String>(); detailsPayements.add(
			 * "Comission de remise de traite numéro " +
			 * decaissement.getDecaissementPaymentRuleNumber());
			 * operationCom.setOpperationDetails(detailsPayements);
			 * operations.add(operationCom); });
			 *
			 * }
			 */
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
			 * getOpperationCurrency()); List<String> detailsPayementsCom = new
			 * ArrayList<String>(); detailsPayements.
			 * add("Comission de remise d'effet d'encaissement");
			 * operationCom.setOpperationDetails(detailsPayements);
			 * operations.add(operationCom); });
			 *
			 * } else if (decaissement.getDecaissementPaymentType() ==
			 * PaymentMethod.EFFET_ESCOMPTE) { List<Comission>
			 * effetEscomptesComissions = comissions.stream() .filter(comission
			 * -> comission.getComissionOperation() ==
			 * Operation.REMISE_EFFET_ESCOMPTE) .collect(Collectors.toList());
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
			 * getOpperationCurrency()); List<String> detailsPayementsCom = new
			 * ArrayList<String>();
			 * detailsPayements.add("Comission de remise d'effet d'escompte" );
			 * operationCom.setOpperationDetails(detailsPayements);
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
			if (encaissement.getEncaissementCustomer() != null) {
				operation.setBeneficiaryName(encaissement.getEncaissementCustomer().getCustomerLabel());
			} else {
				operation.setBeneficiaryName(encaissement.getBeneficaryName());
			}
			operation.setIsInTheSimulatedPeriod(isInTheSimulatedPeriod);

			operation.setOpperationDetails(detailsPayements);
			String label = "ENCAISSEMENT " + encaissement.getEncaissementPaymentType().toString().toUpperCase().replace("_", " ");;
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
					operationCom.setOpperationLabel(
							"COMISSION REMISE CHEQUE N° " + encaissement.getEncaissementPaymentRuleNumber());
					operationCom.setOpperationCurrency(operation.getOpperationCurrency());
					List<String> detailsPayementsCom = new ArrayList<String>();
					detailsPayements.add(
							"Comission de remise de chèque numéro " + encaissement.getEncaissementPaymentRuleNumber());
					operationCom.setOpperationDetails(detailsPayements);
					operationCom.setBeneficiaryName("COMISSION BANCAIRE");

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
					operationCom.setBeneficiaryName("COMISSION BANCAIRE");

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
					operationCom.setBeneficiaryName("COMISSION BANCAIRE");

					operationCom.setOpperationLabel(
							"COMISSION REMISE TRAITE N° " + encaissement.getEncaissementPaymentRuleNumber());
					operationCom.setOpperationCurrency(operation.getOpperationCurrency());
					List<String> detailsPayementsCom = new ArrayList<String>();
					detailsPayements.add(
							"Comission de remise de traite numéro " + encaissement.getEncaissementPaymentRuleNumber());
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
					operationCom.setBeneficiaryName("COMISSION BANCAIRE");

					operationCom.setOpperationLabel(
							"COMISSION REMISE TRAITE ESCOMPTE N° " + encaissement.getEncaissementPaymentRuleNumber());
					operationCom.setOpperationCurrency(operation.getOpperationCurrency());
					List<String> detailsPayementsCom = new ArrayList<String>();
					detailsPayements.add(
							"Comission de remise de traite numéro " + encaissement.getEncaissementPaymentRuleNumber());
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
		Double initalAmount=null;
		Double finalAmount=null;
		BankAccount bankAccount=null;
		if (operationRealType == OperationDtoType.REGLEMENT_FACTURE_CLIENT
				|| operationRealType == OperationDtoType.PAIEMENT_FACTURE_FOURNISSEUR) {
			PaymentRule paymentRule = paymentRuleService.findPaymentRuleBYId(operationRealId);
			paymentRule.setValidated(true);
			paymentRuleService.modifyPaymentRule(paymentRule);
			BankAccount account = paymentRule.getPaymentRuleAccount();
			bankAccount=account;
			initalAmount=account.getAccountInitialAmount();
			if (operationRealType == OperationDtoType.REGLEMENT_FACTURE_CLIENT) {
				account.setAccountInitialAmount(account.getAccountInitialAmount() + paymentRule.getPaymentRuleAmount());
			} else if (operationRealType == OperationDtoType.PAIEMENT_FACTURE_FOURNISSEUR) {
				account.setAccountInitialAmount(account.getAccountInitialAmount() - paymentRule.getPaymentRuleAmount());

			}
			finalAmount=account.getAccountInitialAmount();
			bankAccountService.saveAccount(account);

		} else if (operationRealType == OperationDtoType.DECAISSEMENT) {
			Decaissement decaissement = decaissementService.getDecaissementById(operationRealId);
			decaissement.setValidated(true);
			decaissementService.saveDecaissement(decaissement);
			BankAccount account = decaissement.getDecaissementBankAccount();
			bankAccount=account;
			initalAmount=account.getAccountInitialAmount();
			account.setAccountInitialAmount(account.getAccountInitialAmount() - decaissement.getDecaissementAmount());
			finalAmount=account.getAccountInitialAmount();
			bankAccountService.saveAccount(account);

		} else if (operationRealType == OperationDtoType.ENCAISSEMENT) {
			Encaissement encaissement = encaissementService.getEncaissementById(operationRealId);
			encaissement.setValidated(true);
			encaissementService.saveEncaissement(encaissement);
			BankAccount account = encaissement.getEncaissementBankAccount();
			bankAccount=account;
			initalAmount=account.getAccountInitialAmount();
			account.setAccountInitialAmount(account.getAccountInitialAmount() + encaissement.getEncaissementAmount());
			finalAmount=account.getAccountInitialAmount();
			bankAccountService.saveAccount(account);

		} else if (operationRealType == OperationDtoType.COMISSION) {
			if (operation.getDecaissementType().equals(Constants.PAYMENT_RULE)) {
				PaymentRule paymentRule = paymentRuleService.findPaymentRuleBYId(operationRealId);
				paymentRule.setRelatedComissionValidated(true);
				paymentRuleService.modifyPaymentRule(paymentRule);
				BankAccount account = operation.getOperationAccount();
				BankAccount persistedAccound = bankAccountService.getAccountById(account.getAccountId());
				bankAccount=persistedAccound;
				initalAmount=persistedAccound.getAccountInitialAmount();
				persistedAccound.setAccountInitialAmount(account.getAccountInitialAmount() - operation.getOperationAmount());
				finalAmount=persistedAccound.getAccountInitialAmount();

				bankAccountService.saveAccount(persistedAccound);
			} else if (operation.getDecaissementType().equals(Constants.DECAISSEMENT)) {
				Decaissement decaissement = decaissementService.getDecaissementById(operationRealId);
				decaissement.setRelatedComissionValidated(true);
				decaissementService.saveDecaissement(decaissement);
				BankAccount account = operation.getOperationAccount();
				BankAccount persistedAccound = bankAccountService.getAccountById(account.getAccountId());
				bankAccount=persistedAccound;
				initalAmount=persistedAccound.getAccountInitialAmount();
				persistedAccound.setAccountInitialAmount(account.getAccountInitialAmount() - operation.getOperationAmount());
				finalAmount=persistedAccound.getAccountInitialAmount();

				bankAccountService.saveAccount(persistedAccound);
			} else if (operation.getDecaissementType().equals(Constants.ENCAISSEMENT)) {
				Encaissement encaissement = encaissementService.getEncaissementById(operationRealId);
				encaissement.setRelatedComissionValidated(true);
				encaissementService.saveEncaissement(encaissement);
				BankAccount account = operation.getOperationAccount();
				BankAccount persistedAccound = bankAccountService.getAccountById(account.getAccountId());
				bankAccount=persistedAccound;
				initalAmount=persistedAccound.getAccountInitialAmount();
				persistedAccound.setAccountInitialAmount(account.getAccountInitialAmount() - operation.getOperationAmount());
				finalAmount=persistedAccound.getAccountInitialAmount();

				bankAccountService.saveAccount(persistedAccound);
			}

		} else if (operationRealType == OperationDtoType.ECHEANCHIER) {
			TimeLineEntry entry = timeLineEntryService.findTimeLineEntryById(operationRealId);
			entry.setIsValidated(true);
			timeLineEntryService.saveTimeLineEntry(entry);
			BankAccount account = operation.getOperationAccount();
			bankAccount=account;
			initalAmount=account.getAccountInitialAmount();
			account.setAccountInitialAmount(account.getAccountInitialAmount() - operation.getOperationAmount());
			finalAmount=account.getAccountInitialAmount();

			bankAccountService.saveAccount(account);

		}
		HistoryOperationBank historyOperationBank=new HistoryOperationBank();
		historyOperationBank.setBankAccount(bankAccount);
		historyOperationBank.setHistoryOperationBankAmount(operation.getOperationAmount());
		historyOperationBank.setHistoryOperationBankDate(operation.getHistoryOperationDate());
		historyOperationBank.setHistoryOperationBankLabel(operation.getOpperationLabel());
		historyOperationBank.setHistoryOperationBankValueDate(operation.getOperationDate());
		historyOperationBank.setHistoryOperationBankType(operation.getOpperationType());
		historyOperationBank.setHistoryOperationBankInitialAmount(initalAmount);
		historyOperationBank.setHistoryOperationBankFinalAmount(finalAmount);
		historyOperationBankService.saveHistoryOperationBank(historyOperationBank);


	}

	// etat non engagé

	public List<OperationTreserorieDto> nonEngageSupervision(Date startDate, Date endDate, boolean isCustomer,
															 boolean isProvider) {

		List<OperationTreserorieDto> operations = new ArrayList<OperationTreserorieDto>();

		if (isCustomer ||(!isCustomer && !isProvider)) {
			// Trouver toutes les factures clients ouvertes
			List<CustomerInvoice> customerInvoices = this.customerInvoiceService
					.findByInvoiceStatusAndInvoiceDeadlineDate(InvoiceStatus.OPENED, startDate, endDate);
			customerInvoices.addAll(this.customerInvoiceService.findByInvoiceStatusAndInvoiceDeadlineDate(InvoiceStatus.OPENED, startDate));
			customerInvoices.stream().forEach(customerInvocie -> {
				OperationTreserorieDto operation = new OperationTreserorieDto();
				if(customerInvocie.getInvoiceDeadlineDate().compareTo(startDate) < 0) {
					operation.setIsInTheSimulatedPeriod(false);
				} else {
					operation.setIsInTheSimulatedPeriod(true);
				}
				if (customerInvocie.getInvoiceTotalAmount() < 0)
					operation.setOpperationType(OperationType.DECAISSEMENT);
				else
					operation.setOpperationType(OperationType.ENCAISSEMENT);
				operation.setOperationDate(customerInvocie.getInvoiceDeadlineDate());
				double montantRestant = customerInvocie.getInvoiceNet() - customerInvocie.getInvoicePayment();
				String montantRestantS = Utils.convertAmountToStringWithSeperator(montantRestant);
				operation.setOperationAmountS(montantRestantS);
				operation.setValidated(false);
				operation.setOperationAmount(montantRestant);
				operation.setOperationRealId(customerInvocie.getInvoiceId());
				operation.setBeneficiaryName(customerInvocie.getCustomer().getCustomerLabel());
				String label = "REGLEMENT FACTURE CLIENT N° " + customerInvocie.getInvoiceNumber();
				operation.setOpperationLabel(label);
				operations.add(operation);

			});
		}

		if (isProvider ||(!isCustomer && !isProvider)) {
			// Trouver toutes les factures fournisseurs ouvertes
			List<ProviderInvoice> providerInvoices = this.providerInvoiceService
					.findByInvoiceStatusAndInvoiceDeadlineDate(InvoiceStatus.OPENED, startDate, endDate);
			providerInvoices.addAll(this.providerInvoiceService.findByInvoiceStatusAndInvoiceDeadlineDate(InvoiceStatus.OPENED, startDate));
			providerInvoices.stream().forEach(providerInvocie -> {
				OperationTreserorieDto operation = new OperationTreserorieDto();
				if(providerInvocie.getInvoiceDeadlineDate().compareTo(startDate) < 0) {
					operation.setIsInTheSimulatedPeriod(false);
				} else {
					operation.setIsInTheSimulatedPeriod(true);
				}
				operation.setOpperationType(OperationType.DECAISSEMENT);
				operation.setOperationDate(providerInvocie.getInvoiceDeadlineDate());
				double montantRestant = providerInvocie.getInvoiceTotalAmount() - providerInvocie.getInvoicePayment();
				String montantRestantS = Utils.convertAmountToStringWithSeperator(montantRestant);
				operation.setOperationAmountS(montantRestantS);
				operation.setValidated(false);
				operation.setOperationAmount(montantRestant);
				operation.setOperationRealId(providerInvocie.getInvoiceId());
				operation.setBeneficiaryName(providerInvocie.getProvider().getProviderLabel());
				String label = "DECAISSEMENT FACTURE FOURNISSEUR N° " + providerInvocie.getInvoiceNumber();
				operation.setOpperationLabel(label);
				operations.add(operation);

			});
		}

		List<OperationTreserorieDto> sortedOperations = operations.stream()
				.sorted(Comparator.comparing(OperationTreserorieDto::getOperationDate)).collect(Collectors.toList());

		return sortedOperations;
	}

	public List<OperationTreserorieDto> globalSupervision(Date startDate, Date endDate, Boolean isValidated) {
		List<OperationTreserorieDto> operations = new ArrayList<>();
		List<BankAccount> bankAccounts = bankAccountService.getAllBankAccounts();
		for (BankAccount bankAccount : bankAccounts) {
			operations
					.addAll(this.globalSupervisionEngage(bankAccount.getAccountId(), startDate, endDate, isValidated));
		}

		operations.addAll(this.nonEngageSupervision(startDate, endDate, true, true));
		operations = operations.stream().sorted(Comparator.comparing(OperationTreserorieDto::getOperationDate))
				.collect(Collectors.toList());

		// Trouver le solde progressive

		// Etape 1: Trouver la somme de montant dans tout les comptes bancaire
		// (initial amount)
		double initialAmount = bankAccounts.stream().mapToDouble(account -> account.getAccountInitialAmount()).sum();

		// Etape2: trouver le solde progressive
		for (int i = 0; i < operations.size(); i++) {

			if (i == 0) {
				if (!isValidated) {
					if (!operations.get(i).isValidated()) {
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
				if (!isValidated) {
					if (!operations.get(i).isValidated()) {
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

//


	public List<TurnoverDto> findTurnover(Date startDate, Date endDate) {

		LocalDate localDate=LocalDate.of(Calendar.getInstance().get(Calendar.YEAR),1,1);
		startDate=java.util.Date.from(localDate.atStartOfDay()
				.atZone(ZoneId.systemDefault())
				.toInstant());
		endDate=Calendar.getInstance().getTime();
		int endMonth=Calendar.getInstance().get(Calendar.MONTH)+1;
		List<TurnoverDto> turnoverDtoList = new ArrayList<>();
		List<Facture> factures = this.factureRepository.findByFactureDateBetweenOrderByFactureDate(startDate, endDate);
		Double previousTurnover = 0D;
		BigDecimal bd=null;
		for (int i = 1; i <= endMonth; i++) {
			TurnoverDto turnoverDto = new TurnoverDto();
			int currentMonth = i;
			turnoverDto.setHeading(Utils.getMonthName(currentMonth) + " " + Utils.getYearFromDate(startDate));

			List<Facture> facturesCurrentMonth = factures.stream().filter(f -> Utils.getMonth(f.getFactureDate()) == currentMonth).collect(Collectors.toList());
			Double sommeTurnover = 0D;
			for (Facture fa : facturesCurrentMonth) {
				sommeTurnover = sommeTurnover + fa.getTotalHT();
			}
			turnoverDto.setHeading(Utils.getMonthName(currentMonth) + " " + Utils.getYearFromDate(startDate));
			bd = new BigDecimal(sommeTurnover).setScale(3, RoundingMode.HALF_UP);
			turnoverDto.setTurnover(bd.doubleValue());
			turnoverDto.setTurnoverS(Utils.convertAmountToStringWithSeperator(bd.doubleValue()));
			if (previousTurnover == 0D) {
				turnoverDto.setEvolution(0D);
				turnoverDto.setEvolutionS(Utils.convertAmountToStringWithSeperator(0D));
			} else {
				double evolution=((turnoverDto.getTurnover() - previousTurnover) / previousTurnover)*100;
				bd = new BigDecimal(evolution).setScale(3, RoundingMode.HALF_UP);
				turnoverDto.setEvolution(bd.doubleValue());
				turnoverDto.setEvolutionS(Utils.convertAmountToStringWithSeperator(bd.doubleValue()));
			}
			previousTurnover = turnoverDto.getTurnover();
			turnoverDtoList.add(turnoverDto);
		}

		Double totalTurnover = 0D;
		Double totalEvolution = 0D;
		TurnoverDto turnoverDtoTotal = new TurnoverDto();
		turnoverDtoTotal.setHeading("Total");
		for (TurnoverDto turnoverDto : turnoverDtoList) {
			totalTurnover = totalTurnover + turnoverDto.getTurnover();
			totalEvolution = totalEvolution + turnoverDto.getEvolution();
		}
		bd = new BigDecimal(totalEvolution).setScale(3, RoundingMode.HALF_UP);
		turnoverDtoTotal.setEvolution(0d);
		turnoverDtoTotal.setEvolutionS("---");
		bd = new BigDecimal(totalTurnover).setScale(3, RoundingMode.HALF_UP);
		turnoverDtoTotal.setTurnover(bd.doubleValue());
		turnoverDtoTotal.setTurnoverS(Utils.convertAmountToStringWithSeperator(bd.doubleValue()));
		turnoverDtoList.add(turnoverDtoTotal);
		return turnoverDtoList;
	}
	public List<StatusCashDto> statusCash(Date startDate, Date endDate, Boolean isNotEngaged) {
		List<OperationTreserorieDto> operationTreserorieDtosEngaged = new ArrayList<OperationTreserorieDto>();
		List<OperationTreserorieDto> operationTreserorieDtosNotEngaged = new ArrayList<OperationTreserorieDto>();
		List<OperationTreserorieDto> operations = new ArrayList<OperationTreserorieDto>();
		Utils.getFirstDayMonthPrevious(startDate);
		Utils.getLastDayMonthPrevious(startDate);
		List<BankAccount> bankAccountList = this.bankAccountService.getAllBankAccounts();
		bankAccountList.forEach(bankAccount -> {
			operationTreserorieDtosEngaged.addAll(this.globalSupervisionEngage(bankAccount.getAccountId(), startDate, endDate, false));
		});
		if (isNotEngaged) {
			operationTreserorieDtosNotEngaged.addAll(this.nonEngageSupervision(startDate, endDate, true, true));
		}
		operations.addAll(operationTreserorieDtosEngaged);
		operations.addAll(operationTreserorieDtosNotEngaged);
		Comparator<OperationTreserorieDto> c = (s1, s2) -> s1.getOperationDate().compareTo(s2.getOperationDate());
		operationTreserorieDtosEngaged.sort(c);
		operationTreserorieDtosNotEngaged.sort(c);
		operations.sort(c);
		Integer previousMonth = null;
		Integer previousYear = null;
		Double soldeMM = this.getInitialSoldeMM(startDate,bankAccountList);
		List<OperationTreserorieDto> operationsList = this.eliminateOperationsJustBefore(startDate, operations);
		List<StatusCashDto> statusCashDtoList = new ArrayList<StatusCashDto>();
		System.out.println("-----list size----:"+operationsList.size());
		for (OperationTreserorieDto op : operationsList)
		{
			Integer currentMonth = Utils.getMonthFromDate(op.getOperationDate());
			Integer currentYear = Utils.getYearFromDate(op.getOperationDate());
			if((previousMonth==null && previousYear== null ) || (currentMonth!=null && currentYear!=null && ((previousMonth!=currentMonth )||( !previousYear.equals(currentYear) ))))
			{
				System.out.println("----new Month------"+currentMonth);
				StatusCashDto statusCashDto=new StatusCashDto();
				List<OperationTreserorieDto> operationsEngagedInTheSameMonthAndYear=this.getOperationsInSameMonthAndYear(currentMonth,currentYear,operationTreserorieDtosEngaged);
				List<OperationTreserorieDto> encaissementsEngagedInTheSameMonthAndYear=operationsEngagedInTheSameMonthAndYear.stream().filter(e->e.getOpperationType()==OperationType.ENCAISSEMENT).collect(Collectors.toList());
				List<OperationTreserorieDto> decaissementsEngagedInTheSameMonthAndYear=operationsEngagedInTheSameMonthAndYear.stream().filter(e->e.getOpperationType()==OperationType.DECAISSEMENT).collect(Collectors.toList());
				List<OperationTreserorieDto> operationsNotEngagedInTheSameMonthAndYear=this.getOperationsInSameMonthAndYear(currentMonth,currentYear,operationTreserorieDtosNotEngaged);
				List<OperationTreserorieDto> encaissementsNotEngagedInTheSameMonthAndYear=operationsNotEngagedInTheSameMonthAndYear.stream().filter(e->e.getOpperationType()==OperationType.ENCAISSEMENT).collect(Collectors.toList());
				List<OperationTreserorieDto> decaissementsNotEngagedInTheSameMonthAndYear=operationsNotEngagedInTheSameMonthAndYear.stream().filter(e->e.getOpperationType()==OperationType.DECAISSEMENT).collect(Collectors.toList());
				Double soldeEncaissementEngaged=this.getSumOperationsAmount(encaissementsEngagedInTheSameMonthAndYear);
				Double soldeDecaissementEngaged=this.getSumOperationsAmount(decaissementsEngagedInTheSameMonthAndYear);
				Double soldeEncaissementNotEngaged=this.getSumOperationsAmount(encaissementsNotEngagedInTheSameMonthAndYear);
				Double soldeDecaissementNotEngaged=this.getSumOperationsAmount(decaissementsNotEngagedInTheSameMonthAndYear);
				statusCashDto.setHeading(Utils.getMonthName(currentMonth)+" "+currentYear);
				statusCashDto.setCashBalanceMM(soldeMM);
				statusCashDto.setCashBalanceMMS(Utils.convertAmountToStringWithSeperator(statusCashDto.getCashBalanceMM()));
				statusCashDto.setEncaissementEngaged(soldeEncaissementEngaged);
				statusCashDto.setEncaissementEngagedS(Utils.convertAmountToStringWithSeperator(statusCashDto.getEncaissementEngaged()));
				statusCashDto.setDecaissementEngaged(soldeDecaissementEngaged);
				statusCashDto.setDecaissementEngagedS(Utils.convertAmountToStringWithSeperator(statusCashDto.getDecaissementEngaged()));
				statusCashDto.setCommittedCash(soldeEncaissementEngaged-soldeDecaissementEngaged);
				statusCashDto.setCommittedCashS(Utils.convertAmountToStringWithSeperator(statusCashDto.getCommittedCash()));
				statusCashDto.setCashBalanceM(statusCashDto.getCashBalanceMM()+statusCashDto.getCommittedCash());
				statusCashDto.setCashBalanceMS(Utils.convertAmountToStringWithSeperator(statusCashDto.getCashBalanceM()));
				statusCashDto.setEncaissementNotEngaged(soldeEncaissementNotEngaged);
				statusCashDto.setEncaissementNotEngagedS(Utils.convertAmountToStringWithSeperator(statusCashDto.getEncaissementNotEngaged()));
				statusCashDto.setDecaissementNotEngaged(soldeDecaissementNotEngaged);
				statusCashDto.setDecaissementNotEngagedS(Utils.convertAmountToStringWithSeperator(statusCashDto.getDecaissementNotEngaged()));

				statusCashDto.setCashBalanceNotEngagedM(soldeEncaissementNotEngaged-soldeDecaissementNotEngaged);
				statusCashDto.setCashBalanceNotEngagedMS(Utils.convertAmountToStringWithSeperator(statusCashDto.getCashBalanceNotEngagedM()));
				statusCashDto.setCumulativeNetCash(statusCashDto.getCashBalanceM()+statusCashDto.getCashBalanceNotEngagedM());
				statusCashDto.setCumulativeNetCashS(Utils.convertAmountToStringWithSeperator(statusCashDto.getCumulativeNetCash()));
				soldeMM=statusCashDto.getCumulativeNetCash();
				statusCashDtoList.add(statusCashDto);
				previousMonth=currentMonth;
				previousYear=currentYear;
			}


		}
		if(statusCashDtoList.size()>0) {
			statusCashDtoList.add(this.getStatusCashDtoTotal(statusCashDtoList));
		}
		return statusCashDtoList;
	}

/*
    public List<StatusCashDto> statusCashS(Date startDate, Date endDate, Boolean isNotEngaged) throws ParseException {
        List<OperationTreserorieDto> operationsEncaissementEngaged = new ArrayList<OperationTreserorieDto>();
        List<OperationTreserorieDto> operationsDecaissementEngaged = new ArrayList<OperationTreserorieDto>();
        List<OperationTreserorieDto> operationsEncaissementNotEngaged = new ArrayList<OperationTreserorieDto>();
        List<OperationTreserorieDto> operationsDecaissementNotEngaged = new ArrayList<OperationTreserorieDto>();
        List<OperationTreserorieDto> operationsEngaged = new ArrayList<OperationTreserorieDto>();
        List<OperationTreserorieDto> operationsNotEngaged = new ArrayList<OperationTreserorieDto>();

        List<BankAccount> bankAccountList = this.bankAccountService.getAllBankAccounts();
        bankAccountList.forEach(bankAccount -> {
            operationsEngaged.addAll(this.globalSupervisionEngage(bankAccount.getAccountId(), startDate, endDate, false));

        });
        if (isNotEngaged == true) {
            operationsNotEngaged.addAll(this.nonEngageSupervision(startDate, endDate, true, true));
        }
        Comparator<OperationTreserorieDto> c = (s1, s2) -> s1.getOperationDate().compareTo(s2.getOperationDate());
        operationsEngaged.forEach(operationTreserorieDto -> {
            if (operationTreserorieDto.getOpperationType() == OperationType.ENCAISSEMENT) {
                operationsEncaissementEngaged.add(operationTreserorieDto);
            } else {
                operationsDecaissementEngaged.add(operationTreserorieDto);
            }

        });

        operationsEngaged.forEach(operationTreserorieDto -> {
            if (operationTreserorieDto.getOpperationType() == OperationType.ENCAISSEMENT) {
                operationsEncaissementNotEngaged.add(operationTreserorieDto);
            } else {
                operationsDecaissementNotEngaged.add(operationTreserorieDto);
            }

        });
        operationsEncaissementEngaged.sort(c);
        operationsDecaissementEngaged.sort(c);
        operationsEncaissementNotEngaged.sort(c);
        operationsDecaissementNotEngaged.sort(c);

        List<StatusCashDto> statusCashDtoList = new ArrayList<StatusCashDto>();
        Double soldeMM = 0D;
        soldeMM = this.getInitialSoldeMM( startDate, bankAccountList, isNotEngaged);

        for (int i = 1; i <= 12; i++) {
            StatusCashDto statusCashDto = new StatusCashDto();
            int currentMonth = i;
            double sommeEncaissementEngage = 0D;
            double sommeDecaissementEngage = 0D;
            List<OperationTreserorieDto> operationsEncaissementEngagedCurrentMonth = operationsEncaissementEngaged.stream().filter(op -> Utils.getMonthFromDate(op.getOperationDate()) == currentMonth).collect(Collectors.toList());
            List<OperationTreserorieDto> operationsDecaissementEngagedCurrentMonth = operationsDecaissementEngaged.stream().filter(op -> Utils.getMonthFromDate(op.getOperationDate()) == currentMonth).collect(Collectors.toList());

            for (OperationTreserorieDto op : operationsEncaissementEngagedCurrentMonth) {
                sommeEncaissementEngage = sommeEncaissementEngage + op.getOperationAmount();
            }
            for (OperationTreserorieDto op : operationsDecaissementEngagedCurrentMonth) {
                sommeDecaissementEngage = sommeDecaissementEngage + op.getOperationAmount();
            }
            double sommeEncaissementNotEngaged = 0D;
            double sommeDecaissementNotEngaged = 0D;
            if (isNotEngaged) {
                List<OperationTreserorieDto> operationsEncaissementNotEngagedCurrentMonth = operationsEncaissementEngaged.stream().filter(op -> Utils.getMonthFromDate(op.getOperationDate()) == currentMonth).collect(Collectors.toList());
                List<OperationTreserorieDto> operationsDecaissementNotEngagedCurrentMonth = operationsDecaissementEngaged.stream().filter(op -> Utils.getMonthFromDate(op.getOperationDate()) == currentMonth).collect(Collectors.toList());
                for (OperationTreserorieDto op : operationsEncaissementNotEngagedCurrentMonth) {
                    sommeEncaissementNotEngaged = sommeEncaissementNotEngaged + op.getOperationAmount();
                }
                for (OperationTreserorieDto op : operationsDecaissementNotEngagedCurrentMonth) {
                    sommeDecaissementNotEngaged = sommeDecaissementNotEngaged + op.getOperationAmount();
                }

            }
            statusCashDto.setHeading(Utils.getMonthName(currentMonth) + " " + Utils.getYearFromDate(startDate));
            statusCashDto.setCashBalanceMM(soldeMM);
            statusCashDto.setEncaissementEngaged(sommeEncaissementEngage);
            statusCashDto.setDecaissementEngaged(sommeDecaissementEngage);
            statusCashDto.setCommittedCash(sommeEncaissementEngage - sommeDecaissementEngage);
            statusCashDto.setCashBalanceM(statusCashDto.getCashBalanceMM() + statusCashDto.getCommittedCash());
            statusCashDto.setEncaissementNotEngaged(sommeEncaissementNotEngaged);
            statusCashDto.setDecaissementNotEngaged(sommeDecaissementNotEngaged);
            statusCashDto.setCashBalanceNotEngagedM(sommeEncaissementNotEngaged - sommeDecaissementNotEngaged);
            statusCashDto.setCumulativeNetCash(statusCashDto.getCashBalanceM() + statusCashDto.getCashBalanceNotEngagedM());
            soldeMM = statusCashDto.getCumulativeNetCash();
            statusCashDtoList.add(statusCashDto);
        }
        StatusCashDto statusCashDtoTotal = new StatusCashDto();
        statusCashDtoTotal.setHeading("Total");
        Double cashBalanceMMTotal = 0D;
        Double encaissementEngagedTotal = 0D;
        Double decaissementEngagedTotal = 0D;
        Double committedCashTotal = 0D;
        Double cashBalanceMTotal = 0D;
        Double encaissementNotEngagedTotal = 0D;
        Double decaissementNotEngagedTotal = 0D;
        Double cashBalanceNotEngagedMTotal = 0D;
        Double cumulativeNetCashTotal = 0D;
        for (StatusCashDto statusCashDto : statusCashDtoList) {
            cashBalanceMMTotal = cashBalanceMMTotal + statusCashDto.getCashBalanceMM();
            encaissementEngagedTotal = encaissementEngagedTotal + statusCashDto.getEncaissementEngaged();
            decaissementEngagedTotal = decaissementEngagedTotal + statusCashDto.getDecaissementEngaged();
            committedCashTotal = committedCashTotal + statusCashDto.getCommittedCash();
            cashBalanceMTotal = cashBalanceMTotal + statusCashDto.getCashBalanceM();
            encaissementNotEngagedTotal = encaissementNotEngagedTotal + statusCashDto.getEncaissementNotEngaged();
            decaissementNotEngagedTotal = decaissementNotEngagedTotal + statusCashDto.getDecaissementNotEngaged();
            cashBalanceNotEngagedMTotal = cashBalanceNotEngagedMTotal + statusCashDto.getCashBalanceNotEngagedM();
            cumulativeNetCashTotal = cumulativeNetCashTotal + statusCashDto.getCumulativeNetCash();
        }
        statusCashDtoTotal.setCashBalanceMM(cashBalanceMMTotal);
        statusCashDtoTotal.setEncaissementEngaged(encaissementEngagedTotal);
        statusCashDtoTotal.setDecaissementEngaged(decaissementEngagedTotal);
        statusCashDtoTotal.setCommittedCash(committedCashTotal);
        statusCashDtoTotal.setCashBalanceM(cashBalanceMTotal);
        statusCashDtoTotal.setEncaissementNotEngaged(encaissementNotEngagedTotal);
        statusCashDtoTotal.setDecaissementNotEngaged(decaissementNotEngagedTotal);
        statusCashDtoTotal.setCashBalanceNotEngagedM(cashBalanceNotEngagedMTotal);
        statusCashDtoTotal.setCumulativeNetCash(cumulativeNetCashTotal);
        statusCashDtoList.add(statusCashDtoTotal);
        return statusCashDtoList;

    }
*/

	public StatusCashDto getStatusCashDtoTotal(List<StatusCashDto> statusCashDtos) {
		StatusCashDto statusCashDtoTotal = new StatusCashDto();
		statusCashDtoTotal.setHeading("Total");
		Double cashBalanceMMTotal = 0D;
		Double encaissementEngagedTotal = 0D;
		Double decaissementEngagedTotal = 0D;
		Double committedCashTotal = 0D;
		Double cashBalanceMTotal = 0D;
		Double encaissementNotEngagedTotal = 0D;
		Double decaissementNotEngagedTotal = 0D;
		Double cashBalanceNotEngagedMTotal = 0D;
		Double cumulativeNetCashTotal = 0D;
		for (StatusCashDto statusCashDto : statusCashDtos) {
			cashBalanceMMTotal = cashBalanceMMTotal + statusCashDto.getCashBalanceMM();
			encaissementEngagedTotal = encaissementEngagedTotal + statusCashDto.getEncaissementEngaged();
			decaissementEngagedTotal = decaissementEngagedTotal + statusCashDto.getDecaissementEngaged();
			committedCashTotal = committedCashTotal + statusCashDto.getCommittedCash();
			cashBalanceMTotal = cashBalanceMTotal + statusCashDto.getCashBalanceM();
			encaissementNotEngagedTotal = encaissementNotEngagedTotal + statusCashDto.getEncaissementNotEngaged();
			decaissementNotEngagedTotal = decaissementNotEngagedTotal + statusCashDto.getDecaissementNotEngaged();
			cashBalanceNotEngagedMTotal = cashBalanceNotEngagedMTotal + statusCashDto.getCashBalanceNotEngagedM();
			cumulativeNetCashTotal = cumulativeNetCashTotal + statusCashDto.getCumulativeNetCash();
		}
		statusCashDtoTotal.setCashBalanceMM(cashBalanceMMTotal);
		statusCashDtoTotal.setCashBalanceMMS(Utils.convertAmountToStringWithSeperator(cashBalanceMMTotal));

		statusCashDtoTotal.setEncaissementEngaged(encaissementEngagedTotal);
		statusCashDtoTotal.setEncaissementEngagedS(Utils.convertAmountToStringWithSeperator(encaissementEngagedTotal));

		statusCashDtoTotal.setDecaissementEngaged(decaissementEngagedTotal);
		statusCashDtoTotal.setDecaissementEngagedS(Utils.convertAmountToStringWithSeperator(decaissementEngagedTotal));

		statusCashDtoTotal.setCommittedCash(committedCashTotal);
		statusCashDtoTotal.setCommittedCashS(Utils.convertAmountToStringWithSeperator(committedCashTotal));

		statusCashDtoTotal.setCashBalanceM(cashBalanceMTotal);
		statusCashDtoTotal.setCashBalanceMS(Utils.convertAmountToStringWithSeperator(cashBalanceMTotal));

		statusCashDtoTotal.setEncaissementNotEngaged(encaissementNotEngagedTotal);
		statusCashDtoTotal.setEncaissementNotEngagedS(Utils.convertAmountToStringWithSeperator(encaissementNotEngagedTotal));

		statusCashDtoTotal.setDecaissementNotEngaged(decaissementNotEngagedTotal);
		statusCashDtoTotal.setDecaissementNotEngagedS(Utils.convertAmountToStringWithSeperator(decaissementNotEngagedTotal));

		statusCashDtoTotal.setCashBalanceNotEngagedM(cashBalanceNotEngagedMTotal);
		statusCashDtoTotal.setCashBalanceNotEngagedMS(Utils.convertAmountToStringWithSeperator(cashBalanceNotEngagedMTotal));

		statusCashDtoTotal.setCumulativeNetCash(cumulativeNetCashTotal);
		statusCashDtoTotal.setCumulativeNetCashS(Utils.convertAmountToStringWithSeperator(cumulativeNetCashTotal));

		return statusCashDtoTotal;
	}

	public Double getInitialSoldeMM( Date startDate, List<BankAccount> bankAccountList)  {

		Double sommeAccount = 0D;
		for (BankAccount bankAccount : bankAccountList) {
			HistoricAccountSold historicAccountSold = historicAccountSoldService.findFirstByBankAccountAndCreatedAtLessThanEqualOrderByCreatedAtDesc(bankAccount, startDate);
			if (historicAccountSold != null)
				sommeAccount = sommeAccount + historicAccountSold.getSolde();
		}
		Date startDateYear = Utils.getFirstDayMonthPrevious(startDate);
		Date endDateYear = Utils.getLastDayMonthPrevious(startDate);
		List<OperationTreserorieDto> operationsEngaged = new ArrayList<OperationTreserorieDto>();
		List<OperationTreserorieDto> operationsNotEngaged = new ArrayList<OperationTreserorieDto>();
		bankAccountList.forEach(bankAccount -> {
			operationsEngaged.addAll(this.globalSupervisionEngage(bankAccount.getAccountId(), startDateYear, endDateYear, false));

		});
		operationsNotEngaged.addAll(this.nonEngageSupervision(startDateYear, endDateYear, true, true));

		Comparator<OperationTreserorieDto> c = (s1, s2) -> s1.getOperationDate().compareTo(s2.getOperationDate());
		operationsEngaged.sort(c);
		operationsNotEngaged.sort(c);
		List<OperationTreserorieDto>operationsEngagedFilter=this.eliminateOperationsJustBefore(startDate, operationsEngaged);
		List<OperationTreserorieDto>operationsNotEngagedFilter=this.eliminateOperationsJustBefore(startDate, operationsNotEngaged);
		List<OperationTreserorieDto> encaissementsEngaged=operationsEngagedFilter.stream().filter(e->e.getOpperationType()==OperationType.ENCAISSEMENT).collect(Collectors.toList());
		List<OperationTreserorieDto> decaissementsEngaged=operationsEngagedFilter.stream().filter(e->e.getOpperationType()==OperationType.DECAISSEMENT).collect(Collectors.toList());
		List<OperationTreserorieDto> encaissementsNotEngaged=operationsNotEngagedFilter.stream().filter(e->e.getOpperationType()==OperationType.ENCAISSEMENT).collect(Collectors.toList());
		List<OperationTreserorieDto> decaissementsNotEngaged=operationsNotEngagedFilter.stream().filter(e->e.getOpperationType()==OperationType.DECAISSEMENT).collect(Collectors.toList());
		Double soldeEncaissementEngaged=this.getSumOperationsAmount(encaissementsEngaged);
		Double soldeDecaissementEngaged=this.getSumOperationsAmount(decaissementsEngaged);
		Double soldeEncaissementNotEngaged=this.getSumOperationsAmount(encaissementsNotEngaged);
		Double soldeDecaissementNotEngaged=this.getSumOperationsAmount(decaissementsNotEngaged);
		Double initialAmount = sommeAccount + soldeEncaissementEngaged + soldeEncaissementNotEngaged - soldeDecaissementEngaged - soldeDecaissementNotEngaged;
		return initialAmount;
	}


	public Double getSumOperationsAmount(List<OperationTreserorieDto> operationTreserorieDtoList) {
		Double sumOperationsAmount = 0D;
		for (OperationTreserorieDto op : operationTreserorieDtoList) {
			sumOperationsAmount = sumOperationsAmount + op.getOperationAmount();
		}
		return sumOperationsAmount;
	}

	public List<OperationTreserorieDto> getOperationsInSameMonthAndYear(Integer month, Integer year, List<OperationTreserorieDto> operationTreserorieDtoList) {
		return operationTreserorieDtoList.stream().filter(op -> (Utils.getMonthFromDate(op.getOperationDate()).equals(month) && Utils.getYearFromDate(op.getOperationDate()).equals(year))).collect(Collectors.toList());
	}

	public List<OperationTreserorieDto> eliminateOperationsJustBefore(Date startDate, List<OperationTreserorieDto> operationTreserorieDtoList) {
		return operationTreserorieDtoList.stream().filter(op ->op.getOperationDate().compareTo(startDate)>=0).collect(Collectors.toList());
	}

	public List<CustomerSaleDto> getCustomersSales() {
		LocalDate localDate=LocalDate.of(Calendar.getInstance().get(Calendar.YEAR),1,1);
		Date startDate=java.util.Date.from(localDate.atStartOfDay()
				.atZone(ZoneId.systemDefault())
				.toInstant());
		Date endDate=Calendar.getInstance().getTime();
		int endMonth=Calendar.getInstance().get(Calendar.MONTH)+1;
		List<CustomerSaleDto> customerSales = new ArrayList<>();
		for (Customer customer : customerRepository.findAllByOrderByCustomerLabel()) {
			CustomerSaleDto customerSale = new CustomerSaleDto();
			customerSale.setCustomerLabel(customer.getCustomerLabel());
			List<Facture> factures = this.factureRepository.findByCustomerAndFactureDateBetweenOrderByFactureDate(customer,startDate, endDate);
			BigDecimal bd = null;
			for (int i = 1; i <= endMonth; i++) {
				CustomerMonthSaleDto monthSale = new CustomerMonthSaleDto();
				int currentMonth = i;
				monthSale.setHeading(Utils.getMonthName(currentMonth) + " " + Utils.getYearFromDate(startDate));

				List<Facture> facturesCurrentMonth = factures.stream().filter(f -> Utils.getMonth(f.getFactureDate()) == currentMonth).collect(Collectors.toList());
				Double sommeValue = 0D;
				for (Facture fa : facturesCurrentMonth) {
					sommeValue = sommeValue + fa.getTotalHT();
				}
				monthSale.setHeading(Utils.getMonthName(currentMonth) + " " + Utils.getYearFromDate(startDate));
				bd = new BigDecimal(sommeValue).setScale(3, RoundingMode.HALF_UP);
				monthSale.setValue(bd.doubleValue());
				monthSale.setValueS(Utils.convertAmountToStringWithSeperator(bd.doubleValue()));
				customerSale.setValueTotal(customerSale.getValueTotal()+monthSale.getValue());
				customerSale.getMonthSales().add(monthSale);
			}
			bd = new BigDecimal(customerSale.getValueTotal()).setScale(3, RoundingMode.HALF_UP);
			customerSale.setValueTotal(bd.doubleValue());
			customerSale.setValueTotalS(Utils.convertAmountToStringWithSeperator(customerSale.getValueTotal()));
			customerSales.add(customerSale);
		}
		return customerSales;
	}


	public List<ProductSaleDto> getProductsSales() {
		LocalDate localDate=LocalDate.of(Calendar.getInstance().get(Calendar.YEAR),1,1);
		Date startDate=java.util.Date.from(localDate.atStartOfDay()
				.atZone(ZoneId.systemDefault())
				.toInstant());
		Date endDate=Calendar.getInstance().getTime();
		int endMonth=Calendar.getInstance().get(Calendar.MONTH)+1;
		List<ProductSaleDto> productSales = new ArrayList<>();
		List<ProductGroup> productGroups=productGroupRepository.findAll();
		for (ProductGroup productGroup : productGroups) {
			for (Product product : productGroup.getProductList()) {
				ProductSaleDto productSale = new ProductSaleDto();
				productSale.setProductGroupLabel(productGroup.getProductGroupLabel());
				productSale.setProductLabel(product.getProductReference());
				List<Facture> factures = this.factureRepository.findByFactureDateBetweenOrderByFactureDate(startDate, endDate);
				List<FactureLine> factureLines=this.factureLineRepository.findByFactureInAndProductAndProductGroup(factures,product,productGroup);
				BigDecimal bd = null;
				for (int i = 1; i <= endMonth; i++) {
					ProductMonthSaleDto monthSale = new ProductMonthSaleDto();
					int currentMonth = i;
					monthSale.setHeading(Utils.getMonthName(currentMonth) + " " + Utils.getYearFromDate(startDate));

					List<FactureLine> facturesCurrentMonth = factureLines.stream().filter(fl -> Utils.getMonth(fl.getFacture().getFactureDate()) == currentMonth).collect(Collectors.toList());
					Double sommeValue = 0D;
					Double quantite=0D;
					for (FactureLine fl : facturesCurrentMonth) {
						sommeValue = sommeValue + fl.getMontantHt();
						quantite+=fl.getQuantity();
					}
					monthSale.setHeading(Utils.getMonthName(currentMonth) + " " + Utils.getYearFromDate(startDate));
					bd = new BigDecimal(sommeValue).setScale(3, RoundingMode.HALF_UP);
					monthSale.setValue(bd.doubleValue());
					productSale.setValueTotal(productSale.getValueTotal()+monthSale.getValue());
					monthSale.setValueS(Utils.convertAmountToStringWithSeperator(bd.doubleValue()));
					monthSale.setQuantity(quantite);
					productSale.getMonthSales().add(monthSale);
				}
				bd = new BigDecimal(productSale.getValueTotal()).setScale(3, RoundingMode.HALF_UP);
				productSale.setValueTotal(bd.doubleValue());
				productSale.setValueTotalS(Utils.convertAmountToStringWithSeperator(productSale.getValueTotal()));
				productSales.add(productSale);
			}
		}
		return productSales;
	}


	public List<CustomerProductSaleDto> getCustomersSalesByProduct(Date startDate,Date endDate) {
		List<CustomerProductSaleDto> customerProductSales = new ArrayList<>();
		for (Customer customer : customerRepository.findAllByOrderByCustomerLabel()) {
			CustomerProductSaleDto customerProductSale = new CustomerProductSaleDto();
			customerProductSale.setCustomerLabel(customer.getCustomerLabel());
			List<Facture> factures = this.factureRepository.findByCustomerAndFactureDateBetweenOrderByFactureDate(customer,startDate, endDate);
			List<Product> products=productRepository.findAllByOrderByProductLabelAsc();
				for (Product product : products) {
                    ProductCustomerDto productCustomer=new ProductCustomerDto();
                    productCustomer.setProductLabel(product.getProductReference());
					List<FactureLine> factureLines = this.factureLineRepository.findByFactureInAndProduct(factures, product);
				    double somme=0D;
					for (FactureLine factureLine : factureLines) {
						somme+=factureLine.getMontantHt();
					}
					BigDecimal bd = new BigDecimal(somme).setScale(3, RoundingMode.HALF_UP);
					productCustomer.setValue(bd.doubleValue());
					productCustomer.setValueS(Utils.convertAmountToStringWithSeperator(productCustomer.getValue()));
					customerProductSale.setValueTotal(customerProductSale.getValueTotal()+bd.doubleValue());
					customerProductSale.getProductsCustomer().add(productCustomer);
			}
			customerProductSale.setValueTotalS(Utils.convertAmountToStringWithSeperator(customerProductSale.getValueTotal()));
			customerProductSales.add(customerProductSale);
		}
		return customerProductSales;
	}



}
