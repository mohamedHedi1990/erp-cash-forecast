package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.BankAccount;
import org.apac.erp.cach.forecast.persistence.entities.Comission;
import org.apac.erp.cach.forecast.persistence.repositories.ComissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComissionService {

	@Autowired
	private ComissionRepository comissionRepo;
	
	@Autowired
	private BankAccountService bankAccountService;
	
	public void deleteComission(Long commisionId) {
		this.comissionRepo.delete(commisionId);
	}
	
	public Comission getComissionById(Long comissionId) {
		return this.comissionRepo.findOne(comissionId);
	}
	
	public Comission saveComission(Comission comission) {
		return this.comissionRepo.save(comission);
	}
	
	public List<Comission> saveAllComissions(Long accountId, List<Comission>comissions)
	{
		BankAccount account = this.bankAccountService.getAccountById(accountId);
		comissions.stream().forEach(comission -> {
			comission.setBankAccount(account);
		});
		return  this.comissionRepo.save(comissions);
	}
}
