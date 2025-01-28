package com.example.journalApp.scheduler;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.entity.User;
import com.example.journalApp.enums.Sentiment;
import com.example.journalApp.model.SentimentData;
import com.example.journalApp.repository.UserRepositoryImpl;
import com.example.journalApp.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SentimentAnalysisScheduler {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    @Scheduled(cron="0 0 9 ? * SUN")
    public void sendEmailsToSentimentUsers() {
        /* Fetch users for sentiment analysis */
        List<User> userForSA = userRepository.getUserForSA();

        /* Sending email to users who opted for sentiment analysis if sentiments found */
        for (User user : userForSA) {
            Map<Sentiment,Integer> sentimentCnt = new HashMap<>();

            /* Get last 7 days user sentiment as list */
            List<Sentiment> sentiments = user.getJournalEntries()
                    .stream()
                    .filter(journalEntry -> journalEntry.getDate().isAfter(LocalDateTime.now().minusDays(7)))
                    .map(JournalEntry::getSentiment)
                    .toList();

            /* Counting the number of each sentiment */
            for(Sentiment sentiment : sentiments) {
                if(sentiment != null) {
                    sentimentCnt.put(sentiment,sentimentCnt.getOrDefault(sentiment,0)+1);
                }
            }

            /* Logic for getting sentiment which comes max number of times */
            String userLastWeekSentiment = null;
            int maxSentimentCnt = 0;

            for(Map.Entry<Sentiment, Integer> entry : sentimentCnt.entrySet()) {
                if(maxSentimentCnt < entry.getValue()) {
                    maxSentimentCnt = entry.getValue();
                    userLastWeekSentiment = entry.getKey().toString();
                }
            }

            /* if last week sentiment is found , then sending email to them */
            if(userLastWeekSentiment != null) {
                try {
                    SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment(userLastWeekSentiment).build();
                    kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), sentimentData);
                }
                catch (Exception e) {
                    emailService.sendEmail(user.getEmail(), "Last 7 Days Sentiment Analysis", userLastWeekSentiment);
                }
            }
        }
    }

}
