package org.apac.erp.cach.forecast.service;

import org.apac.erp.cach.forecast.persistence.entities.Product;
import org.apac.erp.cach.forecast.persistence.entities.ProductGroup;
import org.apac.erp.cach.forecast.persistence.repositories.ProductGroupRepository;
import org.apac.erp.cach.forecast.persistence.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductGroupService {
    @Autowired
    ProductGroupRepository productgroupRepository;
    @Autowired
    ProductRepository productRepository;
    public ProductGroup saveProductGroup(ProductGroup productgroup)
    {
        return this.productgroupRepository.save(productgroup);
    }
    public List<ProductGroup> getAllProductGroups()
    {
        return  this.productgroupRepository.findAll();
    }
    public ProductGroup getProductGroupById(Long id)
    {
        return this.productgroupRepository.findOne(id);
    }
    public void deleteAllProductGroups()
    {
        this.productgroupRepository.deleteAll();
    }
    public void deleteProductGroupById(Long id)
    {
        List<Product> productListByProductGroup=productRepository.findAllByProductGroupProductGroupId(id);
        productRepository.delete(productListByProductGroup);
        this.productgroupRepository.delete(id);
    }
}
