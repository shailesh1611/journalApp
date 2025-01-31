package com.example.journalApp.services;

import com.example.journalApp.entity.ConfigJournalApp;
import com.example.journalApp.repository.ConfigJournalAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigJournalAppService {
    private final ConfigJournalAppRepository configJournalAppRepository;

    @Autowired
    public ConfigJournalAppService(ConfigJournalAppRepository configJournalAppRepository) {
        this.configJournalAppRepository = configJournalAppRepository;
    }

    public List<ConfigJournalApp> getAllConfigJournalApp() {
        return configJournalAppRepository.findAll();
    }
}
