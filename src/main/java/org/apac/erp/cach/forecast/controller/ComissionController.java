package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.service.ComissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comission")
public class ComissionController {

	@Autowired
	private ComissionService comissionService;
	
	@CrossOrigin
	@DeleteMapping("/{comissionId}")
	public void deleteComission(@PathVariable("comissionId") Long comissionId) {
		comissionService.deleteComission(comissionId);
	}
}
