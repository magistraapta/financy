package com.financy.financy.auth;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.financy.financy.auth.config.SecurityConfig;
import com.financy.financy.auth.controller.AuthController;
import com.financy.financy.auth.entity.User;
import com.financy.financy.auth.service.AuthService;


@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private User testUser;

    @BeforeEach
    void setUp() {
        String username = "testuser";
        String password = "testuser123";

        testUser = new User();
        testUser.setUsername(username);
        testUser.setPassword(password);

        when(authService.register(testUser)).thenReturn(testUser);
        when(authService.login(testUser)).thenReturn(Optional.of(testUser));
    }

    @Test
    void testRegisterUser() throws Exception {
        mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testUser)))
            .andExpect(status().isCreated());
    }

    @Test
    void testLoginUser() throws Exception {
        mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testUser)))
            .andExpect(status().isOk());
    }
}
