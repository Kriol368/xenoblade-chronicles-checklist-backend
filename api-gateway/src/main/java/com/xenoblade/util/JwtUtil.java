package com.xenoblade.util;

import com.xenoblade.domain.service.JwtKeyRotationService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private static final long EXPIRATION_TIME = 86400000; // 24 hours
    private final JwtKeyRotationService keyService;

    @Autowired
    public JwtUtil(JwtKeyRotationService keyService) {
        this.keyService = keyService;
    }

    public Mono<String> generateToken(String username) {
        return Mono.fromCallable(() ->
                Jwts.builder()
                        .header().add("kid", keyService.getCurrentKeyId()).and()
                        .subject(username)
                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .signWith(keyService.getCurrentKey())
                        .compact()
        );
    }

    public Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                if (tryValidateWithKey(token, keyService.getCurrentKey())) {
                    return true;
                }

                SecretKey previousKey = keyService.getPreviousKey();
                if (previousKey != null) {
                    return tryValidateWithKey(token, previousKey);
                }

                return false;
            } catch (Exception e) {
                return false;
            }
        });
    }

    private boolean tryValidateWithKey(String token, SecretKey key) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Mono<String> extractUsername(String token) {
        return Mono.fromCallable(() -> {
            try {
                try {
                    return Jwts.parser()
                            .verifyWith(keyService.getCurrentKey())
                            .build()
                            .parseSignedClaims(token)
                            .getPayload()
                            .getSubject();
                } catch (Exception e) {
                    SecretKey previousKey = keyService.getPreviousKey();
                    if (previousKey != null) {
                        return Jwts.parser()
                                .verifyWith(previousKey)
                                .build()
                                .parseSignedClaims(token)
                                .getPayload()
                                .getSubject();
                    }
                    throw e;
                }
            } catch (Exception e) {
                throw new RuntimeException("Invalid token", e);
            }
        });
    }
}