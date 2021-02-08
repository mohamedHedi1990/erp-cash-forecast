package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Company;
import org.apac.erp.cach.forecast.persistence.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepo;

	public List<Company> findAllCompanies() {
		return companyRepo.findAll();
	}

	public Company findCompanie() {
		List<Company> companies=companyRepo.findAll();
		if(companies.size()>0) {
			return companies.get(0);
		}else
			return null;
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

	@Transactional
	public Company updateCompanyLogoUrl(String fileDownloadUri, Long companyId) {
		Company company = this.findCompanyById(companyId);
		company.setCompanyLogoUrl(fileDownloadUri);
		return this.saveNewCompany(company);
	}

	@Transactional
	public Company updateCompanySignatureUrl(String fileDownloadUri, Long companyId) {
		Company company = this.findCompanyById(companyId);
		company.setCompanySignatureUrl(fileDownloadUri);
		return this.saveNewCompany(company);
	}

}
