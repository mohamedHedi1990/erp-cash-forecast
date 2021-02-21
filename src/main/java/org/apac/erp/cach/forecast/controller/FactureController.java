package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.dtos.FactureFilterDto;
import org.apac.erp.cach.forecast.dtos.FactureProductDto;
import org.apac.erp.cach.forecast.persistence.entities.BonLivraison;
import org.apac.erp.cach.forecast.persistence.entities.Facture;
import org.apac.erp.cach.forecast.service.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;

@RestController
@RequestMapping("/api/facture")
public class FactureController {
    @Autowired
    FactureService factureService;

    @CrossOrigin
    @GetMapping()
    List<Facture> findAllFactures()
    {
        return this.factureService.getAllInvoices();
    }

    @CrossOrigin
    @PostMapping("/by-date-between")
    List<Facture> findAllFacturesDateBetween(@RequestBody Map<String, Date> dates)
    {
        return this.factureService.findAllFacturesDateBetween(dates);
    }


    @CrossOrigin
    @GetMapping("{facture_id}")
    Facture findFactureById(@PathVariable("facture_id")Long id)
    {
       return this.factureService.getInvoiceById(id);
    }

    @CrossOrigin
    @PostMapping("/generer-from-bl")
    Facture genererFacture(@RequestBody List<Long> blsIds)
    {
        return  this.factureService.genererFactureFromBL(blsIds);
    }

    @CrossOrigin
    @PostMapping("/generer-from-devis")
    Facture genererFacture(@RequestBody Long devisId)
    {
        return  this.factureService.genererFactureFromDevis(devisId);
    }

    @CrossOrigin
    @PostMapping()
    ResponseEntity<Facture> saveFacture(@RequestBody Facture facture)
    {
        if(this.factureService.existesByFactureNumberAndOtherId(facture)) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }else {
            return new ResponseEntity<>(this.factureService.saveFacture(facture), HttpStatus.OK);
        }
    }

    @CrossOrigin
    @DeleteMapping()
    public void  deleteAllFactures()
    {
        this.factureService.deleteAllFacture();
    }

    @CrossOrigin
    @DeleteMapping("{facture_id}")
    public void deleteInvoiceById(@PathVariable("facture_id")Long id)
    {
        this.factureService.deleteFactureById(id);
    }

    @CrossOrigin
    @PostMapping("/find-by-filter-without-product")
    public List<Facture> findByFilterWithoutProduct(@RequestBody FactureFilterDto factureFilterDto){
        return factureService.findByFilterWithoutProduct(factureFilterDto);
    }

    @CrossOrigin
    @PostMapping("/find-by-filter-with-product")
    public List<FactureProductDto> findByFilterWithProduct(@RequestBody FactureFilterDto factureFilterDto){
        return factureService.findByFilterWithProduct(factureFilterDto);
    }
}
