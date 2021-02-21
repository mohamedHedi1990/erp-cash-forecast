package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.persistence.entities.Devis;
import org.apac.erp.cach.forecast.service.DevisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devis")
public class DevisController {
    @Autowired
    DevisService devisService;

    @CrossOrigin
    @GetMapping()
    List<Devis> findAllDeviss()
    {
        return this.devisService.getAllInvoices();
    }

    @CrossOrigin
    @GetMapping("{devis_id}")
    Devis findDevisById(@PathVariable("devis_id")Long id)
    {
       return this.devisService.getInvoiceById(id);
    }

    @CrossOrigin
    @PostMapping()
    ResponseEntity<Devis> saveDevis(@RequestBody Devis devis)
    {   if(this.devisService.existesByDevisNumberAndOtherId(devis)) {
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    }else {
        return new ResponseEntity<>(this.devisService.saveDevis(devis), HttpStatus.OK);
    }
    }

    @CrossOrigin
    @DeleteMapping()
    public void  deleteAllDeviss()
    {
        this.devisService.deleteAllDevis();
    }

    @CrossOrigin
    @DeleteMapping("{devis_id}")
    public void deleteInvoiceById(@PathVariable("devis_id")Long id)
    {
        this.devisService.deleteDevisById(id);
    }
}
