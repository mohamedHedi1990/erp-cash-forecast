package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.Comission;
import org.apac.erp.cach.forecast.service.ComissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comission")
public class ComissionController {

	@Autowired
	private ComissionService comissionService;
	@CrossOrigin
	@PostMapping
	public Comission addComission(@RequestBody Comission comission)
	{
		return comissionService.saveComission(comission);
	}

	@CrossOrigin
	@DeleteMapping("/{comissionId}")
	public void deleteComission(@PathVariable("comissionId") Long comissionId) {
		comissionService.deleteComission(comissionId);
	}
	
	@CrossOrigin
	@PostMapping("{accountId}/saveAndUpdateAllCommission")
	public List<Comission> addAllComission(@PathVariable("accountId") Long accountId, @RequestBody List<Comission> comissions)
	{
		 return  comissionService.saveAllComissions(accountId, comissions);

	}
}
