package com.financy.financy.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.financy.financy.auth.entity.User;
import com.financy.financy.auth.repository.AuthRepository;
import com.financy.financy.auth.service.AuthService;

public class AuthServiceTests {
    @Mock
    private AuthRepository authRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        when(authRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(testUser.getPassword())).thenReturn("encodedPassword");
        when(authRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User registeredUser = authService.register(testUser);

        // Assert
        assertNotNull(registeredUser);
        assertEquals(testUser.getUsername(), registeredUser.getUsername());
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        // Arrange
        when(authRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authService.register(testUser));
    }

    @Test
    void testLogin_Success() {
        // Arrange
        User existingUser = new User();
        existingUser.setUsername(testUser.getUsername());
        existingUser.setPassword("encodedPassword");

        when(authRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(testUser.getPassword(), existingUser.getPassword())).thenReturn(true);

        // Act
        Optional<User> loggedInUser = authService.login(testUser);

        // Assert
        assertNotNull(loggedInUser);
        assertEquals(testUser.getUsername(), loggedInUser.get().getUsername());
    }

    @Test
    void testLogin_UserNotFound() {
        // Arrange
        when(authRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authService.login(testUser));
    }

    @Test
    void testLogin_InvalidPassword() {
        // Arrange
        User existingUser = new User();
        existingUser.setUsername(testUser.getUsername());
        existingUser.setPassword("encodedPassword");

        when(authRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(testUser.getPassword(), existingUser.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(testUser));
    }
}
