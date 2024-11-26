package com.linkmoa.source.auth.jwt.service;



import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@RedisHash
public class RefreshTokenService {

    private final RedisTemplate<String,Object> redisTemplate;


    public void saveRefreshToken(String email, String refreshToken){

        //set() 메서드를 사용하면, 동일한 키가 있을 경우 기존 값을 덮어씀
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(email,refreshToken,86400, TimeUnit.MILLISECONDS);// 24시간
    }


    public String getRefreshToken(String email) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(email) == null) {
            return "false";
        }
        return (String) values.get(email);
    }

}
