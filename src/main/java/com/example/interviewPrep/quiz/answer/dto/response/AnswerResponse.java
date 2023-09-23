package com.example.interviewPrep.quiz.answer.dto.response;

import com.example.interviewPrep.quiz.answer.domain.Answer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.example.interviewPrep.quiz.utils.DateFormat.customLocalDateTime;

@Getter
@NoArgsConstructor
public class AnswerResponse {

    private Long id;
    private Long questionId;
    private String content;
    private String createdDate;
    private String name;

    @Builder
    public AnswerResponse(Long questionId, String content, String name){
        Objects.requireNonNull(questionId, "questionId가 null입니다.");
        Objects.requireNonNull(content, "content가 null입니다.");
        Objects.requireNonNull(name, "name이 null입니다.");

        this.questionId = questionId;
        this.content = content;
        this.name = name;
    }

    public static AnswerResponse createAnswerResponse(Answer answer){
        return AnswerResponse.builder()
                .questionId(answer.getQuestion().getId())
                .content(answer.getContent())
                .name(answer.getMember().getName())
                .build();
    }
}
