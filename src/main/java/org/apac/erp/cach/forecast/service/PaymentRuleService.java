package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.persistence.repositories.PaymentRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentRuleService {

	@Autowired
	private PaymentRuleRepository paymentRuleRepo;

	public List<PaymentRule> findAllPaymentRules() {
		return paymentRuleRepo.findAll();
	}

}
