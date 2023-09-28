package com.example.interviewPrep.quiz.product.factory;

import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.interview.service.InterviewService;
import com.example.interviewPrep.quiz.product.domain.Product;
import com.example.interviewPrep.quiz.product.domain.ProductOne;
import com.example.interviewPrep.quiz.product.domain.ProductSix;
import com.example.interviewPrep.quiz.product.domain.ProductThree;
import com.example.interviewPrep.quiz.product.dto.request.ProductRequest;
import com.example.interviewPrep.quiz.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.NOT_CREATED_PRODUCT;


@Component
public class ProductFactory {

    public final ProductRepository productRepository;
    public final InterviewService interviewService;

    public ProductFactory(ProductRepository productRepository, InterviewService interviewService){
        this.productRepository = productRepository;
        this.interviewService = interviewService;
    }

    public void createProduct(ProductRequest productRequest){

        int type = productRequest.getType();
        Long memberLevel = productRequest.getMemberLevel();
        Long mentorId = productRequest.getMentorId();
        LocalDateTime interviewDateTime = productRequest.getInterviewDateTime();

        Product product = null;

        if(type == 1){
            product = new ProductOne();
        }else if(type == 2){
            product = new ProductThree();
        }else if(type == 3){
            product = new ProductSix();
        }

        interviewService.createInterviews(product, memberLevel, mentorId, interviewDateTime);

        if(product == null){
            throw new CommonException(NOT_CREATED_PRODUCT);
        }

        productRepository.save(product);

    }



}
