package com.example.interviewPrep.quiz.product.service;

import com.example.interviewPrep.quiz.product.domain.Product;
import com.example.interviewPrep.quiz.product.dto.request.ProductRequest;
import com.example.interviewPrep.quiz.product.factory.ProductFactory;
import com.example.interviewPrep.quiz.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
