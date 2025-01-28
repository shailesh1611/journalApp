package com.example.journalApp.services;

import com.example.journalApp.entity.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSources;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    @Disabled
    public void testGetUserByUsername() {
        assertNotNull(userService.getUserByUserName("Ram"));
    }


    @Disabled
    @ParameterizedTest
    @CsvSource({"2,3,5"})
    public void test(int a, int b, int expected) {
        assertEquals(expected, a+b, "failed for " + a +" and " + b);
    }
}
