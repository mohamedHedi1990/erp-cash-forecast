package org.apac.erp.cach.forecast.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apac.erp.cach.forecast.dtos.OperationTreserorieDto;
import org.apac.erp.cach.forecast.dtos.StatusCashDto;
import org.apac.erp.cach.forecast.dtos.TurnoverDto;
import org.apac.erp.cach.forecast.dtos.*;
import org.apac.erp.cach.forecast.enumeration.OperationDtoType;
import org.apac.erp.cach.forecast.service.SupervisionTresorerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	@GetMapping("global-supervision-engage/{startDate}/{endDate}")
	public List<OperationTreserorieDto> findGlobalSupervision(@PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
																	@PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		return supervisionTresorerieService.globalSupervision(startDate, endDate, false);
	}
	
	@CrossOrigin
	@GetMapping("rapprochement-bancaire/{accountId}/{startDate}/{endDate}")
	public List<OperationTreserorieDto> rapprochementBancaire(@PathVariable("accountId") Long accountId,  @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		return supervisionTresorerieService.globalSupervisionEngage(accountId, startDate, endDate, true);

	}
	
	@CrossOrigin
	@GetMapping("non-engage/{startDate}/{endDate}")
	public List<OperationTreserorieDto> nonEngage(@PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, @RequestParam("isCustomer") boolean isCustomer, @RequestParam("isProvider") boolean isProvider) {
		return supervisionTresorerieService.nonEngageSupervision(startDate, endDate, isCustomer, isProvider);

	}
	@CrossOrigin
	@GetMapping("status-cash/{startDate}/{endDate}")
	public List<StatusCashDto> statusCash(@PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
										  @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, @RequestParam("isNotEngaged") boolean isNotEngaged) throws ParseException {
		return supervisionTresorerieService.statusCash(startDate,endDate,isNotEngaged);

	}
	@CrossOrigin
	@GetMapping("turnover/{startDate}/{endDate}")
	public List<TurnoverDto> turnover(@PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,@PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		return supervisionTresorerieService.findTurnover(startDate,endDate );

	}
	@CrossOrigin
	@PutMapping("rapprochement-bancaire")
	public void rapprochementBancaireModifyOperation(@RequestBody OperationTreserorieDto operationDto) {
		supervisionTresorerieService.rapprochementBancaireModifyOperation(operationDto);

	}
	
	@CrossOrigin
	@PutMapping("/rapprochement-bancaire/validate/{operationRealType}/{operationRealId}")
	public void validate(@PathVariable("operationRealType") OperationDtoType operationRealType, @PathVariable("operationRealId") Long operationRealId, @RequestBody OperationTreserorieDto operation) {
		supervisionTresorerieService.validate(operationRealType, operationRealId, operation);
	}

	@CrossOrigin
	@GetMapping("/get-customer-sales/{startDate}/{endDate}")
	public List<CustomerSaleDto> getCustomersSales(@PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,@PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
		return supervisionTresorerieService.getCustomersSales(startDate,endDate);
	}

	@CrossOrigin
	@GetMapping("/get-product-sales/{startDate}/{endDate}")
	public List<ProductSaleDto> getProductsSales(@PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,@PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		return supervisionTresorerieService.getProductsSales(startDate,endDate);
	}

	@CrossOrigin
	@GetMapping("get-customers-sales-by-product/{startDate}/{endDate}")
	public List<CustomerProductSaleDto> getCustomersSalesByProduct(@PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,@PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		return supervisionTresorerieService.getCustomersSalesByProduct(startDate,endDate );
	}

	@CrossOrigin
	@GetMapping("/get-general-ledger-by-customer/{idCustomer}")
	public List<GeneralLedgerDto> getGeneralLedger(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,@PathVariable(name = "idCustomer" ) Long idCustomer) {
		return supervisionTresorerieService.getGeneralLedger(startDate,endDate ,idCustomer);
	}
}
