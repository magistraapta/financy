package com.financy.financy.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;

import com.financy.financy.auth.entity.User;
import com.financy.financy.auth.repository.AuthRepository;

import jakarta.transaction.Transactional;

@DataJpaTest
public class AuthRepositoryTests {
    
    @Autowired
    private AuthRepository authRepository;
    
    @Test
    @Transactional
    @Rollback
    void testCreateUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testuser123");

        User savedUser = authRepository.save(user);

        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getPassword(), savedUser.getPassword());
    }

    @Test
    @Transactional
    @Rollback
    void testFindByUsername() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testuser123");

        authRepository.save(user);

        Optional<User> foundUser = authRepository.findByUsername(user.getUsername());

        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.get().getUsername());
        assertEquals(user.getPassword(), foundUser.get().getPassword());
    }

    @Test
    @Transactional
    @Rollback
    void testFindByUsernameNotFound() {
        Optional<User> foundUser = authRepository.findByUsername("nonexistentuser");

        assertNotNull(foundUser);
        assertEquals(Optional.empty(), foundUser);
    }

    @Test
    @Transactional
    @Rollback
    void testUsernameExists() {
        // First user creation
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testuser123");
        authRepository.save(user);

        // Verify username exists
        assertTrue(authRepository.existsByUsername("testuser"));
        
        // Try to create another user with the same username
        User user2 = new User();
        user2.setUsername("testuser");
        user2.setPassword("differentpassword");

        // This should throw an exception because the username already exists
        assertThrows(DataIntegrityViolationException.class, () -> {
            authRepository.save(user2);
        });
    }
}
