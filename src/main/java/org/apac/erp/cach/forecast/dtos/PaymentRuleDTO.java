package org.apac.erp.cach.forecast.dtos;

import java.util.Date;

import org.apac.erp.cach.forecast.enumeration.PaymentMethod;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRuleDTO {


	private Long paymentRuleId;

	private PaymentMethod paymentRulePaymentMethod;

	private Integer paymentRulePaymentMethodNb;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Africa/Tunis")
	private Date paymentRuleDeadlineDate;

	private boolean isValidated;

	private String customerInvoiceNumber;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date createdAt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	protected Date updatedAt;

}
