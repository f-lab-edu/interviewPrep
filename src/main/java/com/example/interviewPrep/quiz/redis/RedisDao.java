package com.example.interviewPrep.quiz.redis;

import com.example.interviewPrep.quiz.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class RedisDao {

    private RedisTemplate<String, String> redisTemplate;
    private RedisTemplate<String, List<Notification>> redisTemplateForNotification;
    public RedisDao(RedisTemplate<String, String> redisTemplate, RedisTemplate<String, List<Notification>> redisTemplateForNotification) {
        this.redisTemplate = redisTemplate;
        this.redisTemplateForNotification = redisTemplateForNotification;
   }

    public void setValues(String key, String data) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    public String getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }


    public void setValuesForNotification(String key) {
        ValueOperations<String, List<Notification>> values = redisTemplateForNotification.opsForValue();
        values.set(key, new ArrayList<>());
    }

    public List<Notification> getValuesForNotification(String key) {
        ValueOperations<String, List<Notification>> values = redisTemplateForNotification.opsForValue();
        return values.get(key);
    }

    public void updateValuesForNotification(String key, List<Notification> notifications) {
        ValueOperations<String, List<Notification>> values = redisTemplateForNotification.opsForValue();
        values.set(key, notifications);
    }
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}
