package com.xenoblade.controller;

import com.xenoblade.util.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TestController {
    @Resource
    private JwtUtil jwtUtil;

    @GetMapping("/test")
    public Mono<String> test() {
        return jwtUtil.generateToken("admin");
    }
}