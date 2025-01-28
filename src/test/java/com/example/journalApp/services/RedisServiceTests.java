package com.example.journalApp.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@Disabled
public class RedisServiceTests {
    @Autowired
    RedisTemplate redisTemplate;


    @Test
    public void test() {
        redisTemplate.opsForValue().set("price","1000",20, TimeUnit.SECONDS);
        System.out.println(redisTemplate.opsForValue().get("price"));
    }
}
