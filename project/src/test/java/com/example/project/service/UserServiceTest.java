package com.example.project.service;

import com.example.project.models.User;
import com.example.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("plainpassword");
    }

    @Test
    public void saveUser_shouldSaveUser() {
        when(passwordEncoder.encode("plainpassword")).thenReturn("encodedpassword");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("plainpassword");

        userService.saveUser(user);

        verify(userRepository).save(argThat(savedUser ->
                savedUser.getEmail().equals("test@example.com") &&
                        savedUser.getPassword().equals("encodedpassword")
        ));
    }

    @Test
    void findByEmail_shouldReturnUser() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(user);

        User foundUser = userService.findByEmail(email);

        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }

    @Test
    void findByEmail_shouldReturnNullWhenNotFound() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        User foundUser = userService.findByEmail(email);

        assertNull(foundUser);
    }

    @Test
    void findUserById_shouldReturnUser() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        User foundUser = userService.findUserById(userId);

        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }

    @Test
    void findUserById_shouldReturnNullWhenNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        User foundUser = userService.findUserById(userId);

        assertNull(foundUser);
    }
}
