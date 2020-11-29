package org.apac.erp.cach.forecast.controller;

import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.OperationTreserorieDto;
import org.apac.erp.cach.forecast.persistence.entities.Invoice;
import org.apac.erp.cach.forecast.service.SupervisionTresorerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/supervision")
public class SupervisionTresorerieController {
	
	@Autowired
	private SupervisionTresorerieService supervisionTresorerieService;
	
	@CrossOrigin
	@GetMapping("global/{accountId}/{startDate}/{endDate}")
	public List<OperationTreserorieDto> findGlobalSupervisionEngage(@PathVariable("accountId") Long accountId,  @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		return supervisionTresorerieService.globalSupervisionEngage(accountId, startDate, endDate, false);

	}
	
	@CrossOrigin
	@GetMapping("rapprochement-bancaire/{accountId}/{startDate}/{endDate}")
	public List<OperationTreserorieDto> rapprochementBancaire(@PathVariable("accountId") Long accountId,  @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		return supervisionTresorerieService.globalSupervisionEngage(accountId, startDate, endDate, true);

	}

	@CrossOrigin
	@PutMapping()
	public void rapprochementBancaireModifyOperation(@RequestBody OperationTreserorieDto operationDto) {
		supervisionTresorerieService.rapprochementBancaireModifyOperation(operationDto);

	}
}
