package com.kiran.collaborativeprojectandtaskmanagementsystem.security;

import com.kiran.collaborativeprojectandtaskmanagementsystem.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final String secret;
    private final long expiration;

    public JwtService(JwtProperties jwtProperties){
        this.secret = jwtProperties.getSecret();
        this.expiration = jwtProperties.getExpiration();
    }

    public Key getKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }

    private Claims extractClaims(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token){
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String username, String token){
        return ((username.equals(extractUsername(token))) &&
                (!extractClaims(token).getExpiration().before(new Date()))
        );
    }

}
