package com.example.SubcribeService;

import beebooks.StartServer;
import beebooks.entities.Subcribe;
import beebooks.service.SubcribeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class SubcribeServiceCreateTest {

    @Autowired
    private SubcribeService subcribeService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 1: Tạo đăng ký hợp lệ với email và createdDate hợp lệ
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testCreateSubcribe_Success() {
        Subcribe subcribe = new Subcribe();
        subcribe.setEmail("user1@example.com");
        subcribe.setCreatedDate(new Date());

        Subcribe savedSubcribe = subcribeService.saveOrUpdate(subcribe);

        assertNotNull(savedSubcribe.getId(), "ID đăng ký không được để trống");
        assertEquals("user1@example.com", savedSubcribe.getEmail(), "Email không khớp với giá trị đã lưu");
        assertNotNull(savedSubcribe.getCreatedDate(), "Ngày tạo không được để trống");

        Subcribe subcribeFromDb = entityManager.find(Subcribe.class, savedSubcribe.getId());
        assertNotNull(subcribeFromDb, "Không tìm thấy đăng ký trong cơ sở dữ liệu");
    }

    // Test 2: Tạo đăng ký với email null (phải thất bại vì email là trường bắt buộc)
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testCreateSubcribe_Fail_EmailIsNull() {
        Subcribe subcribe = new Subcribe();
        subcribe.setEmail(null); // Email không được null theo ràng buộc cơ sở dữ liệu
        subcribe.setCreatedDate(new Date());

        assertThrows(PersistenceException.class, () -> {
            subcribeService.saveOrUpdate(subcribe);
            entityManager.flush(); // Đảm bảo lỗi được kích hoạt
        }, "Phải ném ra PersistenceException khi email là null");
    }

    // Test 3: Tạo đăng ký với createdDate null (sửa lại để phản ánh đúng thực tế)
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testCreateSubcribe_Fail_CreatedDateIsNull() {
        Subcribe subcribe = new Subcribe();
        subcribe.setEmail("user2@example.com");
        subcribe.setCreatedDate(null);

        // Giả sử createdDate là bắt buộc trong cơ sở dữ liệu (nếu không thì sửa lại logic này)
        assertThrows(PersistenceException.class, () -> {
            subcribeService.saveOrUpdate(subcribe);
            entityManager.flush(); // Đảm bảo lỗi được kích hoạt
        }, "Phải ném ra PersistenceException khi createdDate là null");

        // Nếu createdDate không bắt buộc, thay bằng đoạn code sau và bỏ assertThrows:
        /*
        Subcribe savedSubcribe = subcribeService.saveOrUpdate(subcribe);
        assertNotNull(savedSubcribe.getId(), "ID đăng ký không được để trống");
        assertEquals("user2@example.com", savedSubcribe.getEmail(), "Email không khớp với giá trị đã lưu");
        assertNull(savedSubcribe.getCreatedDate(), "Ngày tạo phải là null");
        */
    }
}