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
    public AnswerResponse(Long id, Long questionId, String content, String createdDate, String name){
        Objects.requireNonNull(id, "id가 null입니다.");
        Objects.requireNonNull(questionId, "questionId가 null입니다.");
        Objects.requireNonNull(content, "content가 null입니다.");
        Objects.requireNonNull(createdDate, "createdDate가 null입니다.");
        Objects.requireNonNull(name, "name이 null입니다.");

        this.id = id;
        this.questionId = questionId;
        this.content = content;
        this.createdDate = createdDate;
        this.name = name;
    }

    public static AnswerResponse createAnswerResponse(Answer answer){
        return AnswerResponse.builder()
                .id(answer.getId())
                .createdDate(customLocalDateTime(answer.getCreatedDate()))
                .name(answer.getMember().getName())
                .build();
    }
}
