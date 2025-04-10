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
public class PromotionServiceUpdateTest {

    @Autowired
    private PromotionService promotionService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 1: Cập nhật chương trình khuyến mãi thành công với thông tin hợp lệ
    @Test
    @Order(1)
    @Transactional
    @Rollback(value = true)
    public void testUpdatePromotion_Success() {
        Promotion promotion = new Promotion();
        promotion.setName("Khuyến mãi 20%");
        promotion.setPercent(20.0);
        promotion.setStartDate(Date.valueOf("2025-01-01"));
        promotion.setEndDate(Date.valueOf("2025-12-31"));
        Promotion savedPromotion = promotionService.saveOrUpdate(promotion);

        assertNotNull(savedPromotion.getId(), "Chương trình khuyến mãi phải được lưu trước khi cập nhật");

        savedPromotion.setName("Khuyến mãi 30%");
        savedPromotion.setPercent(30.0);
        savedPromotion.setStartDate(Date.valueOf("2025-02-01"));
        savedPromotion.setEndDate(Date.valueOf("2025-11-30"));
        Promotion updatedPromotion = promotionService.saveOrUpdate(savedPromotion);

        assertEquals(savedPromotion.getId(), updatedPromotion.getId(), "ID không được thay đổi sau khi cập nhật");
        assertEquals("Khuyến mãi 30%", updatedPromotion.getName(), "Tên chương trình khuyến mãi phải được cập nhật");
        assertEquals(30.0, updatedPromotion.getPercent(), "Phần trăm khuyến mãi phải được cập nhật");
        assertEquals(Date.valueOf("2025-02-01"), updatedPromotion.getStartDate(), "Ngày bắt đầu phải được cập nhật");
        assertEquals(Date.valueOf("2025-11-30"), updatedPromotion.getEndDate(), "Ngày kết thúc phải được cập nhật");

        Promotion promotionFromDb = entityManager.find(Promotion.class, savedPromotion.getId());
        assertEquals("Khuyến mãi 30%", promotionFromDb.getName(), "Tên chương trình khuyến mãi trong DB phải được cập nhật");
        assertEquals(30.0, promotionFromDb.getPercent(), "Phần trăm khuyến mãi trong DB phải được cập nhật");
        assertEquals(Date.valueOf("2025-02-01"), promotionFromDb.getStartDate(), "Ngày bắt đầu trong DB phải được cập nhật");
        assertEquals(Date.valueOf("2025-11-30"), promotionFromDb.getEndDate(), "Ngày kết thúc trong DB phải được cập nhật");

        assertNotNull(updatedPromotion.getUpdatedDate(), "Ngày cập nhật không được để trống");
        assertTrue(updatedPromotion.getUpdatedDate().after(updatedPromotion.getCreatedDate()), "Ngày cập nhật phải sau ngày tạo");
        assertEquals(1, updatedPromotion.getCreatedBy(), "CreatedBy phải được giữ nguyên");
        assertEquals(1, updatedPromotion.getUpdatedBy(), "UpdatedBy phải được thiết lập");
    }

    // Test 2: Cập nhật chương trình khuyến mãi thất bại khi ID không tồn tại
    @Test
    @Order(2)
    @Transactional
    @Rollback(value = true)
    public void testUpdatePromotion_Fail_NonExistentId() {
        Promotion promotion = new Promotion();
        promotion.setId(999); // ID không tồn tại
        promotion.setName("Khuyến mãi không tồn tại");
        promotion.setPercent(15.0);
        promotion.setStartDate(Date.valueOf("2025-01-01"));
        promotion.setEndDate(Date.valueOf("2025-12-31"));

        Promotion updatedPromotion = promotionService.saveOrUpdate(promotion);

        assertNotNull(updatedPromotion.getId(), "Một chương trình khuyến mãi mới phải được tạo vì ID không tồn tại");
        assertNotEquals(999, updatedPromotion.getId(), "ID 999 không tồn tại, nên một ID mới phải được tạo");
    }

    // Test 3: Cập nhật chương trình khuyến mãi thất bại khi tên quá ngắn
    @Test
    @Order(3)
    @Transactional
    @Rollback(value = true)
    public void testUpdatePromotion_Fail_NameTooShort() {
        Promotion promotion = new Promotion();
        promotion.setName("Khuyến mãi 20%");
        promotion.setPercent(20.0);
        promotion.setStartDate(Date.valueOf("2025-01-01"));
        promotion.setEndDate(Date.valueOf("2025-12-31"));
        Promotion savedPromotion = promotionService.saveOrUpdate(promotion);

        savedPromotion.setName("AB");
        Promotion updatedPromotion = promotionService.saveOrUpdate(savedPromotion);

        assertEquals("AB", updatedPromotion.getName(), "Tên chương trình khuyến mãi đã được cập nhật nhưng không nên vì tên quá ngắn (thiếu ràng buộc)");
    }

    // Test 4: Cập nhật chương trình khuyến mãi thất bại khi tên là null
    @Test
    @Order(4)
    @Transactional
    @Rollback(value = true)
    public void testUpdatePromotion_Fail_NameIsNull() {
        Promotion promotion = new Promotion();
        promotion.setName("Khuyến mãi 20%");
        promotion.setPercent(20.0);
        promotion.setStartDate(Date.valueOf("2025-01-01"));
        promotion.setEndDate(Date.valueOf("2025-12-31"));
        Promotion savedPromotion = promotionService.saveOrUpdate(promotion);

        savedPromotion.setName(null);

        assertThrows(PersistenceException.class, () -> {
            promotionService.saveOrUpdate(savedPromotion);
            entityManager.flush(); // Đảm bảo flush để kiểm tra ràng buộc null
        }, "Cập nhật thất bại vì tên không được để null");

        assertFalse(true, "Test thất bại vì tham số null không được phép");
    }

    // Test 5: Cập nhật chương trình khuyến mãi thất bại khi percent và ngày là null
    @Test
    @Order(5)
    @Transactional
    @Rollback(value = true)
    public void testUpdatePromotion_Fail_NullFields() {
        Promotion promotion = new Promotion();
        promotion.setName("Khuyến mãi 20%");
        promotion.setPercent(20.0);
        promotion.setStartDate(Date.valueOf("2025-01-01"));
        promotion.setEndDate(Date.valueOf("2025-12-31"));
        Promotion savedPromotion = promotionService.saveOrUpdate(promotion);

        savedPromotion.setPercent(null);
        savedPromotion.setStartDate(null);
        savedPromotion.setEndDate(null);
        Promotion updatedPromotion = promotionService.saveOrUpdate(savedPromotion);

        assertNull(updatedPromotion.getPercent(), "Percent là null sau khi cập nhật");
        assertNull(updatedPromotion.getStartDate(), "Ngày bắt đầu là null sau khi cập nhật");
        assertNull(updatedPromotion.getEndDate(), "Ngày kết thúc là null sau khi cập nhật");
        assertEquals("Khuyến mãi 20%", updatedPromotion.getName(), "Tên chương trình khuyến mãi không được thay đổi");

        assertFalse(true, "Test thất bại vì tham số null không được phép");
    }
}