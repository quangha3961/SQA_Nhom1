package com.example.PromotionService;

import beebooks.StartServer;
import beebooks.entities.Promotion;
import beebooks.service.PromotionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class PromotionServiceFindAllTest {

    @Autowired
    private PromotionService promotionService;

    // Test 120: Kiểm tra danh sách chương trình khuyến mãi không rỗng khi có ít nhất một chương trình
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testFindAll_NotEmpty() {
        // Tạo và lưu một chương trình khuyến mãi để đảm bảo danh sách không rỗng
        Promotion promotion = new Promotion();
        promotion.setName("Khuyến mãi 20%");
        promotion.setPercent(20.0);
        promotion.setStartDate(Date.valueOf("2025-01-01"));
        promotion.setEndDate(Date.valueOf("2025-12-31"));
        promotionService.saveOrUpdate(promotion);

        List<Promotion> promotions = promotionService.findAll();

        Assertions.assertFalse(promotions.isEmpty(), "Danh sách chương trình khuyến mãi không được rỗng");
    }

    // Test 121: Kiểm tra danh sách chứa một chương trình khuyến mãi đã lưu
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testFindAll_ContainsKnownPromotion() {
        // Tạo và lưu một chương trình khuyến mãi để đảm bảo chúng ta có một chương trình đã biết
        Promotion promotion = new Promotion();
        promotion.setName("Khuyến mãi 20%");
        promotion.setPercent(20.0);
        promotion.setStartDate(Date.valueOf("2025-01-01"));
        promotion.setEndDate(Date.valueOf("2025-12-31"));
        Promotion savedPromotion = promotionService.saveOrUpdate(promotion);

        List<Promotion> promotions = promotionService.findAll();

        boolean hasPromotionWithKnownId = promotions.stream().anyMatch(p -> p.getId() == savedPromotion.getId());
        Assertions.assertTrue(hasPromotionWithKnownId, "Danh sách phải chứa chương trình khuyến mãi với ID " + savedPromotion.getId());

        Promotion foundPromotion = promotions.stream().filter(p -> p.getId() == savedPromotion.getId()).findFirst().orElse(null);
        Assertions.assertNotNull(foundPromotion, "Chương trình khuyến mãi phải tồn tại trong danh sách");
        Assertions.assertEquals("Khuyến mãi 20%", foundPromotion.getName(), "Tên chương trình khuyến mãi phải khớp");
        Assertions.assertEquals(20.0, foundPromotion.getPercent(), "Phần trăm khuyến mãi phải khớp");
        Assertions.assertEquals(Date.valueOf("2025-01-01"), foundPromotion.getStartDate(), "Ngày bắt đầu phải khớp");
        Assertions.assertEquals(Date.valueOf("2025-12-31"), foundPromotion.getEndDate(), "Ngày kết thúc phải khớp");
    }
}
