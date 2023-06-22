package com.example.interviewPrep.quiz.dto;

import java.time.LocalDateTime;


import com.example.interviewPrep.quiz.notification.domain.Notification;
import com.example.interviewPrep.quiz.utils.LocalDateTimeToArray;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationResponse {
    /**
     * 알림 id
     */
    private Long id;

    /**
     * 알림 내용
     */
    private String content;

    /**
     * 알림 클릭 시 이동할 url
     */
    private String url;

    /**
     * 알림이 생성된 날짜(몇일 전 계산 위함)
     */
    private Integer[] createdDate;

    /**
     * 알림 읽음 여부
     */
    private boolean read;

    @Builder
    public NotificationResponse(Long id, String content, String url, LocalDateTime createdDate, boolean read) {
        this.id = id;
        this.content = content;
        this.url = url;
        this.createdDate = LocalDateTimeToArray.convert(createdDate);
        this.read = read;
    }

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .url(notification.getUrl())
                .createdDate(notification.getCreatedDate())
                .read(notification.isRead())
                .build();
    }
}