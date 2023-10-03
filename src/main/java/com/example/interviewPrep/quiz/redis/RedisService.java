package com.example.interviewPrep.quiz.redis;

import java.time.Duration;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
  private final RedisDao redisDao;
  public RedisService(RedisDao redisDao){
    this.redisDao = redisDao;
  }

  public void deleteMemberOnRedis(String memberId){
    if(redisDao.getValues(memberId) !=null){
      redisDao.deleteValues(memberId);
    }
  }


  public void setRefreshTokenOnRedis(String id, String refreshToken){
    redisDao.setValues(id, refreshToken, Duration.ofDays(7));
  }


  public boolean isNotValidRefreshToken(String token, String memberId){
    String refreshToken = redisDao.getValues(memberId);
    return refreshToken == null || !refreshToken.equals(token);
  }

  public void setLogOutWithAccessTokenOnRedis(String accessToken, Long expiration){
    redisDao.setValues(accessToken, "logout", Duration.ofMillis(expiration));
  }

}
