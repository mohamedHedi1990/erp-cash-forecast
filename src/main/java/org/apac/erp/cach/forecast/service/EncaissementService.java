package org.apac.erp.cach.forecast.service;

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

	public Encaissement saveEncaissement(Encaissement encaissement) {
		return encaissementRepo.save(encaissement);
	}
	
	public Encaissement getEncaissementById(Long encaissementId) {
		return encaissementRepo.getOne(encaissementId);
	}
	
	public void deleteEncaissement(Long encaissementId) {
		this.encaissementRepo.delete(encaissementId);
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
