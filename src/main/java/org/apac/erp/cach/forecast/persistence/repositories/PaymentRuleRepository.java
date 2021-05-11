package org.apac.erp.cach.forecast.persistence.repositories;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.enumeration.PaymentMethod;
import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRuleRepository extends JpaRepository<PaymentRule, Long> {
List<PaymentRule> findByPaymentRuleAccountAndPaymentRulePaymentMethodInAndPaymentRuleDeadlineDateBetweenOrderByPaymentRuleDeadlineDateAsc(BankAccount bankAccount,PaymentMethod[] paymentMethods, Date startDate, Date endDate);
List<PaymentRule> findByPaymentRuleAccountAndPaymentRulePaymentMethodInAndPaymentRuleEffetEscompteDateBetweenOrderByPaymentRuleEffetEscompteDateAsc(BankAccount bankAccount,PaymentMethod[] paymentMethods, Date startDate, Date endDate);

List<PaymentRule> findByPaymentRuleAccountAndIsValidatedAndPaymentRulePaymentMethodInAndPaymentRuleDeadlineDateBeforeOrderByPaymentRuleDeadlineDateAsc(BankAccount bankAccount, boolean isvalidated,PaymentMethod[] paymentMethods, Date startDate);
List<PaymentRule> findByPaymentRuleAccountAndIsValidatedAndPaymentRulePaymentMethodInAndPaymentRuleEffetEscompteDateBeforeOrderByPaymentRuleEffetEscompteDateAsc(BankAccount bankAccount, boolean isvalidated,PaymentMethod[] paymentMethods, Date startDate);
    List<PaymentRule> findBypaymentRulePaymentMethodInAndIsValidated(PaymentMethod[] paymentMethods, boolean isValidated);
    List<PaymentRule> findByPaymentRuleDeadlineDateBetweenAndPaymentRulePaymentMethodAndIsValidated(Date startDate,Date endDate,PaymentMethod paymentMethod,boolean isValidated);
    List<PaymentRule> findByPaymentRuleDeadlineDateBetweenAndPaymentRulePaymentMethodInAndIsValidated(Date startDate,Date endDate,PaymentMethod[] paymentMethods,boolean isValidated);


}
