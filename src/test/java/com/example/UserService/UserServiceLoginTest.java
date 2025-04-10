package com.example.UserService;

import beebooks.StartServer;
import beebooks.entities.User;
import beebooks.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class UserServiceLoginTest {

    @Autowired
    private UserService userService;

    // Test 171: Đăng nhập với username hợp lệ
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testLogin_Success() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));
        user.setEmail("user1@example.com");
        user.setAddress("123 Đường Láng, Hà Nội");
        userService.saveOrUpdate(user);

        User loadedUser = userService.loadUserByUsername("user1");

        assertNotNull(loadedUser, "Người dùng phải được tìm thấy với username 'user1'");
        assertEquals("user1", loadedUser.getUsername(), "Tên người dùng phải khớp");
        assertEquals("user1@example.com", loadedUser.getEmail(), "Email phải khớp");
    }

    // Test 172: Đăng nhập với username không tồn tại
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testLogin_Fail_NonExistentUsername() {
        User loadedUser = userService.loadUserByUsername("nonexistent");

        assertNull(loadedUser, "Người dùng không tồn tại, phải trả về null");
    }

    // Test 173: Đăng nhập với username null
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testLogin_Fail_NullUsername() {
        User loadedUser = userService.loadUserByUsername(null);

        assertNull(loadedUser, "Đăng nhập với username null phải trả về null");
    }
}