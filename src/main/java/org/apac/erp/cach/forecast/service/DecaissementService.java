package org.apac.erp.cach.forecast.service;

import java.util.List;

import  org.apac.erp.cach.forecast.constants.Constants;
import org.apac.erp.cach.forecast.enumeration.InvoiceStatus;
import org.apac.erp.cach.forecast.persistence.entities.Decaissement;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;
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
		if(decaissement.getDecaissementType().getDecaissementType().equals(Constants.DECAISSEMENT_PAIEMENT_FACTURE_FOURNISSEUR)) {
			Invoice invoice = decaissement.getDecaissementInvoice();
			if(invoice != null) {
				invoice.setInvoicePayment(invoice.getInvoicePayment() + decaissement.getDecaissementAmount());
				if(Double.compare(invoice.getInvoicePayment(), invoice.getInvoiceTotalAmount()) == 0) {
					invoice.setInvoiceStatus(InvoiceStatus.CLOSED);
				}
			this.invoiceService.saveInvoice(invoice);
			}
		}
		return decaissementRepo.save(decaissement);
	}
	
	public Decaissement getDecaissementById(Long decaissementId) {
		return decaissementRepo.getOne(decaissementId);
	}
	
	public void deleteDecaissement(Long decaissementId) {
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

	

	


}
