package com.example.interviewPrep.quiz.answer.service;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import com.example.interviewPrep.quiz.answer.domain.AnswerComment;
import com.example.interviewPrep.quiz.answer.dto.request.CommentRequest;
import com.example.interviewPrep.quiz.answer.dto.response.CommentResponse;
import com.example.interviewPrep.quiz.answer.repository.AnswerRepository;
import com.example.interviewPrep.quiz.answer.repository.CommentRepository;
import com.example.interviewPrep.quiz.exception.advice.CommonException;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.member.repository.MemberRepository;
import com.example.interviewPrep.quiz.notification.service.NotificationService;
import com.example.interviewPrep.quiz.utils.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.interviewPrep.quiz.exception.advice.ErrorCode.*;
import static com.example.interviewPrep.quiz.utils.DateFormat.customLocalDateTime;


@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    public CommentService(CommentRepository commentRepository, AnswerRepository answerRepository, MemberRepository memberRepository, NotificationService notificationService) {
        this.commentRepository = commentRepository;
        this.answerRepository = answerRepository;
        this.memberRepository = memberRepository;
        this.notificationService = notificationService;
    }


    public Page<CommentResponse> findAnswerComment(Long id, Pageable pageable){

        Long memberId = JwtUtil.getMemberId();
        Page<AnswerComment> comments = commentRepository.findAnswerComment(id, pageable);

        if(comments.getContent().isEmpty()) {
            throw new CommonException(NOT_FOUND_COMMENT);
        }

        return makeCommentResponse(comments, memberId);
    }

    public Page<CommentResponse> makeCommentResponse(Page<AnswerComment> comments, Long memberId){
        return comments.map(comment-> CommentResponse.builder()
                            .id(comment.getId())
                            .comment(comment.getComment())
                            .memberName(comment.getMember().getName())
                            .createdDate(customLocalDateTime(comment.getCreatedDate()))
                            .modifiedDate(customLocalDateTime(comment.getModifiedDate()))
                            .modify(!comment.getCreatedDate().equals(comment.getModifiedDate()))
                            .myAnswer(comment.getMember().getId().equals(memberId))
                            .build());
    }


    public CommentResponse createComment(CommentRequest commentReq){

        Long memberId = JwtUtil.getMemberId();

        Member member = findMember(memberId);
        Answer answer = findAnswer(commentReq.getAnswerId());
        Member answerWriter = answer.getMember();

        answer.commentIncrease();
        answerRepository.save(answer);

        AnswerComment comment = AnswerComment.builder()
                                .answer(answer)
                                .answerWriter(answerWriter)
                                .member(member)
                                .comment(commentReq.getComment())
                                .build();

        AnswerComment savedComment = commentRepository.save(comment);
        notificationService.send(answerWriter, savedComment, "새로운 댓글이 추가되었습니다!");

        return CommentResponse.builder()
                              .id(comment.getId())
                              .createdDate(customLocalDateTime(comment.getCreatedDate()))
                              .memberName(comment.getMember().getName())
                              .build();
    }


    public void updateComment(CommentRequest commentRequest){
        AnswerComment comment = findComment(commentRequest.getId());
        comment.change(commentRequest.getComment());
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
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new CommonException(NOT_FOUND_ANSWER));
        answer.commentDecrease();
        answerRepository.save(answer);
    }

}

