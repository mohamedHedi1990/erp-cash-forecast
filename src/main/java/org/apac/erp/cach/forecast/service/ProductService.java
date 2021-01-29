package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.Product;
import org.apac.erp.cach.forecast.persistence.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    public Product saveProduct(Product product)
    {
        return this.productRepository.save(product);
    }
    public List<Product> getAllProducts()
    {
       return  this.productRepository.findAllByOrderByProductLabelAsc();
    }
    public Product getProductById(Long id)
    {
        return this.productRepository.findByProductId(id);
    }
    public void deleteAllProducts()
    {
        this.productRepository.deleteAll();
    }
    public void deleteProductById(Long id)
    {
        this.productRepository.delete(id);
    }

}
