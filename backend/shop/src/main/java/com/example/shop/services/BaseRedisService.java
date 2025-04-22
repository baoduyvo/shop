package com.example.shop.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BaseRedisService {
    RedisTemplate<String, Object> redisTemplate;

    private static final long CACHE_EXPIRATION_TIME = 60l;

    public BaseRedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addToHash(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public Map<String, Object> getAllFromHash(String key) {
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();
        return hashOps.entries(key);
    }

}
