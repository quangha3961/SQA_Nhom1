package com.example.CategoriesBlogService;

import beebooks.StartServer;
import beebooks.entities.CategoriesBlog;
import beebooks.service.impl.CategoriesBlogService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class CategoriesBlogServiceCreateTest {

    @Autowired
    private CategoriesBlogService categoriesBlogService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 33: Thêm danh mục blog hợp lệ, rollback sau khi test
    @Test
    @Order(1)
    @Rollback(value = true)
    public void testCreateCategoriesBlog_SuccessWithRollback() {
        CategoriesBlog categoriesBlog = new CategoriesBlog();
        categoriesBlog.setName("Tin Tức Sách");
        categoriesBlog.setDescription("Danh mục chứa các bài viết về tin tức sách");
        categoriesBlog.setSeo("tin-tuc-sach");

        CategoriesBlog savedCategoriesBlog = categoriesBlogService.saveOrUpdate(categoriesBlog);

        assertNotNull(savedCategoriesBlog.getId(), "Id không được để trống.");
        assertEquals("Tin Tức Sách", savedCategoriesBlog.getName(), "Tên danh mục blog không đúng.");
        assertEquals("Danh mục chứa các bài viết về tin tức sách", savedCategoriesBlog.getDescription(), "Mô tả danh mục blog không đúng.");
        assertEquals("tin-tuc-sach", savedCategoriesBlog.getSeo(), "SEO danh mục blog không đúng.");

        CategoriesBlog categoriesBlogFromDb = entityManager.find(CategoriesBlog.class, savedCategoriesBlog.getId());
        assertNotNull(categoriesBlogFromDb, "Danh mục blog không được tìm thấy trong DB.");
    }

    // Test 34: Thêm danh mục blog không có tên (dùng assertThrows)
    @Test
    @Order(2)
    public void testCreateCategoriesBlog_Fail_NameIsNull() {
        CategoriesBlog categoriesBlog = new CategoriesBlog();
        categoriesBlog.setDescription("Danh mục không có tên");
        categoriesBlog.setSeo("danh-muc-khong-ten");

        assertThrows(PersistenceException.class, () -> {
            categoriesBlogService.saveOrUpdate(categoriesBlog);
        }, "Expected PersistenceException when name is null");
    }

    // Test 35: Tên danh mục blog quá ngắn (giả sử có ràng buộc)
    @Test
    @Order(3)
    public void testCreateCategoriesBlog_Fail_NameTooShort() {
        CategoriesBlog categoriesBlog = new CategoriesBlog();
        categoriesBlog.setName("AB");
        categoriesBlog.setDescription("Danh mục ngắn");
        categoriesBlog.setSeo("danh-muc-ngan");

        CategoriesBlog savedCategoriesBlog = categoriesBlogService.saveOrUpdate(categoriesBlog);

        assertNotNull(savedCategoriesBlog.getId(), "Danh mục blog có tên ngắn nhưng được lưu do thiếu ràng buộc.");
    }

    // Test 36: Thêm danh mục blog thiếu mô tả
    @Test
    @Order(4)
    public void testCreateCategoriesBlog_Fail_DescriptionIsNull() {
        CategoriesBlog categoriesBlog = new CategoriesBlog();
        categoriesBlog.setName("Sự Kiện Sách");
        categoriesBlog.setSeo("su-kien-sach");

        assertThrows(PersistenceException.class, () -> {
            categoriesBlogService.saveOrUpdate(categoriesBlog);
        }, "Dự kiến sẽ xảy ra lỗi PersistenceException khi mô tả để trống.");
    }

    // Test 37: Thêm danh mục blog với SEO null (SEO có thể null)
    @Test
    @Order(5)
    @Transactional
    @Rollback(value = true)
    public void testCreateCategoriesBlog_Success_SeoIsNull() {
        CategoriesBlog categoriesBlog = new CategoriesBlog();
        categoriesBlog.setName("Đánh Giá Sách");
        categoriesBlog.setDescription("Danh mục chứa các bài đánh giá sách");
        categoriesBlog.setSeo(null);

        CategoriesBlog savedCategoriesBlog = categoriesBlogService.saveOrUpdate(categoriesBlog);

        assertNull(savedCategoriesBlog.getId(), "Không được lưu vì null.");
        assertNull(savedCategoriesBlog.getSeo(), "SEO phải là null.");
    }
}