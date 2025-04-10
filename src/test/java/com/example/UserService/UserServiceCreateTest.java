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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class UserServiceCreateTest {

    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 1: Tạo người dùng hợp lệ bằng RegisterUser
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testCreateUser_Success_WithRegisterUser() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("password123");
        user.setEmail("user1@example.com");
        user.setAddress("123 Đường Láng, Hà Nội"); // Đảm bảo cung cấp giá trị cho address

        boolean result = userService.RegisterUser(user);

        assertTrue(result, "Đăng ký người dùng phải thành công");
        assertNotNull(user.getId(), "ID người dùng không được để trống");

        User userFromDb = entityManager.find(User.class, user.getId());
        assertNotNull(userFromDb, "Người dùng phải được lưu vào DB");
        assertEquals("user1", userFromDb.getUsername(), "Tên người dùng không đúng");
        assertEquals("user1@example.com", userFromDb.getEmail(), "Email không đúng");

        assertTrue(new BCryptPasswordEncoder().matches("password123", userFromDb.getPassword()), "Mật khẩu phải được mã hóa và khớp");
    }

    // Test 2: Tạo người dùng với email đã tồn tại (thất bại)
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testCreateUser_Fail_EmailExists_WithRegisterUser() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password123");
        user1.setEmail("user1@example.com");
        user1.setAddress("123 Đường Láng, Hà Nội");
        userService.RegisterUser(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("password456");
        user2.setEmail("user1@example.com"); // Email đã tồn tại
        user2.setAddress("456 Nguyễn Trãi, Hà Nội");

        boolean result = userService.RegisterUser(user2);

        assertFalse(result, "Đăng ký người dùng phải thất bại vì email đã tồn tại");
    }

    // Test 3: Tạo người dùng hợp lệ bằng saveOrUpdate
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testCreateUser_Success_WithSaveOrUpdate() {
        User user = new User();
        user.setUsername("user3");
        user.setPassword(new BCryptPasswordEncoder().encode("password789"));
        user.setEmail("user3@example.com");
        user.setAddress("789 Lê Lợi, Hà Nội");
        user.setPhone("0987654321");

        User savedUser = userService.saveOrUpdate(user);

        assertNotNull(savedUser.getId(), "ID người dùng không được để trống");
        assertEquals("user3", savedUser.getUsername(), "Tên người dùng không đúng");
    }

    // Test 4: Tạo người dùng với username null (thất bại)
    @Test
    @Order(4)
    @Transactional
    @Rollback
    public void testCreateUser_Fail_UsernameIsNull() {
        User user = new User();
        user.setUsername(null); // Username là nullable = false
        user.setPassword("password123");
        user.setEmail("user4@example.com");
        user.setAddress("123 Đường Láng, Hà Nội");

        assertThrows(PersistenceException.class, () -> {
            userService.saveOrUpdate(user);
        }, "Dự kiến sẽ xảy ra lỗi PersistenceException khi username để trống");
    }
}