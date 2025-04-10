package com.example.SubcribeService;

import beebooks.StartServer;
import beebooks.dto.SubcribeSearchModel;
import beebooks.entities.Subcribe;
import beebooks.service.PagerData;
import beebooks.service.SubcribeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class SubcribeServiceSearchTest {

    @Autowired
    private SubcribeService subcribeService;

    // Test 161: Tìm đăng ký theo email - Thành công
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testSearchWithExistingEmail() {
        Subcribe subcribe = new Subcribe();
        subcribe.setEmail("user1@example.com");
        subcribe.setCreatedDate(new Date());
        subcribeService.saveOrUpdate(subcribe);

        SubcribeSearchModel searchModel = new SubcribeSearchModel();
        searchModel.keyword = "user1@example.com";

        PagerData<Subcribe> result = subcribeService.search(searchModel);

        assertFalse(result.getData().isEmpty(), "Có đăng ký với email 'user1@example.com' trong danh sách");
        assertEquals("user1@example.com", result.getData().get(0).getEmail(), "Email phải khớp");
    }

    // Test 162: Tìm đăng ký với từ khóa email không tồn tại
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testSearchWithNonExistentEmail() {
        SubcribeSearchModel searchModel = new SubcribeSearchModel();
        searchModel.keyword = "nonexistent@example.com";

        PagerData<Subcribe> result = subcribeService.search(searchModel);

        assertTrue(result.getData().isEmpty(), "Không có đăng ký với email 'nonexistent@example.com' trong danh sách");
    }

    // Test 163:Tìm đăng ký với searchModel null
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testSearchWithNullSearchModel() {
        Subcribe subcribe = new Subcribe();
        subcribe.setEmail("user2@example.com");
        subcribe.setCreatedDate(new Date());
        subcribeService.saveOrUpdate(subcribe);

        PagerData<Subcribe> result = subcribeService.search(null);

        assertFalse(result.getData().isEmpty(), "Phải có kết quả khi searchModel là null (trả về tất cả)");
    }

    // Test 164: Tìm đăng ký với từ khóa email một phần
    @Test
    @Order(4)
    @Transactional
    @Rollback
    public void testSearchWithPartialEmailKeyword() {
        Subcribe subcribe = new Subcribe();
        subcribe.setEmail("user3@example.com");
        subcribe.setCreatedDate(new Date());
        subcribeService.saveOrUpdate(subcribe);

        SubcribeSearchModel searchModel = new SubcribeSearchModel();
        searchModel.keyword = "user3";

        PagerData<Subcribe> result = subcribeService.search(searchModel);

        assertFalse(result.getData().isEmpty(), "Có đăng ký với email chứa 'user3' trong danh sách");
    }
}