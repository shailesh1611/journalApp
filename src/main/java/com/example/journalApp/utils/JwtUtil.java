package com.example.journalApp.utils;

import com.example.journalApp.services.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private UserDetailsServiceImpl userDetail;

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    @Autowired
    public JwtUtil(UserDetailsServiceImpl userDetail) {
        this.userDetail = userDetail;
    }

    public String generateToken(String userName) {
        Map<String, Object> claim = new HashMap<>();
        return createToken(userName, claim);
    }

    private String createToken(String subject, Map<String, Object> claim) {
        return Jwts.builder()
                .claims(claim)
                .subject(subject)
                .header().add("typ", "JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 Minutes
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey()) // Provide the secret key for verifying the signature
                .build()
                .parseSignedClaims(token); // Parses the token and checks the signature
            return true; // If no exceptions, the token is valid
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT : " + e.getMessage());
        }
    }

    public UserDetails getUser(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey()) // Same secret key used for validation
                .build()
                .parseSignedClaims(token) // Parse the token
                .getPayload(); // Extract the payload (claims)
        return userDetail.loadUserByUsername(claims.getSubject()); // The "sub" claim contains the username
    }
}
