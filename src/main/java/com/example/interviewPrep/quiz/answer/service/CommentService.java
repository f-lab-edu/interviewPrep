package com.example.interviewPrep.quiz.answer.service;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.answer.domain.AnswerComment;
import com.example.interviewPrep.quiz.answer.dto.req.CommentReq;
import com.example.interviewPrep.quiz.answer.dto.res.CommentRes;
import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.answer.repository.CommentRepository;
import com.example.interviewPrep.quiz.dto.CreateDto;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;
import static com.example.interviewPrep.quiz.utils.DateFormat.customLocalDateTime;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;


    public Page<CommentRes> findAnswerComment(Long id, Pageable pageable){

        Long memberId = JwtUtil.getMemberId();
        Page<AnswerComment> comments = commentRepository.findByComment(id, pageable);
        if(comments.getContent().isEmpty()) throw new CommonException(NOT_FOUND_COMMENT);
        return makeCommentRes(comments, memberId);
    }

    public Page<CommentRes> makeCommentRes(Page<AnswerComment> comments, Long memberId){
        return comments.map(comment-> CommentRes.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .memberName(comment.getMember().getName())
                .createdDate(customLocalDateTime(comment.getCreatedDate()))
                .modifiedDate(customLocalDateTime(comment.getModifiedDate()))
                .modify(!comment.getCreatedDate().equals(comment.getModifiedDate()))
                .myAnswer(comment.getMember().getId().equals(memberId))
                .build());
    }


    public CreateDto createComment(CommentReq commentReq){

        Long memberId = JwtUtil.getMemberId();

        Member member = findMember(memberId);
        Answer answer = findAnswer(commentReq.getAnswerId());

        answer.commentIncrease();
        answerRepository.save(answer);

        AnswerComment comment = AnswerComment.builder()
                .answer(answer)
                .member(member)
                .comment(commentReq.getComment())
                .build();

        commentRepository.save(comment);

        return CreateDto.builder()
                .id(comment.getId())
                .createDate(customLocalDateTime(comment.getCreatedDate()))
                .name(comment.getMember().getName())
                .build();
    }


    public void updateComment(CommentReq commentReq){
        AnswerComment comment = findComment(commentReq.getId());
        comment.change(commentReq.getComment());
        commentRepository.save(comment);
    }

    public void deleteComment(Long id){
        AnswerComment comment = findComment(id);
        decreaseComment(comment.getAnswer().getId());
        commentRepository.delete(comment);
    }

    public AnswerComment findComment(Long id){
        return commentRepository.findById(id).orElseThrow(() -> new CommonException(NOT_FOUND_COMMENT));
    }

    public Answer findAnswer(Long id){
        return answerRepository.findById(id).orElseThrow(() -> new CommonException(NOT_FOUND_ANSWER));
    }

    public Member findMember(Long id){
        return memberRepository.findById(id).orElseThrow(()-> new CommonException(NOT_FOUND_MEMBER));
    }

    public void decreaseComment(Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() ->
                new CommonException(NOT_FOUND_ANSWER));
        answer.commentDecrease();
        answerRepository.save(answer);
    }

}

