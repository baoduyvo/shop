package com.example.shop.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BaseRedisService {
    RedisTemplate<String, Object> redisTemplate;

    private static final long CACHE_EXPIRATION_TIME = 60l;

    public BaseRedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveDataWithKey(String key, Object value) {
        saveDataWithKey(key, value, CACHE_EXPIRATION_TIME);
    }

    public void saveDataWithKey(String key, Object value, long timeoutInMinutes) {
        try {
            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            om.writerWithDefaultPrettyPrinter().writeValueAsString(value);

            redisTemplate.opsForValue().set(key, value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getDataWithKey(String key, Class<?> clazz) {
        String serializedValue = (String) redisTemplate.opsForValue().get(key);
        if (serializedValue != null) {
            try {
                ObjectMapper om = new ObjectMapper();
                om.registerModule(new JavaTimeModule());
                return om.readValue(serializedValue, clazz);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error deserializing value from Redis", e);
            }
        }
        return null;
    }

    public List<Object> getListElements(String key) {
        List<Object> result = redisTemplate.opsForList().range(key, 0, -1);
        return result != null ? result : List.of();
    }

    public void addToList(String key, Object value) {
        addToList(key, value, false);
    }

    public void addToList(String key, Object value, boolean pushToFront) {
        try {
            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            String serializedValue = om.writeValueAsString(value);

            if (!pushToFront) {
                redisTemplate.opsForList().leftPush(key, serializedValue);
            } else {
                redisTemplate.opsForList().rightPush(key, serializedValue);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing value to Redis list", e);
        }
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
