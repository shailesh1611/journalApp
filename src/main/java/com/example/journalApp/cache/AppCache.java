package com.example.journalApp.cache;

import com.example.journalApp.entity.ConfigJournalApp;
import com.example.journalApp.services.ConfigJournalAppService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
@Setter
public class AppCache {
    public enum keys {
        WEATHER_API
    }

    private final Map<String,String> appCache = new HashMap<>();
    private ConfigJournalAppService configJournalAppService;

    @Autowired
    public AppCache(ConfigJournalAppService configJournalAppService) {
        this.configJournalAppService = configJournalAppService;
    }

    @PostConstruct
    public void init() {
        List<ConfigJournalApp> configJournalAppList = configJournalAppService.getAllConfigJournalApp();
        for(ConfigJournalApp config : configJournalAppList) {
            appCache.put(config.getKey(),config.getValue());
        }
    }
}
