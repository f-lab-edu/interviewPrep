package com.example.interviewPrep.quiz.question.dto;

import com.example.interviewPrep.quiz.question.domain.Question;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class QuestionRequest {

    private  Long id;
    private  String title;
    private  String type;
    private boolean status;

    @Builder
    public QuestionRequest(Long id, String title, String type, boolean status){
        Objects.requireNonNull(id, "id가 null입니다.");
        Objects.requireNonNull(title, "title이 null입니다.");
        Objects.requireNonNull(type, "type이 null입니다.");

        this.id = id;
        this.title = title;
        this.type = type;
        this.status = status;
    }

    public static Question createQuestionByQuestionRequest(QuestionRequest questionRequest){
        return Question.builder()
               .id(questionRequest.getId())
               .title(questionRequest.getTitle())
               .type(questionRequest.getType())
               .build();
    }

}
