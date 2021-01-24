package org.apac.erp.cach.forecast.controller;

import org.apac.erp.cach.forecast.persistence.entities.Product;
import org.apac.erp.cach.forecast.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @CrossOrigin
    @PostMapping()
    public Product saveProduct(@RequestBody Product product)
    {
        return  this.productService.saveProduct(product);
    }
    @CrossOrigin
    @GetMapping()
    public List<Product> getAllProducts()
    {
        return  this.productService.getAllProducts();
    }
    @CrossOrigin
    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable("productId") Long productId)
    {
        return  this.productService.getProductById(productId);
    }
    @CrossOrigin
    @DeleteMapping("/{productId}")
    public void deleteProductById(@PathVariable("productId") Long productId)
    {
        this.productService.deleteProductById(productId);
    }

}
