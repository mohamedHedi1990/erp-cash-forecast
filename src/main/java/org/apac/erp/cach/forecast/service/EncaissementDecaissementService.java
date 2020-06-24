package org.apac.erp.cach.forecast.service;

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
}
