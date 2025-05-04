package com.xenoblade.config;

import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtKeyConfig {

    @Bean
    public SecretKey jwtKey() {
        return Jwts.SIG.HS256.key().build(); // generates a secure random key for HS256
    }
}