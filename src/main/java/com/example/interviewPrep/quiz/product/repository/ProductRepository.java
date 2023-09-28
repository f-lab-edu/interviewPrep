package com.example.interviewPrep.quiz.product.repository;


import com.example.interviewPrep.quiz.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
