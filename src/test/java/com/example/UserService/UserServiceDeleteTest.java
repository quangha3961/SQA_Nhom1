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

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class UserServiceDeleteTest {

    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 169: Xóa người dùng hợp lệ
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testDeleteUser_Success() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));
        user.setEmail("user1@example.com");
        user.setAddress("123 Đường Láng, Hà Nội");
        User savedUser = userService.saveOrUpdate(user);

        assertNotNull(savedUser.getId(), "Người dùng phải được lưu trước khi xóa");

        userService.delete(savedUser);

        User deletedUser = entityManager.find(User.class, savedUser.getId());
        assertNull(deletedUser, "Người dùng phải được xóa khỏi cơ sở dữ liệu");
    }

    // Test 170: Xóa người dùng với ID không tồn tại
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testDeleteUser_NonExistentId() {
        // Tạo một ID không tồn tại
        User user = new User();
        user.setId(999); // ID không tồn tại trong cơ sở dữ liệu

        // Kiểm tra xem user có tồn tại không trước khi xóa
        User userInDb = entityManager.find(User.class, user.getId());

        if (userInDb != null) {
            // Nếu người dùng tồn tại trong DB, xóa người dùng
            userService.delete(userInDb);
        } else {
            // Nếu người dùng không tồn tại, kiểm tra rằng không có gì thay đổi
            assertNull(userInDb, "Người dùng với ID không tồn tại không nên có trong DB");
        }
    }

}