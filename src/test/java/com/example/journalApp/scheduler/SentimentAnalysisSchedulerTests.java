package com.example.journalApp.scheduler;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@Disabled
public class SentimentAnalysisSchedulerTests {
    @Autowired
    SentimentAnalysisScheduler sentimentAnalysisScheduler;

    @Test
    public void testSendEmailsToSentimentUsers() {
        sentimentAnalysisScheduler.sendEmailsToSentimentUsers();
    }
}
