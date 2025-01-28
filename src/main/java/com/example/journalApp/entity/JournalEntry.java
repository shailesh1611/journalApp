package com.example.journalApp.entity;

import com.example.journalApp.enums.Sentiment;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Setter
@Getter
@Document(collection =  "journal_entry")
public class JournalEntry {
    @Id
    private ObjectId id;
    @NonNull
    private String title;
    private String content;
    private Sentiment sentiment;
    private LocalDateTime date;
}
