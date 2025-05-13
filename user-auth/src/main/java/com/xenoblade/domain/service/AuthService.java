package com.xenoblade.domain.service;


import com.xenoblade.domain.model.*;
import com.xenoblade.domain.entity.User;
import com.xenoblade.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserViewModel register(RegisterViewModel registerVM) {
        if (userRepository.existsByEmail(registerVM.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setEmail(registerVM.getEmail());
        user.setName(registerVM.getName());
        user.setPassword(passwordEncoder.encode(registerVM.getPassword()));

        User savedUser = userRepository.save(user);
        return toViewModel(savedUser);
    }

    public UserViewModel login(LoginViewModel loginVM) {
        User user = userRepository.findByEmail(loginVM.getEmail())
                .orElse(null);

        if (user != null && passwordEncoder.matches(loginVM.getPassword(), user.getPassword())) {
            return toViewModel(user);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserViewModel toViewModel(User user) {
        UserViewModel vm = new UserViewModel();
        vm.setId(user.getId());
        vm.setEmail(user.getEmail());
        vm.setName(user.getName());
        return vm;
    }
}