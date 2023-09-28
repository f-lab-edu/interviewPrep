package com.example.interviewPrep.quiz.Product.controller;


import com.example.interviewPrep.quiz.config.CustomAuthenticationEntryPoint;
import com.example.interviewPrep.quiz.filter.JwtAuthenticationFilter;
import com.example.interviewPrep.quiz.member.service.CustomOAuth2UserService;
import com.example.interviewPrep.quiz.product.controller.ProductController;
import com.example.interviewPrep.quiz.product.dto.request.ProductRequest;
import com.example.interviewPrep.quiz.product.service.ProductService;
import com.example.interviewPrep.quiz.security.WithMockCustomOAuth2Account;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockCustomOAuth2Account()
public class ProductWebControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Autowired
    ObjectMapper objectMapper;
    String validJsonRequest;

    @BeforeEach
    void setUp() throws Exception{

        int type = 1;
        Long memberLevel = 1L;
        Long mentorId = 1L;
        LocalDateTime dateTime = LocalDateTime.now();

        ProductRequest validProductRequest = ProductRequest.builder()
                                            .type(type)
                                            .memberLevel(memberLevel)
                                            .mentorId(mentorId)
                                            .dateTime(dateTime)
                                            .build();

        validJsonRequest = objectMapper.writeValueAsString(validProductRequest);
    }



    @Test
    @DisplayName("유효한 답안 생성")
    void createValidProduct() throws Exception {

        mockMvc.perform(post("/api/v1/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(productService).createProduct(any(ProductRequest.class));
    }

}
