package com.example.interviewPrep.quiz.answer.controller;

import com.example.interviewPrep.quiz.answer.dto.req.CommentReq;
import com.example.interviewPrep.quiz.answer.service.CommentService;
import com.example.interviewPrep.quiz.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/answer")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comment/{id}")
    public ResultResponse<?> findAnswerComment(@PathVariable Long id, @PageableDefault(size=10) Pageable pageable){

        return ResultResponse.success(commentService.findAnswerComment(id, pageable));
    }

    @PostMapping("/comment")
    public ResultResponse<?> createAnswerComment(@RequestBody @Valid CommentReq commentReq){
        return ResultResponse.success(commentService.createComment(commentReq));
    }

    @PutMapping("/comment")
    public ResultResponse<?> updateAnswerComment(@RequestBody @Valid CommentReq commentReq){
        commentService.updateComment(commentReq);
        return ResultResponse.success();
    }

    @DeleteMapping("/comment/{id}")
    public ResultResponse<?> deleteAnswerComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResultResponse.success();
    }




}