package com.example.journalApp.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    @Disabled
    public void testSendEmail() {
        emailService.sendEmail("Shaileshsingh8077@gmail.com","Test Mail", "This is a test mail");
    }
}
