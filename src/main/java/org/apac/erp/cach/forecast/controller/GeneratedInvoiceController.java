package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.persistence.entities.GeneratedInvoice;
import org.apac.erp.cach.forecast.service.GeneratedInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/generatedinvoice")
public class GeneratedInvoiceController {
    @Autowired
    GeneratedInvoiceService generatedInvoiceService;

    @CrossOrigin
    @GetMapping()
    List<GeneratedInvoice> findAllGeneratedInvoices()
    {
        return this.generatedInvoiceService.getAllInvoices();
    }
    @CrossOrigin
    @GetMapping("{generatedinvoice_id}")
    GeneratedInvoice findGeneratedInvoiceById(@PathVariable("generatedinvoice_id")Long id)
    {
       return this.generatedInvoiceService.getInvoiceById(id);
    }
    @CrossOrigin
    @PostMapping()
    GeneratedInvoice saveGeneratedInvoice(@RequestBody GeneratedInvoice generatedInvoice)
    {
        return  this.generatedInvoiceService.saveGeneratedInvoice(generatedInvoice);
    }
    @CrossOrigin
    @DeleteMapping()
    public void  deleteAllGeneratedInvoices()
    {
        this.generatedInvoiceService.deleteAllGeneratedInvoice();
    }
    @CrossOrigin
    @DeleteMapping("{generatedinvoice_id}")
    public void deleteInvoiceById(@PathVariable("generatedinvoice_id")Long id)
    {
        this.generatedInvoiceService.deleteGeneratedInvoiceById(id);
    }
}
