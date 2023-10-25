package com.example.interviewPrep.quiz.Notification.domain;

import com.example.interviewPrep.quiz.answer.domain.AnswerComment;
import com.example.interviewPrep.quiz.member.domain.Member;
import com.example.interviewPrep.quiz.notification.domain.Notification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class NotificationTest {

    @Mock
    Member receiver;

    @Mock
    AnswerComment comment;

    @Test
    void createNotification(){
        Notification notification = Notification.builder()
                                                .receiver(receiver)
                                                .comment(comment)
                                                .content("새 알림입니다.")
                                                .isRead(false)
                                                .build();

        assertThat(notification.getReceiver()).isEqualTo(receiver);
        assertThat(notification.getComment()).isEqualTo(comment);
        assertThat(notification.getContent()).isEqualTo("새 알림입니다.");
        assertThat(notification.isRead()).isEqualTo(false);
    }
}
