package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.persistence.entities.GeneratedInvoice;
import org.apac.erp.cach.forecast.persistence.entities.GeneratedInvoiceLine;
import org.apac.erp.cach.forecast.service.GeneratedInvoiceLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/generatedinvoiceline")
public class GeneratedInvoiceLineConroller {

	@Autowired
	private GeneratedInvoiceLineService generatedInvoiceLineService;

	@CrossOrigin
	@GetMapping("/findallbygeneratedinvoice")
	public List<GeneratedInvoiceLine> findAllByGeneratedInvoice(@RequestBody GeneratedInvoice generatedInvoice) {
		return this.generatedInvoiceLineService.findByGeneratedInvoice(generatedInvoice);
	}

	@CrossOrigin
	@PostMapping()
	public GeneratedInvoiceLine saveGeneratedInvoiceLine(@RequestBody GeneratedInvoiceLine  generatedInvoiceLine) {
		return generatedInvoiceLineService.saveGeneratedInvoiceLine(generatedInvoiceLine);
	}

	@CrossOrigin
	@GetMapping("/findbyline/{generatedInvoiceLineId}")
	public GeneratedInvoiceLine getGeneratedInvoiceLineById(@PathVariable("generatedInvoiceLineId") Long generatedInvoiceLineId) {
		return this.generatedInvoiceLineService.findById(generatedInvoiceLineId);
	}

	@CrossOrigin
	@DeleteMapping("/{generatedInvoiceLineId}")
	public void deleteGeneratedInvoiceLine(@PathVariable("generatedInvoiceLineId") Long generatedInvoiceLineId) {
		generatedInvoiceLineService.deleteGeneratedInvoiceLine(generatedInvoiceLineId);
	}

}
