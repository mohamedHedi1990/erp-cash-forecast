package org.apac.erp.cach.forecast.service;

import java.util.Date;
import java.util.List;

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

	public List<Encaissement> findAllEncaissementsBetweenTwoDates(Date startDate, Date endDate) {
		return encaissementRepo.findByTransactionDeadlineDateGreaterThanEqualAndTransactionDeadlineDateLessThanEqual(startDate, endDate);
	}

	public Encaissement saveNewEncaissement(Encaissement encDec) {
		return encaissementRepo.save(encDec);
	}

//	public List<EncaissementDecaissement> findAllEncaissementDecaissements() {
//		return encaissementDecaissementRepo.findAll();
//	}
//
//	public EncaissementDecaissement saveNewEncaissementDecaissement(EncaissementDecaissement encDec) {
//		return encaissementDecaissementRepo.save(encDec);
//	}
//
//	public List<EncaissementDecaissement> findAllEncaissementsBetweenTwoDates(Date startDate, Date endDate) {
//		List<EncaissementDecaissement> encDecs = encaissementDecaissementRepo
//				.findByEncaissementDecaissementDeadlineDateGreaterThanEqualAndEncaissementDecaissementDeadlineDateLessThanEqual(
//						startDate, endDate);
//		
//		List<EncaissementDecaissementType> encTypes = new ArrayList<EncaissementDecaissementType>();
//		encTypes.add(EncaissementDecaissementType.ENCAISSEMENT_AUTRE);
//		encTypes.add(EncaissementDecaissementType.ENCAISSEMENT_PLAN_COMPTABLE_TIERS);
//		encTypes.add(EncaissementDecaissementType.ENCAISSEMENT_FACTURE_CLIENT);
//
//		return encDecs.stream().filter(encDec -> encTypes.contains(encDec.getEncaissementDecaissementType()))
//				.collect(Collectors.toList());
//
//
//	}
//
//	public List<EncaissementDecaissement> findAllDecaissementsBetweenTwoDates(Date startDate, Date endDate) {
//		List<EncaissementDecaissement> encDecs = encaissementDecaissementRepo
//				.findByEncaissementDecaissementDeadlineDateGreaterThanEqualAndEncaissementDecaissementDeadlineDateLessThanEqual(
//						startDate, endDate);
//		
//		List<EncaissementDecaissementType> decTypes = new ArrayList<EncaissementDecaissementType>();
//		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_AUTRE);
//		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_BANK_COMMISSION);
//		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_INTERESTS);
//		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_PLAN_COMPTABLE_TIERS);
//		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_FACTURE_FOURNISSEUR);
//		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_SALAIRE);
//
//		return encDecs.stream().filter(encDec -> decTypes.contains(encDec.getEncaissementDecaissementType()))
//				.collect(Collectors.toList());
//	}
//
//	public List<EncaissementDecaissement> findAllEncaissements() {
//		List<EncaissementDecaissement> encDecs = encaissementDecaissementRepo.findAll();
//		List<EncaissementDecaissementType> encTypes = new ArrayList<EncaissementDecaissementType>();
//		encTypes.add(EncaissementDecaissementType.ENCAISSEMENT_AUTRE);
//		encTypes.add(EncaissementDecaissementType.ENCAISSEMENT_PLAN_COMPTABLE_TIERS);
//
//		return encDecs.stream().filter(encDec -> encTypes.contains(encDec.getEncaissementDecaissementType()))
//				.collect(Collectors.toList());
//
//	}
//	
//	public List<EncaissementDecaissement> findAllDecaissements() {
//		List<EncaissementDecaissement> encDecs = encaissementDecaissementRepo.findAll();
//		List<EncaissementDecaissementType> decTypes = new ArrayList<EncaissementDecaissementType>();
//		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_AUTRE);
//		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_BANK_COMMISSION);
//		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_INTERESTS);
//		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_PLAN_COMPTABLE_TIERS);
//		decTypes.add(EncaissementDecaissementType.DECAISSEMENT_SALAIRE);
//
//		return encDecs.stream().filter(encDec -> decTypes.contains(encDec.getEncaissementDecaissementType()))
//				.collect(Collectors.toList());
//
//	}

}
