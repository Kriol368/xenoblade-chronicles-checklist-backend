package com.xenoblade.config;

import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JwtConfig {

    @Bean
    public Map<String, SecretKey> jwtKeys() {
        Map<String, SecretKey> keys = new HashMap<>();
        keys.put("current", Jwts.SIG.HS256.key().build());
        return keys;
    }
}