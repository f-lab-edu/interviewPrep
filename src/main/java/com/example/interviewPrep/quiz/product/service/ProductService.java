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
    private final ProductRepository productRepository;

    public ProductService(ProductFactory productFactory, ProductRepository productRepository){
        this.productFactory = productFactory;
        this.productRepository = productRepository;
    }


    public void createProduct(ProductRequest productRequest){

        int type = productRequest.getType();
        LocalDateTime interviewDateTime = productRequest.getInterviewDateTime();

        Product product = productFactory.createProduct(type, interviewDateTime);


    }




}
