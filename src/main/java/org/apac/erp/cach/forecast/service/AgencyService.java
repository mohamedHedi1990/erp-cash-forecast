package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Agency;
import org.apac.erp.cach.forecast.persistence.repositories.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgencyService {

	@Autowired
	private AgencyRepository agencyRepo;

	public List<Agency> findAllAgencies() {
		return agencyRepo.findAll();
	}

}
