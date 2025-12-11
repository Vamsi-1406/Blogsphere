package com.blogsphere.service;

import com.blogsphere.model.User;
import com.blogsphere.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setFullName("Test User");
        testUser.setBio("Test bio");
    }

    @Test
    void registerUser_Success() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User registeredUser = userService.registerUser(testUser);

        assertNotNull(registeredUser);
        assertEquals(testUser.getUsername(), registeredUser.getUsername());
        assertEquals(testUser.getEmail(), registeredUser.getEmail());
        assertEquals("encodedPassword", registeredUser.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_UsernameExists_ThrowsException() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.registerUser(testUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_Success() {
        User updatedUser = new User();
        updatedUser.setFullName("Updated Name");
        updatedUser.setBio("Updated bio");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        assertEquals(updatedUser.getFullName(), result.getFullName());
        assertEquals(updatedUser.getBio(), result.getBio());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_UserNotFound_ThrowsException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(1L, new User()));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void followUser_Success() {
        User follower = new User();
        follower.setId(1L);
        User followed = new User();
        followed.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(follower));
        when(userRepository.findById(2L)).thenReturn(Optional.of(followed));

        userService.followUser(1L, 2L);

        assertTrue(follower.getFollowing().contains(followed));
        assertTrue(followed.getFollowers().contains(follower));
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void unfollowUser_Success() {
        User follower = new User();
        follower.setId(1L);
        User followed = new User();
        followed.setId(2L);
        follower.getFollowing().add(followed);
        followed.getFollowers().add(follower);

        when(userRepository.findById(1L)).thenReturn(Optional.of(follower));
        when(userRepository.findById(2L)).thenReturn(Optional.of(followed));

        userService.unfollowUser(1L, 2L);

        assertFalse(follower.getFollowing().contains(followed));
        assertFalse(followed.getFollowers().contains(follower));
        verify(userRepository, times(2)).save(any(User.class));
    }
} 