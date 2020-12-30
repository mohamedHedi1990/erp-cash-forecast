package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.Comission;
import org.apac.erp.cach.forecast.persistence.repositories.ComissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComissionService {

	@Autowired
	private ComissionRepository comissionRepo;
	
	public void deleteComission(Long commisionId) {
		this.comissionRepo.delete(commisionId);
	}
	
	public Comission getComissionById(Long comissionId) {
		return this.comissionRepo.findOne(comissionId);
	}
	
	public Comission saveComission(Comission comission) {
		return this.comissionRepo.save(comission);
	}
	public List<Comission> saveAllComissions(List<Comission>comissions)
	{
		return  this.comissionRepo.save(comissions);
	}
}
