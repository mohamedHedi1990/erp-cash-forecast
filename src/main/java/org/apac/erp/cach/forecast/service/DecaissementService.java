package org.apac.erp.cach.forecast.service;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Decaissement;
import org.apac.erp.cach.forecast.persistence.repositories.DecaissementRepository;
import org.apac.erp.cach.forecast.persistence.repositories.EncaissementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DecaissementService {
	
	@Autowired
	private DecaissementRepository decaissementRepo;

	public List<Decaissement> findAllDecaissements() {
		return decaissementRepo.findAll();
	}

	public List<Decaissement> findAllDecaissementsBetweenTwoDates(Date startDate, Date endDate) {
		return decaissementRepo.findByTransactionDeadlineDateGreaterThanEqualAndTransactionDeadlineDateLessThanEqual(startDate, endDate);
	}

	public Decaissement saveNewDecaissement(Decaissement encDec) {
		return decaissementRepo.save(encDec);
	}

}
