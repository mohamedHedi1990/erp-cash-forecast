package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.TarifBancaire;
import org.apac.erp.cach.forecast.persistence.repositories.TarifBancaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TarifBancaireService {

	@Autowired
	private TarifBancaireRepository tarifBancaireRepo;


	public TarifBancaire saveTarifBancaire(TarifBancaire tarif) {
		return this.tarifBancaireRepo.save(tarif);
	}
	
	public List<TarifBancaire> getAllTarifsBancaires() {
		return this.tarifBancaireRepo.findAll();
	}
	
	public TarifBancaire getTarifById(Long tarifId) {
		return this.tarifBancaireRepo.findOne(tarifId);
	}

	public void deleteTarifBancaire(Long tarifId) {
		 this.tarifBancaireRepo.delete(tarifId);
		
	}
	public List<TarifBancaire>findByTarifAccount(BankAccount account) {
		return this.tarifBancaireRepo.findByTarifAccount(account);
	}
}
