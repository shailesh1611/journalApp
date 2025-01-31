package com.example.journalApp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("config_journal_app")
public class ConfigJournalApp {
    private String key;
    private String value;
}
