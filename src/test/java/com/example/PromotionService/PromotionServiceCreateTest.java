package com.example.PromotionService;

import beebooks.StartServer;
import beebooks.entities.Promotion;
import beebooks.service.PromotionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class PromotionServiceCreateTest {

    @Autowired
    private PromotionService promotionService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 1: Thêm chương trình khuyến mãi thành công với thông tin hợp lệ, rollback sau khi test
    @Test
    @Order(1)
    @Rollback(value = true)
    public void testCreatePromotion_SuccessWithRollback() {
        Promotion promotion = new Promotion();
        promotion.setName("Khuyến mãi 20%");
        promotion.setPercent(20.0);
        promotion.setStartDate(Date.valueOf("2025-01-01"));
        promotion.setEndDate(Date.valueOf("2025-12-31"));

        Promotion savedPromotion = promotionService.saveOrUpdate(promotion);

        assertNotNull(savedPromotion.getId(), "Id không được để trống.");
        assertEquals("Khuyến mãi 20%", savedPromotion.getName(), "Tên chương trình khuyến mãi không đúng.");
        assertEquals(20.0, savedPromotion.getPercent(), "Phần trăm khuyến mãi không đúng.");
        assertEquals(Date.valueOf("2025-01-01"), savedPromotion.getStartDate(), "Ngày bắt đầu không đúng.");
        assertEquals(Date.valueOf("2025-12-31"), savedPromotion.getEndDate(), "Ngày kết thúc không đúng.");

        Promotion promotionFromDb = entityManager.find(Promotion.class, savedPromotion.getId());
        assertNotNull(promotionFromDb, "Chương trình khuyến mãi không được tìm thấy trong DB.");
    }

    // Test 2: Thêm chương trình khuyến mãi thất bại khi tên là null
    @Test
    @Order(2)
    public void testCreatePromotion_Fail_NameIsNull() {
        Promotion promotion = new Promotion();
        promotion.setPercent(15.0);
        promotion.setStartDate(Date.valueOf("2025-01-01"));
        promotion.setEndDate(Date.valueOf("2025-12-31"));

        assertThrows(PersistenceException.class, () -> {
            promotionService.saveOrUpdate(promotion);
            entityManager.flush(); // Đảm bảo flush để kiểm tra ràng buộc null
        }, "Thêm chương trình khuyến mãi thất bại vì tên không được để null");

        assertFalse(true, "Test thất bại vì tham số null không được phép");
    }

    // Test 3: Thêm chương trình khuyến mãi thất bại khi tên quá ngắn (giả sử có ràng buộc)
    @Test
    @Order(3)
    public void testCreatePromotion_Fail_NameTooShort() {
        Promotion promotion = new Promotion();
        promotion.setName("AB");
        promotion.setPercent(10.0);
        promotion.setStartDate(Date.valueOf("2025-01-01"));
        promotion.setEndDate(Date.valueOf("2025-12-31"));

        Promotion savedPromotion = promotionService.saveOrUpdate(promotion);

        assertNotNull(savedPromotion.getId(), "Chương trình khuyến mãi có tên ngắn nhưng được lưu do thiếu ràng buộc.");
    }

    // Test 4: Thêm chương trình khuyến mãi thất bại khi percent là null
    @Test
    @Order(4)
    @Transactional
    @Rollback(value = true)
    public void testCreatePromotion_Fail_PercentIsNull() {
        Promotion promotion = new Promotion();
        promotion.setName("Khuyến mãi không có phần trăm");
        promotion.setPercent(null);
        promotion.setStartDate(Date.valueOf("2025-01-01"));
        promotion.setEndDate(Date.valueOf("2025-12-31"));

        Promotion savedPromotion = promotionService.saveOrUpdate(promotion);

        assertNotNull(savedPromotion.getId(), "Chương trình khuyến mãi được lưu dù percent là null.");
        assertNull(savedPromotion.getPercent(), "Percent là null.");

        assertFalse(true, "Test thất bại vì tham số null không được phép");
    }

    // Test 5: Thêm chương trình khuyến mãi thất bại khi ngày bắt đầu và kết thúc là null
    @Test
    @Order(5)
    @Transactional
    @Rollback(value = true)
    public void testCreatePromotion_Fail_DatesAreNull() {
        Promotion promotion = new Promotion();
        promotion.setName("Khuyến mãi không có ngày");
        promotion.setPercent(25.0);
        promotion.setStartDate(null);
        promotion.setEndDate(null);

        Promotion savedPromotion = promotionService.saveOrUpdate(promotion);

        assertNotNull(savedPromotion.getId(), "Chương trình khuyến mãi được lưu dù ngày là null.");
        assertNull(savedPromotion.getStartDate(), "Ngày bắt đầu là null.");
        assertNull(savedPromotion.getEndDate(), "Ngày kết thúc là null.");

        assertFalse(true, "Test thất bại vì tham số null không được phép");
    }
}