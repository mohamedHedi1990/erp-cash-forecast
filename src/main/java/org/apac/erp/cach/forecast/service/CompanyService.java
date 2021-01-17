package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Company;
import org.apac.erp.cach.forecast.persistence.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepo;

	public List<Company> findAllCompanies() {
		return companyRepo.findAll();
	}

	public Company saveNewCompany(Company company) {
		return companyRepo.save(company);
	}

	public Company findCompanyById(Long companyId) {
		return companyRepo.findOne(companyId);
	}

	public void deleteCompany(Long companyId) {
		companyRepo.delete(companyId);
	}

	public Company updateCompanyLogoUrl(String fileDownloadUri, Long companyId) {
		Company company = this.findCompanyById(companyId);
		company.setCompanyLogoUrl(fileDownloadUri);
		return this.saveNewCompany(company);
	}

}
