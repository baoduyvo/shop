package com.example.rediscache.configurations;

import com.example.rediscache.entities.Cart;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

//    @Bean
//    public RedisTemplate<String, Cart> redisTemplate(org.springframework.data.redis.core.RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, Cart> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
//
//        // Set serializers for key and value
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//
//        return template;
//    }
}