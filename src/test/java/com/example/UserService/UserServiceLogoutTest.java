package com.example.UserService;

import beebooks.StartServer;
import beebooks.entities.User;
import beebooks.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class UserServiceLogoutTest {

    @Autowired
    private UserService userService;

    // Test 174: Đăng nhập và đăng xuất
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testLogout_Success() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));
        user.setEmail("user1@example.com");
        user.setAddress("123 Đường Láng, Hà Nội");
        userService.saveOrUpdate(user);

        // Giả lập đăng nhập
        User loadedUser = userService.loadUserByUsername("user1");
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                loadedUser, null, loadedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication(), "Người dùng đã đăng nhập");

        // Giả lập đăng xuất
        SecurityContextHolder.getContext().setAuthentication(null);

        assertNull(SecurityContextHolder.getContext().getAuthentication(), "Người dùng đã đăng xuất");
    }

    // Test 175: Đăng xuất khi chưa đăng nhập
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testLogout_WhenNotLoggedIn() {
        SecurityContextHolder.getContext().setAuthentication(null);

        assertNull(SecurityContextHolder.getContext().getAuthentication(), "Không có người dùng nào đăng nhập");

        // Giả lập đăng xuất
        SecurityContextHolder.getContext().setAuthentication(null);

        assertNull(SecurityContextHolder.getContext().getAuthentication(), "Vẫn không có người dùng nào đăng nhập sau khi đăng xuất");
    }
}