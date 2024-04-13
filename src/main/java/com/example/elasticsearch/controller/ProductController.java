package com.example.elasticsearch.controller;

import com.example.elasticsearch.entity.Product;
import com.example.elasticsearch.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apis")
public class ProductController {

    @Autowired
    private ProductService productService;
    @GetMapping("/findAll")
    Iterable<Product> findAll(){
        return productService.getProducts();

    }

    @PostMapping("/insertProduct")
    public Product insertProduct(@RequestBody Product product){
        return productService.insertProduct(product);
    }

}
