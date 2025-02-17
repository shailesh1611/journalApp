package com.example.journalApp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final RedisTemplate<String,String> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String,String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> T get(String key, Class<T> entity) throws JsonProcessingException {
        Object o = redisTemplate.opsForValue().get(key);
        if(o != null) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(o.toString(), entity);
        }
        return null;
    }

    public <T> void set(String key, T value, long timeout) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String v = mapper.writeValueAsString(value);
        redisTemplate.opsForValue().set(key,v,timeout, TimeUnit.SECONDS);
    }
}
