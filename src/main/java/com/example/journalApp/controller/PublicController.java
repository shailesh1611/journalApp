package com.example.journalApp.controller;

import com.example.journalApp.entity.User;
import com.example.journalApp.services.UserService;
import com.example.journalApp.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Ok";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            User createdUser = userService.addUser(user);
            return new ResponseEntity<>(createdUser,HttpStatus.CREATED);
        }
        catch (Exception e) {
            log.error("Error while creating user",e.getCause());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword()));
            String token = jwtUtil.generateToken(user.getUserName());
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        catch (Exception e) {
            log.error("Error while logging in",e.getCause());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
