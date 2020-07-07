package org.apac.erp.cach.forecast.service;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.EncaissementDecaissement;
import org.apac.erp.cach.forecast.persistence.repositories.EncaissementDecaissementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EncaissementDecaissementService {

	@Autowired
	private EncaissementDecaissementRepository encaissementDecaissementRepo;

	public List<EncaissementDecaissement> findAllEncaissementDecaissements() {
		return encaissementDecaissementRepo.findAll();
	}

	public EncaissementDecaissement saveNewEncaissementDecaissement(EncaissementDecaissement encDec) {
		return encaissementDecaissementRepo.save(encDec);
	}

	public List<EncaissementDecaissement> findAllEncaissementsBetweenTwoDates(Date startDate, Date endDate) {
		// TODO add type
		return encaissementDecaissementRepo
				.findByEncaissementDecaissementDeadlineDateGreaterThanEqualAndEncaissementDecaissementDeadlineDateLessThanEqual(
						startDate, endDate);

	}

	public List<EncaissementDecaissement> findAllDecaissementsBetweenTwoDates(Date startDate, Date endDate) {
		// TODO add type
		return encaissementDecaissementRepo
				.findByEncaissementDecaissementDeadlineDateGreaterThanEqualAndEncaissementDecaissementDeadlineDateLessThanEqual(
						startDate, endDate);
	}

	
}
