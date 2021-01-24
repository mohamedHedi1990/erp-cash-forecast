package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.persistence.entities.BonLivraison;
import org.apac.erp.cach.forecast.service.BonLivraisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bonlivraison")
public class BonLivraisonController {
    @Autowired
    BonLivraisonService bonLivraisonService;

    @CrossOrigin
    @GetMapping()
    List<BonLivraison> findAllBonLivraisons()
    {
        return this.bonLivraisonService.getAllInvoices();
    }
    @CrossOrigin
    @GetMapping("{bonlivraison_id}")
    BonLivraison findBonLivraisonById(@PathVariable("bonlivraison_id")Long id)
    {
       return this.bonLivraisonService.getInvoiceById(id);
    }
    @CrossOrigin
    @PostMapping()
    BonLivraison saveBonLivraison(@RequestBody BonLivraison bonLivraison)
    {
        return  this.bonLivraisonService.saveBonLivraison(bonLivraison);
    }
    @CrossOrigin
    @DeleteMapping()
    public void  deleteAllBonLivraisons()
    {
        this.bonLivraisonService.deleteAllBonLivraison();
    }
    @CrossOrigin
    @DeleteMapping("{bonlivraison_id}")
    public void deleteInvoiceById(@PathVariable("bonlivraison_id")Long id)
    {
        this.bonLivraisonService.deleteBonLivraisonById(id);
    }
}
