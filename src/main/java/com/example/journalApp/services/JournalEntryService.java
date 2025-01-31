package com.example.journalApp.services;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.entity.User;
import com.example.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final UserService userService;

    @Autowired
    public JournalEntryService(JournalEntryRepository journalEntryRepository, UserService userService) {
        this.journalEntryRepository = journalEntryRepository;
        this.userService = userService;
    }

    public List<JournalEntry> getAllJournalEntries() {
        return journalEntryRepository.findAll();
    }

    public JournalEntry addJournalEntry(JournalEntry journalEntry, String username) {
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
        User user = userService.getUserByUserName(username);
        user.getJournalEntries().add(savedEntry);
        userService.addUser(user);
        return savedEntry;
    }

    public JournalEntry addJournalEntry(JournalEntry journalEntry) {
        return journalEntryRepository.save(journalEntry);
    }

    public void deleteJournalEntry(ObjectId id, String username) {
        User user = userService.getUserByUserName(username);
        boolean removed = user.getJournalEntries().removeIf(journalEntry -> journalEntry.getId().equals(id));
        if(!removed) {
            throw new IllegalStateException("Journal Entry not found");
        }
        userService.addUser(user);
        journalEntryRepository.deleteById(id);
    }

    public Optional<JournalEntry> getJournalEntryById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

}
