package com.xenoblade.domain.service;

import io.jsonwebtoken.Jwts;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtKeyRotationService {
    private final Map<String, SecretKey> keys;
    private String currentKeyId = "current";
    private String previousKeyId = null;

    public JwtKeyRotationService(Map<String, SecretKey> initialKeys) {
        this.keys = new ConcurrentHashMap<>(initialKeys);
    }

    public SecretKey getCurrentKey() {
        return keys.get(currentKeyId);
    }

    public String getCurrentKeyId() {
        return currentKeyId;
    }

    public SecretKey getPreviousKey() {
        return previousKeyId != null ? keys.get(previousKeyId) : null;
    }

    @Scheduled(fixedRate = 604800000) // 7 days
    public Mono<Void> rotateKey() {
        return Mono.fromRunnable(() -> {
            String newKeyId = "key-" + System.currentTimeMillis();
            SecretKey newKey = Jwts.SIG.HS256.key().build();
            previousKeyId = currentKeyId;
            currentKeyId = newKeyId;

            keys.put(currentKeyId, newKey);
            keys.keySet().removeIf(keyId -> !keyId.equals(currentKeyId) && !keyId.equals(previousKeyId));
        });
    }
}