package org.apac.erp.cach.forecast.persistence.entities;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.apac.erp.cach.forecast.constants.Utils;

import lombok.Data;

@Entity
@Table(name = "erp_customer")
@Data
public class Customer extends AuditableSql implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerId;

	private String customerLabel;

	private String customerAddress;

	private String customerUniqueIdentifier;

	private String customerManagerName;

	private String customerTel;

	private String customerEmail;

	private Double customerInitialSold;

	private String customerInitialSoldS;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Contact> customerContacts;

	@PrePersist
	public void initInvoice() {
		if (this.customerInitialSold != null) {
			DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
			simbolos.setGroupingSeparator(',');
			simbolos.setDecimalSeparator('.');
			this.customerInitialSold = Double
					.parseDouble(new DecimalFormat("##.###", simbolos).format(this.customerInitialSold));

			this.customerInitialSoldS = Utils.convertAmountToStringWithSeperator(this.customerInitialSold);
		}

	}

}
