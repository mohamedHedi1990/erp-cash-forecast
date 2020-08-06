package org.apac.erp.cach.forecast.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.RapprochementDTO;
import org.apac.erp.cach.forecast.enumeration.EncaissementDecaissementType;
import org.apac.erp.cach.forecast.enumeration.TransactionType;
import org.apac.erp.cach.forecast.persistence.entities.EncaissementDecaissement;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RapprochemntService {
	
	@Autowired
	private PaymentRuleService paymentRuleService;
	
	@Autowired
	private BankAccountService bankAccountService;
	
	@Autowired
	private EncaissementDecaissementService encaissementDecaissementService;

	public List<RapprochementDTO> findRapprochementsByDateIntervalAndBankAccount(Date startDate, Date endDate,
			Long accountId) {
		
		List<EncaissementDecaissementType> decTypes = new ArrayList<EncaissementDecaissementType>();
		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_AUTRE);
		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_BANK_COMMISSION);
		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_INTERESTS);
		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_PLAN_COMPTABLE_TIERS);
		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_SALAIRE);
		
		ArrayList<RapprochementDTO> result = new ArrayList<RapprochementDTO>();
		List<PaymentRule> paymentRules = paymentRuleService.findAllPaymentRulesByDateIntervalAndBankAccount(startDate, endDate, accountId);
		paymentRules.stream().forEach(payRule -> {
			RapprochementDTO dto = new RapprochementDTO();
			dto.setOperationDate(payRule.getPaymentValidationDate());
			dto.setLabel(payRule.getPaymentRulePaymentMethod().toString());
			dto.setDetails(payRule.getPaymentRuleNumber());
			dto.setType(payRule.getPaymentRuleType());	
			dto.setAmount(payRule.getPaymentRuleAmount());
			result.add(dto);
		});
		/***/
		List<EncaissementDecaissement> encDecs = encaissementDecaissementService.findByBankAccountAndEncDecVlaidationDateInterval(
				bankAccountService.findById(accountId), startDate, endDate);
		
		encDecs.stream().forEach(encDec -> {
			RapprochementDTO dto = new RapprochementDTO();
			dto.setAmount(encDec.getEncaissementDecaissementAmount());
			dto.setLabel(encDec.getEncaissementDecaissementLabel());
			dto.setDetails(encDec.getEncaissementDecaissementDetails());
			dto.setOperationDate(encDec.getValidationDate());
			
			
			if (decTypes.contains(encDec.getEncaissementDecaissementType())) {
				dto.setType(TransactionType.DEC);
			} else {
				dto.setType(TransactionType.ENC);
			}
			result.add(dto);
		});
		
		result.sort((o1,o2) -> o1.getOperationDate().compareTo(o2.getOperationDate()));

		
		return result;
	}

}
