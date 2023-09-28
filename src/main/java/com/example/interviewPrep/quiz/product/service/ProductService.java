package com.example.interviewPrep.quiz.product.service;

import com.example.interviewPrep.quiz.product.dto.request.ProductRequest;
import com.example.interviewPrep.quiz.product.factory.ProductFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductFactory productFactory;
    public ProductService(ProductFactory productFactory){
        this.productFactory = productFactory;
    }


    public void createProduct(ProductRequest productRequest){
        productFactory.createProduct(productRequest);
    }




}
