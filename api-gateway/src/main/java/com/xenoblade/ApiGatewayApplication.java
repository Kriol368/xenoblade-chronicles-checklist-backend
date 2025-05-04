package com.xenoblade;

import com.xenoblade.filter.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKey;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter(SecretKey jwtKey) {
        FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtFilter(jwtKey));
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }
}