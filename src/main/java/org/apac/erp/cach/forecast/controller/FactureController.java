package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.persistence.entities.BonLivraison;
import org.apac.erp.cach.forecast.persistence.entities.Facture;
import org.apac.erp.cach.forecast.service.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    Facture saveFacture(@RequestBody Facture facture)
    {
        return  this.factureService.saveFacture(facture);
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
}
