package org.apac.erp.cach.forecast.service;

import java.util.ArrayList;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.AgencyDTO;
import org.apac.erp.cach.forecast.persistence.entities.Agency;
import org.apac.erp.cach.forecast.persistence.repositories.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgencyService {

	@Autowired
	private AgencyRepository agencyRepo;

	@Autowired
	private BankService bankService;

	public List<AgencyDTO> findAllAgencies() {
		List<AgencyDTO> dtos = new ArrayList<AgencyDTO>();
		List<Agency> agencies = agencyRepo.findAll();
		agencies.stream().forEach(agency -> {
			AgencyDTO dto = new AgencyDTO(agency.getAgencyId(), agency.getAgencyName(), agency.getAgencyAddress(),
					agency.getAgencyPhoneNumber(), agency.getAgencyEmail(), agency.getAgencyBank().getBankName(),
					agency.getCreatedAt(), agency.getUpdatedAt());
			dtos.add(dto);
		});

		return dtos;
	}

	public Agency saveNewAgencyToGivenBank(Agency agency, Long bankId) {
		agency.setAgencyBank(bankService.findBankById(bankId));
		return agencyRepo.save(agency);
	}
	
	public Agency findOneById(Long agencyId) {
		return agencyRepo.findOne(agencyId);
	}

	public void deleteAgency(Long agencyId) {
		agencyRepo.delete(agencyId);
	}

}
