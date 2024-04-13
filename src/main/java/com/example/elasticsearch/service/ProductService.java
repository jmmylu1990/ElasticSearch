package com.example.elasticsearch.service;

import com.example.elasticsearch.entity.Product;
import com.example.elasticsearch.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Iterable<Product> getProducts(){
        return productRepository.findAll();
    }

    public Product insertProduct(Product product){
        return productRepository.save(product);
    }

    public Product updateProducts(Product product, Long id){

        return productRepository.findById(id).get();
    }

    public void deleteProducts(Long id){

         productRepository.deleteById(id);
    }
}
