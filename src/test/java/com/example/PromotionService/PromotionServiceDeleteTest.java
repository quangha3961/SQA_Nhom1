package com.example.PromotionService;

import beebooks.StartServer;
import beebooks.entities.Promotion;
import beebooks.service.PromotionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.sql.Date;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class PromotionServiceDeleteTest {

    @Autowired
    private PromotionService promotionService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 118: Kiểm tra xóa chương trình khuyến mãi qua entity
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testDelete_PromotionEntity() {
        Promotion promotion = new Promotion();
        promotion.setName("Promotion To Delete");
        promotion.setPercent(15.0);
        promotion.setStartDate(Date.valueOf("2025-01-01"));
        promotion.setEndDate(Date.valueOf("2025-12-31"));
        Promotion savedPromotion = promotionService.saveOrUpdate(promotion);

        Assertions.assertNotNull(savedPromotion.getId(), "Chương trình khuyến mãi phải được lưu trước khi xóa");

        promotionService.delete(savedPromotion);

        Promotion deletedPromotion = entityManager.find(Promotion.class, savedPromotion.getId());
        Assertions.assertNull(deletedPromotion, "Chương trình khuyến mãi phải được xóa khỏi cơ sở dữ liệu");
    }

    // Test 119: Kiểm tra xóa chương trình khuyến mãi qua ID
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testDeleteById() {
        Promotion promotion = new Promotion();
        promotion.setName("Promotion To Delete By Id");
        promotion.setPercent(15.0);
        promotion.setStartDate(Date.valueOf("2025-01-01"));
        promotion.setEndDate(Date.valueOf("2025-12-31"));
        Promotion savedPromotion = promotionService.saveOrUpdate(promotion);

        Assertions.assertNotNull(savedPromotion.getId(), "Chương trình khuyến mãi phải được lưu trước khi xóa");

        promotionService.deleteById(savedPromotion.getId());

        Promotion deletedPromotion = entityManager.find(Promotion.class, savedPromotion.getId());
        Assertions.assertNull(deletedPromotion, "Chương trình khuyến mãi phải được xóa khỏi cơ sở dữ liệu khi xóa bằng ID");
    }
}
