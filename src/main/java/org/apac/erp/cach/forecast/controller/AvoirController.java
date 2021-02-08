package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.persistence.entities.Avoir;
import org.apac.erp.cach.forecast.service.AvoirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avoir")
public class AvoirController {
    @Autowired
    AvoirService avoirService;

    @CrossOrigin
    @GetMapping()
    List<Avoir> findAllAvoirs()
    {
        return this.avoirService.getAllInvoices();
    }

    @CrossOrigin
    @GetMapping("{avoir_id}")
    Avoir findAvoirById(@PathVariable("avoir_id")Long id)
    {
       return this.avoirService.getInvoiceById(id);
    }

    @CrossOrigin
    @PostMapping("/generer-from-facture")
    Avoir genererFacture(@RequestBody Long avoirId)
    {
        return  this.avoirService.genererAvoirFromFacture(avoirId);
    }


    @CrossOrigin
    @DeleteMapping()
    public void  deleteAllAvoirs()
    {
        this.avoirService.deleteAllAvoir();
    }

    @CrossOrigin
    @DeleteMapping("{avoir_id}")
    public void deleteInvoiceById(@PathVariable("avoir_id")Long id)
    {
        this.avoirService.deleteAvoirById(id);
    }
}
