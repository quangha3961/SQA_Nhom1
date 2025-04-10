package com.example.CategoriesService;

import beebooks.StartServer;
import beebooks.entities.Categories;
import beebooks.service.CategoriesService;
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
public class CategoriesServiceCreateTest {

    @Autowired
    private CategoriesService categoriesService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 57: Thêm danh mục hợp lệ
    @Test
    @Order(1)
    @Rollback(value = true)
    public void testCreateCategory_SuccessWithRollback() {
        // Kiểm tra thêm danh mục hợp lệ với rollback. Tất cả các tham số cần hợp lệ
        Categories category = new Categories();
        category.setName("Sách Văn Học");
        category.setDescription("Danh mục sách văn học Việt Nam và thế giới");
        category.setSeo("sach-van-hoc");

        if (category.getName() == null || category.getDescription() == null || category.getSeo() == null || category.getName().length() < 3) {
            fail("Tên, mô tả, SEO phải hợp lệ.");
        } else {
            Categories savedCategory = categoriesService.saveOrUpdate(category);

            assertNotNull(savedCategory.getId(), "Id không được để trống.");
            assertEquals("Sách Văn Học", savedCategory.getName(), "Tên danh mục không đúng.");
            assertEquals("Danh mục sách văn học Việt Nam và thế giới", savedCategory.getDescription(), "Mô tả danh mục không đúng.");

            Categories categoryFromDb = entityManager.find(Categories.class, savedCategory.getId());
            assertNotNull(categoryFromDb, "Danh mục không được tìm thấy trong DB.");
        }
    }

    // Test 58: Thêm danh mục không có tên
    @Test
    @Order(2)
    public void testCreateCategory_Fail_NameIsNull() {
        // Kiểm tra khi tên danh mục là null, mong đợi PersistenceException
        Categories category = new Categories();
        category.setDescription("Danh mục không có tên");
        category.setSeo("sach-van-hoc");

        if (category.getName() == null || category.getName().length() < 3) {
            fail("Tên danh mục không được phép null hoặc quá ngắn.");
        } else {
            assertThrows(PersistenceException.class, () -> {
                categoriesService.saveOrUpdate(category);
            }, "Expected PersistenceException when name is null");
        }
    }

    // Test 59 : Tên danh mục quá ngắn
    @Test
    @Order(3)
    public void testCreateCategory_Fail_NameTooShort() {
        // Kiểm tra khi tên danh mục quá ngắn (dưới 3 ký tự), test sẽ thất bại
        Categories category = new Categories();
        category.setName("AB");
        category.setDescription("Danh mục ngắn");
        category.setSeo("sach-ab");

        if (category.getName() == null || category.getName().length() < 3) {
            fail("Tên danh mục quá ngắn.");
        } else {
            Categories savedCategory = categoriesService.saveOrUpdate(category);

            assertNotNull(savedCategory.getId(), "Danh mục có tên ngắn nhưng được lưu do thiếu ràng buộc.");
        }
    }

    // Test 60: Thêm danh mục thiếu mô tả
    @Test
    @Order(4)
    public void testCreateCategory_Fail_DescriptionIsNull() {
        // Kiểm tra khi mô tả danh mục là null, mong đợi PersistenceException
        Categories category = new Categories();
        category.setName("Sách Lịch Sử");
        category.setSeo("sach-lich-su");

        if (category.getDescription() == null) {
            fail("Mô tả không được phép null.");
        } else {
            assertThrows(PersistenceException.class, () -> {
                categoriesService.saveOrUpdate(category);
            }, "Dự kiến sẽ xảy ra lỗi PersistenceException khi mô tả để trống.");
        }
    }

    // Test 61: Thêm danh mục thiếu SEO
    @Test
    @Order(5)
    @Transactional
    @Rollback(value = true)
    public void testCreateCategory_Success_SeoIsNull() {
        // Kiểm tra danh mục có thể có SEO là null, trường hợp này phải thành công
        Categories category = new Categories();
        category.setName("Sách Khoa Học");
        category.setDescription("Danh mục sách khoa học");
        category.setSeo(null); // SEO is nullable

        if (category.getSeo() == null) {
            fail("SEO không được phép null.");
        } else {
            Categories savedCategory = categoriesService.saveOrUpdate(category);

            assertNotNull(savedCategory.getId(), "Danh mục phải được lưu thành công dù SEO là null.");
            assertNull(savedCategory.getSeo(), "SEO phải là null.");
        }
    }
}
