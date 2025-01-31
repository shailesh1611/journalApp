package com.example.journalApp.controller;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.entity.User;
import com.example.journalApp.services.JournalEntryService;
import com.example.journalApp.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("journal")
public class JournalEntryController {
    private final JournalEntryService journalEntryService;
    private final UserService userService;

    @Autowired
    public JournalEntryController(JournalEntryService journalEntryService, UserService userService) {
        this.userService = userService;
        this.journalEntryService = journalEntryService;
    }

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUserName(auth.getName());
        if(user != null) {
            List<JournalEntry> allJournalEntries = user.getJournalEntries();
            if(!allJournalEntries.isEmpty()) {
                return new ResponseEntity<>(allJournalEntries, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<JournalEntry> addJournalEntryOfUser(@RequestBody JournalEntry journalEntry) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            JournalEntry newEntry = journalEntryService.addJournalEntry(journalEntry, auth.getName());
            return new ResponseEntity<>(newEntry, HttpStatus.CREATED);
        }
        catch (Exception e) {
            throw(new RuntimeException(e.getMessage()));
        }
    }

    @DeleteMapping("/id/{myId}")
    @Transactional
    public ResponseEntity<?> deleteJournalEntry(@PathVariable ObjectId myId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            journalEntryService.deleteJournalEntry(myId,auth.getName());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            throw(new RuntimeException(e.getMessage()));
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUserName(auth.getName());
        boolean present = user.getJournalEntries().stream().anyMatch(journalEntry -> journalEntry.getId().equals(myId));
        if(!present) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<JournalEntry> journalEntryById = journalEntryService.getJournalEntryById(myId);
        return new ResponseEntity<>(journalEntryById.get(),HttpStatus.OK);
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<JournalEntry> updateJournalEntry(@PathVariable ObjectId myId, @RequestBody JournalEntry journalEntry) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUserName(auth.getName());
        boolean isJournalEntryPresent = user.getJournalEntries().stream().anyMatch(journalEntry1 -> journalEntry1.getId().equals(myId));
        if(!isJournalEntryPresent) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        JournalEntry oldJournalEntry = journalEntryService.getJournalEntryById(myId).orElse(null);
        if(oldJournalEntry != null) {
            oldJournalEntry.setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().isEmpty() ? journalEntry.getTitle() : oldJournalEntry.getTitle());
            oldJournalEntry.setContent(journalEntry.getContent() != null && !journalEntry.getContent().isEmpty() ? journalEntry.getContent() : oldJournalEntry.getContent());
            JournalEntry updatedJournalEntry = journalEntryService.addJournalEntry(oldJournalEntry);
            return new ResponseEntity<>(updatedJournalEntry,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
