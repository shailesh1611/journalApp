package com.example.journalApp.controller;

import com.example.journalApp.api.response.WeatherResponse;
import com.example.journalApp.entity.User;
import com.example.journalApp.services.UserService;
import com.example.journalApp.services.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User oldUser = userService.getUserByUserName(authentication.getName());
        oldUser.setUserName(user.getUserName());
        oldUser.setPassword(user.getPassword());
        User updatedUser = userService.addUser(oldUser);
        return new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.deleteUserByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> gretting() throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weather = weatherService.getWeather("New Delhi");
        String greeting = "";
        if(weather != null) {
            greeting = ", Weather feels like " + weather.getCurrent().getFeelsLike() + "°C";
        }
        return new ResponseEntity<>("Hi "+authentication.getName() + greeting,HttpStatus.OK);
    }
}
