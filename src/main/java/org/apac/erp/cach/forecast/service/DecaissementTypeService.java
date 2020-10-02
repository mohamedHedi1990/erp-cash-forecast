package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.DecaissementType;
import org.apac.erp.cach.forecast.persistence.entities.EncaissementType;
import org.apac.erp.cach.forecast.persistence.repositories.DecaissementTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DecaissementTypeService {

	@Autowired
	private DecaissementTypeRepository decaissementTypeRepo;

	public List<DecaissementType> findAllDecaissementTypes() {
		return decaissementTypeRepo.findAll();
	}

	public DecaissementType saveDecaissementType(DecaissementType decaissementType) {
		return decaissementTypeRepo.save(decaissementType);
	}

	public DecaissementType getDecaissementTypeById(Long decaissementTypeId) {
		return decaissementTypeRepo.getOne(decaissementTypeId);
	}

	public void deleteDecaissementType(Long decaissementTypeId) {
		this.decaissementTypeRepo.delete(decaissementTypeId);
	}

	/*
	 * public List<EncaissementDecaissement>
	 * findAllEncaissementsBetweenTwoDates(Date startDate, Date endDate) {
	 * List<EncaissementDecaissement> encDecs = encaissementDecaissementRepo
	 * .findByEncaissementDecaissementDeadlineDateGreaterThanEqualAndEncaissementDecaissementDeadlineDateLessThanEqual(
	 * startDate, endDate);
	 * 
	 * List<EncaissementDecaissementType> encTypes = new
	 * ArrayList<EncaissementDecaissementType>();
	 * encTypes.add(EncaissementDecaissementType.ENCAISSEMENT_AUTRE);
	 * encTypes.add(EncaissementDecaissementType.
	 * ENCAISSEMENT_PLAN_COMPTABLE_TIERS);
	 * encTypes.add(EncaissementDecaissementType.ENCAISSEMENT_FACTURE_CLIENT);
	 * 
	 * return encDecs.stream().filter(encDec ->
	 * encTypes.contains(encDec.getEncaissementDecaissementType()))
	 * .collect(Collectors.toList());
	 * 
	 * 
	 * }
	 */

}
