package com.init.setting.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP,basePackages = {"com.init.setting.auth.jwt.refresh.repository"})
@Slf4j
public class RedisConfig {
    //Redis는 TTL이 만료되는 시점에 보조 인덱스(@Indexed)는 삭제하지 않음.
    //Redis의 Key Space Notifications 기능을 활용하면, TTL이 만료되는 시점에 이벤트를 감지하고, 보조인덱스를 삭제할 수 있음


    @Value("${spring.jwt.redis.host}")
    private String host ;

    @Value("${spring.jwt.redis.port}")
    private int port ;


    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(host, port);
        return lettuceConnectionFactory;
    }


    // serializer 설정으로 redis-cli를 통해 직접 데이터를 조회할 수 있도록 설정
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }
}
