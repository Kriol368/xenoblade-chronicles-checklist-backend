package com.xenoblade.controller;

import com.xenoblade.domain.model.*;
import com.xenoblade.domain.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserViewModel> register(
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String password) {

        RegisterViewModel registerVM = new RegisterViewModel();
        registerVM.setEmail(email);
        registerVM.setName(name);
        registerVM.setPassword(password);

        return ResponseEntity.ok(authService.register(registerVM));
    }

    @PostMapping("/login")
    public ResponseEntity<UserViewModel> login(
            @RequestParam String email,
            @RequestParam String password) {

        LoginViewModel loginVM = new LoginViewModel();
        loginVM.setEmail(email);
        loginVM.setPassword(password);

        UserViewModel user = authService.login(loginVM);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(401).build();
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        authService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}