package com.example.interviewPrep.quiz.product.factory;

import com.example.interviewPrep.quiz.interview.service.InterviewService;
import com.example.interviewPrep.quiz.product.domain.Product;
import com.example.interviewPrep.quiz.product.domain.ProductOne;
import com.example.interviewPrep.quiz.product.domain.ProductSix;
import com.example.interviewPrep.quiz.product.domain.ProductThree;
import com.example.interviewPrep.quiz.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class ProductFactory {

    public final ProductRepository productRepository;
    public final InterviewService interviewService;

    public ProductFactory(ProductRepository productRepository, InterviewService interviewService){
        this.productRepository = productRepository;
        this.interviewService = interviewService;
    }

    public Product createProduct(int type, LocalDateTime interviewDateTime){

        Product product = null;

        if(type == 1){
            product = new ProductOne();
        }else if(type == 2){
            product = new ProductThree();
        }else if(type == 3){
            product = new ProductSix();
        }

        interviewService.createInterviews(product, interviewDateTime);

        assert product != null;
        productRepository.save(product);

        return product;
    }



}
