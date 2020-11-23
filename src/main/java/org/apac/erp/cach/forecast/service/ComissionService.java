package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.repositories.ComissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComissionService {

	@Autowired
	private ComissionRepository comissionRepo;
	
	public void deleteComission(Long commisionId) {
		this.comissionRepo.delete(commisionId);
	}
}
