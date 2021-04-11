package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.persistence.entities.ProductGroup;
import org.apac.erp.cach.forecast.service.ProductGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-group")
public class ProductGroupController {

    @Autowired
    ProductGroupService productgroupService;

    @CrossOrigin
    @PostMapping()
    public ProductGroup saveProductGroup(@RequestBody ProductGroup productgroup)
    {
        return  this.productgroupService.saveProductGroup(productgroup);
    }
    @CrossOrigin
    @GetMapping()
    public List<ProductGroup> getAllProductGroups()
    {
        return  this.productgroupService.getAllProductGroups();
    }
    @CrossOrigin
    @GetMapping("/{productGroupId}")
    public ProductGroup getProductGroupById(@PathVariable("productGroupId") Long productGroupId)
    {
        return  this.productgroupService.getProductGroupById(productGroupId);
    }
    @CrossOrigin
    @DeleteMapping("/{productGroupId}")
    public void deleteProductGroupById(@PathVariable("productGroupId") Long productGroupId)
    {
        this.productgroupService.deleteProductGroupById(productGroupId);
    }
}
