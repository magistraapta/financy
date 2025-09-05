package com.financy.financy.auth.service;

import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.financy.financy.auth.entity.User;
import com.financy.financy.auth.repository.AuthRepository;

@Service
public class AuthService {
    
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        if (authRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameNotFoundException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return authRepository.save(user);
    }

    public Optional<User> login(User user) {
        Optional<User> existingUser = authRepository.findByUsername(user.getUsername());
        if (existingUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        if (!passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return existingUser;
    }
}
