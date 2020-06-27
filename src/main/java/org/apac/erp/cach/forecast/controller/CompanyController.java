package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Company;
import org.apac.erp.cach.forecast.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@CrossOrigin
	@GetMapping()
	public List<Company> findAllCompanies() {
		return companyService.findAllCompanies();
	}

	@CrossOrigin
	@PostMapping()
	public Company saveNewCompany(@RequestBody Company company) {
		return companyService.saveNewCompany(company);
	}
	@CrossOrigin
	@GetMapping("by-company-id/{companyId}")
	public Company findCompanyById(@PathVariable("companyId") Long companyId) {
		return companyService.findCompanyById(companyId);
	}
	
	@CrossOrigin
	@DeleteMapping("/{companyId}")
	public void deleteCompany(@PathVariable("companyId") Long companyId) {
		 companyService.deleteCompany(companyId);
	}

}
