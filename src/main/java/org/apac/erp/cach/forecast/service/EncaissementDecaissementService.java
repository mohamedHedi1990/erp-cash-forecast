package org.apac.erp.cach.forecast.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apac.erp.cach.forecast.enumeration.EncaissementDecaissementType;
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
		List<EncaissementDecaissement> encDecs = encaissementDecaissementRepo
				.findByEncaissementDecaissementDeadlineDateGreaterThanEqualAndEncaissementDecaissementDeadlineDateLessThanEqual(
						startDate, endDate);
		
		List<EncaissementDecaissementType> encTypes = new ArrayList<EncaissementDecaissementType>();
		encTypes.add(EncaissementDecaissementType.ENCAISSEMENT_AUTRE);
		encTypes.add(EncaissementDecaissementType.ENCAISSEMENT_PLAN_COMPTABLE_TIERS);

		return encDecs.stream().filter(encDec -> encTypes.contains(encDec.getEncaissementDecaissementType()))
				.collect(Collectors.toList());


	}

	public List<EncaissementDecaissement> findAllDecaissementsBetweenTwoDates(Date startDate, Date endDate) {
		List<EncaissementDecaissement> encDecs = encaissementDecaissementRepo
				.findByEncaissementDecaissementDeadlineDateGreaterThanEqualAndEncaissementDecaissementDeadlineDateLessThanEqual(
						startDate, endDate);
		
		List<EncaissementDecaissementType> decTypes = new ArrayList<EncaissementDecaissementType>();
		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_AUTRE);
		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_BANK_COMMISSION);
		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_INTERESTS);
		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_PLAN_COMPTABLE_TIERS);
		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_SALAIRE);

		return encDecs.stream().filter(encDec -> decTypes.contains(encDec.getEncaissementDecaissementType()))
				.collect(Collectors.toList());
	}

	public List<EncaissementDecaissement> findAllEncaissements() {
		List<EncaissementDecaissement> encDecs = encaissementDecaissementRepo.findAll();
		List<EncaissementDecaissementType> encTypes = new ArrayList<EncaissementDecaissementType>();
		encTypes.add(EncaissementDecaissementType.ENCAISSEMENT_AUTRE);
		encTypes.add(EncaissementDecaissementType.ENCAISSEMENT_PLAN_COMPTABLE_TIERS);

		return encDecs.stream().filter(encDec -> encTypes.contains(encDec.getEncaissementDecaissementType()))
				.collect(Collectors.toList());

	}
	
	public List<EncaissementDecaissement> findAllDecaissements() {
		List<EncaissementDecaissement> encDecs = encaissementDecaissementRepo.findAll();
		List<EncaissementDecaissementType> decTypes = new ArrayList<EncaissementDecaissementType>();
		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_AUTRE);
		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_BANK_COMMISSION);
		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_INTERESTS);
		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_PLAN_COMPTABLE_TIERS);
		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_SALAIRE);

		return encDecs.stream().filter(encDec -> decTypes.contains(encDec.getEncaissementDecaissementType()))
				.collect(Collectors.toList());

	}


}
