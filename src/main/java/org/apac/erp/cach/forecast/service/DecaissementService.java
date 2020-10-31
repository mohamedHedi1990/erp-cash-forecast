package org.apac.erp.cach.forecast.service;

import java.util.Date;
import java.util.List;

import  org.apac.erp.cach.forecast.constants.Constants;
import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.Decaissement;
import org.apac.erp.cach.forecast.persistence.entities.Encaissement;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.persistence.entities.PaymentRule;
import org.apac.erp.cach.forecast.persistence.repositories.DecaissementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class DecaissementService {

	@Autowired
	private DecaissementRepository decaissementRepo;
	
	@Autowired
	private InvoiceService invoiceService;

	public List<Decaissement> findAllDecaissements() {
		return decaissementRepo.findAll();
	}

	public Decaissement saveDecaissement(Decaissement decaissement) {
		if(decaissement.getDecaissementType().getDecaissementTypeValue().equals(Constants.DECAISSEMENT_PAIEMENT_FACTURE_FOURNISSEUR)) {
			Invoice invoice = this.invoiceService.findInvoiceById(decaissement.getDecaissementInvoice().getInvoiceId());
			if(invoice != null) {
				invoice.setInvoicePayment(invoice.getInvoicePayment() + decaissement.getDecaissementAmount());
				if(Double.compare(invoice.getInvoicePayment(), invoice.getInvoiceTotalAmount()) == 0) {
					invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
				} else {
					invoice.setInvoiceStatus(InvoiceStatus.OPENED);
				}
			invoice = this.invoiceService.saveInvoice(invoice);
			decaissement.setDecaissementInvoice(invoice);
			}
		}
		return decaissementRepo.save(decaissement);
	}
	
	public Decaissement modifyDecaissement(Decaissement decaissement) {
		Decaissement oldDecaissement = getDecaissementById(decaissement.getDecaissementId());
		if(oldDecaissement != null) {
			if(oldDecaissement.getDecaissementType().getDecaissementTypeValue().equals(Constants.DECAISSEMENT_PAIEMENT_FACTURE_FOURNISSEUR)) {
				Invoice invoice = oldDecaissement.getDecaissementInvoice();
				if(invoice != null) {
					invoice.setInvoicePayment(invoice.getInvoicePayment() - oldDecaissement.getDecaissementAmount());
					invoice.setInvoicePayment(invoice.getInvoicePayment() + decaissement.getDecaissementAmount());
					if(Double.compare(invoice.getInvoicePayment(), invoice.getInvoiceTotalAmount()) == 0) {
						invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
					} else {
						invoice.setInvoiceStatus(InvoiceStatus.OPENED);
					}
				this.invoiceService.saveInvoice(invoice);
				}
			}
		}
		
		return decaissementRepo.save(decaissement);
	}
	
	public Decaissement getDecaissementById(Long decaissementId) {
		return decaissementRepo.getOne(decaissementId);
	}
	
	public void deleteDecaissement(Long decaissementId) {
		Decaissement decaissement = getDecaissementById(decaissementId);
		if(decaissement.getDecaissementType().getDecaissementTypeValue().equals(Constants.DECAISSEMENT_PAIEMENT_FACTURE_FOURNISSEUR)) {
			Invoice invoice = decaissement.getDecaissementInvoice();
			if(invoice != null) {
				invoice.setInvoicePayment(invoice.getInvoicePayment() - decaissement.getDecaissementAmount());
				if(Double.compare(invoice.getInvoicePayment(), invoice.getInvoiceTotalAmount()) == 0) {
					invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
				} else {
					invoice.setInvoiceStatus(InvoiceStatus.OPENED);
				}
			this.invoiceService.saveInvoice(invoice);
			}
		}
		this.decaissementRepo.delete(decaissementId);
	}

	/*
	public List<EncaissementDecaissement> findAllEncaissementsBetweenTwoDates(Date startDate, Date endDate) {
		List<EncaissementDecaissement> encDecs = encaissementDecaissementRepo
				.findByEncaissementDecaissementDeadlineDateGreaterThanEqualAndEncaissementDecaissementDeadlineDateLessThanEqual(
						startDate, endDate);
		
		List<EncaissementDecaissementType> encTypes = new ArrayList<EncaissementDecaissementType>();
		encTypes.add(EncaissementDecaissementType.ENCAISSEMENT_AUTRE);
		encTypes.add(EncaissementDecaissementType.ENCAISSEMENT_PLAN_COMPTABLE_TIERS);
		encTypes.add(EncaissementDecaissementType.ENCAISSEMENT_FACTURE_CLIENT);

		return encDecs.stream().filter(encDec -> encTypes.contains(encDec.getEncaissementDecaissementType()))
				.collect(Collectors.toList());


	} */

	List<Decaissement> findDecaissementsBetwwenTwoDates(BankAccount bankAccount, Date startDate, Date endDate) {
		return this.decaissementRepo.findByDecaissementBankAccountAndDecaissementDeadlineDateBetweenOrderByDecaissementDeadlineDateAsc(bankAccount, startDate, endDate);
	}

	public Decaissement validateDecaissement(Long decaissementId) {
		Decaissement decaissement = getDecaissementById(decaissementId);
		if(decaissement != null) {
			decaissement.setIsValidated(true);
			return this.decaissementRepo.save(decaissement);
		}
		return null;
	}
	

	


}
