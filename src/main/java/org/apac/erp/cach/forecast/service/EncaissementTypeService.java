package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.EncaissementType;
import org.apac.erp.cach.forecast.persistence.repositories.EncaissementTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EncaissementTypeService {

	@Autowired
	private EncaissementTypeRepository encaissementTypeRepo;

	public List<EncaissementType> findAllEncaissementTypes() {
		return encaissementTypeRepo.findAll();
	}

	public EncaissementType saveEncaissementType(EncaissementType encaissementType) {
		return encaissementTypeRepo.save(encaissementType);
	}

	public EncaissementType getEncaissementTypeById(Long encaissementTypeId) {
		return encaissementTypeRepo.getOne(encaissementTypeId);
	}

	public void deleteEncaissementType(Long encaissementTypeId) {
		this.encaissementTypeRepo.delete(encaissementTypeId);
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
