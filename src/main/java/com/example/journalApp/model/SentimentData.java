package com.example.journalApp.model;

import lombok.*;
import org.springframework.kafka.annotation.EnableKafka;

@Getter
@Setter
@Builder
public class SentimentData {
    public SentimentData() {}

    public SentimentData(String email, String sentiment) {
        this.email = email;
        this.sentiment = sentiment;
    }

    private String email;
    private String sentiment;
}
