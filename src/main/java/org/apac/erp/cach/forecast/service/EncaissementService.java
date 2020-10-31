package org.apac.erp.cach.forecast.service;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.Decaissement;
import org.apac.erp.cach.forecast.persistence.entities.Encaissement;
import org.apac.erp.cach.forecast.persistence.repositories.EncaissementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EncaissementService {

	@Autowired
	private EncaissementRepository encaissementRepo;

	public List<Encaissement> findAllEncaissements() {
		return encaissementRepo.findAll();
	}

	public Encaissement saveEncaissement(Encaissement encaissement) {
		return encaissementRepo.save(encaissement);
	}
	
	public Encaissement getEncaissementById(Long encaissementId) {
		return encaissementRepo.getOne(encaissementId);
	}
	
	public void deleteEncaissement(Long encaissementId) {
		this.encaissementRepo.delete(encaissementId);
	}

	List<Encaissement> findEncaissementsBetwwenTwoDates(BankAccount bankAccount, Date startDate, Date endDate) {
		return this.encaissementRepo.findByEncaissementBankAccountAndEncaissementDeadlineDateBetweenOrderByEncaissementDeadlineDateAsc(bankAccount, startDate, endDate);
	}

	
	public Encaissement validateEncaissement(Long encaissementId) {
		Encaissement encaissement = getEncaissementById(encaissementId);
		if(encaissement != null) {
			encaissement.setIsValidated(true);
			return this.encaissementRepo.save(encaissement);
		}
		return null;
	}
	


}
