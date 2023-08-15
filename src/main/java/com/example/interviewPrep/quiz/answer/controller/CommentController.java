package com.example.interviewPrep.quiz.answer.controller;

import com.example.interviewPrep.quiz.answer.dto.request.CommentRequest;
import com.example.interviewPrep.quiz.answer.dto.response.CommentResponse;
import com.example.interviewPrep.quiz.answer.service.CommentService;
import com.example.interviewPrep.quiz.response.ResultResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @GetMapping("/api/v1/answer/comment/{id}")
    public ResultResponse<Page<CommentResponse>> findAnswerComment(@PathVariable Long id, @PageableDefault(size=10) Pageable pageable){

        return ResultResponse.success(commentService.findAnswerComment(id, pageable));
    }

    @PostMapping("/api/v1/answer/comment")
    public ResultResponse<CommentResponse> createAnswerComment(@RequestBody @Valid CommentRequest commentRequest){
        return ResultResponse.success(commentService.createComment(commentRequest));
    }

    @PutMapping("/api/v1/answer/comment")
    public ResultResponse<?> updateAnswerComment(@RequestBody @Valid CommentRequest commentRequest){
        commentService.updateComment(commentRequest);
        return ResultResponse.success();
    }

    @DeleteMapping("/api/v1/answer/comment/{id}")
    public ResultResponse<?> deleteAnswerComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResultResponse.success();
    }




}