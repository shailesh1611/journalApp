package com.example.journalApp.services;

import com.example.journalApp.api.response.WeatherResponse;
import com.example.journalApp.cache.AppCache;
import com.example.journalApp.constant.Placeholder;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Autowired
    private AppCache appCache;

    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RedisService redisService;

    public WeatherResponse getWeather(String city) throws JsonProcessingException {
        if(redisService.get("WEATHER_OF_"+city, WeatherResponse.class) != null) {
            return redisService.get("WEATHER_OF_"+city, WeatherResponse.class);
        }
        else {
            String uri = appCache.getAppCache().get(AppCache.keys.WEATHER_API.toString()).replace(Placeholder.API_KEY, apiKey).replace(Placeholder.CITY, city);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(uri, HttpMethod.GET, null, WeatherResponse.class);
            if(response.getStatusCode() == HttpStatus.OK) {
                redisService.set("WEATHER_OF_"+city, response.getBody(), 300L);
            }
            return response.getBody();
        }
    }
}