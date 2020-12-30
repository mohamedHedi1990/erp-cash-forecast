package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.persistence.entities.Comission;
import org.apac.erp.cach.forecast.service.ComissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
	@PostMapping("/saveAndUpdateAllCommission")
	public List<Comission> addAllComission(@RequestBody List<Comission> comissions)
	{
		 return  comissionService.saveAllComissions(comissions);

	}

	@CrossOrigin
	@DeleteMapping("/{comissionId}")
	public void deleteComission(@PathVariable("comissionId") Long comissionId) {
		comissionService.deleteComission(comissionId);
	}
}
