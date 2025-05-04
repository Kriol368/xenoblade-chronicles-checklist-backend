package com.xenoblade.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

public class JwtUtils {

    public static String generateToken(String subject, SecretKey key, long expirationMillis) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(expirationMillis)))
                .signWith(key)
                .compact();
    }

    public static Claims validateToken(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}