package com.example.interviewPrep.quiz.interview.dto.response;

import com.example.interviewPrep.quiz.interview.domain.Interview;
import lombok.Builder;

import java.util.Objects;

import static com.example.interviewPrep.quiz.utils.DateFormat.customLocalDateTime;

@Builder
public class InterviewResponse {

    Long id;
    String createdDate;

    @Builder
    public InterviewResponse(Long id, String createdDate) {
        Objects.requireNonNull(id, "id가 null입니다.");
        Objects.requireNonNull(createdDate, "createdDate가 null입니다.");

        this.id = id;
        this.createdDate = createdDate;
    }

    public static InterviewResponse createInterviewResponse(Interview interview) {
        return InterviewResponse.builder()
                .id(interview.getId())
                .createdDate(customLocalDateTime(interview.getCreatedDate()))
                .build();
    }
}
