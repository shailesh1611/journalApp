package com.example.journalApp.repository;

import com.example.journalApp.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserRepositoryImplTests {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Test
    @Disabled
    public void testGetUserForSA() {
        List<User> userForSA = userRepository.getUserForSA();
        Assertions.assertNotEquals(0,userForSA);
    }
}
