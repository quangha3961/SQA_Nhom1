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
public class CategoriesServiceUpdateTest {

    @Autowired
    private CategoriesService categoriesService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 52: Cập nhật danh mục thành công với thông tin hợp lệ
    @Test
    @Order(1)
    @Transactional
    @Rollback(value = true)
    public void testUpdateCategory_Success() {
        Categories category = new Categories();
        category.setName("Sách Văn Học");
        category.setDescription("Danh mục sách văn học Việt Nam và thế giới");
        category.setSeo("sach-van-hoc");
        Categories savedCategory = categoriesService.saveOrUpdate(category);

        assertNotNull(savedCategory.getId(), "Danh mục phải được lưu trước khi cập nhật");

        savedCategory.setName("Sách Văn Học Cập Nhật");
        savedCategory.setDescription("Danh mục sách văn học được cập nhật");
        savedCategory.setSeo("sach-van-hoc-cap-nhat");
        Categories updatedCategory = categoriesService.saveOrUpdate(savedCategory);

        assertEquals(savedCategory.getId(), updatedCategory.getId(), "ID không được thay đổi sau khi cập nhật");
        assertEquals("Sách Văn Học Cập Nhật", updatedCategory.getName(), "Tên danh mục phải được cập nhật");
        assertEquals("Danh mục sách văn học được cập nhật", updatedCategory.getDescription(), "Mô tả danh mục phải được cập nhật");
        assertEquals("sach-van-hoc-cap-nhat", updatedCategory.getSeo(), "SEO phải được cập nhật");

        Categories categoryFromDb = entityManager.find(Categories.class, savedCategory.getId());
        assertEquals("Sách Văn Học Cập Nhật", categoryFromDb.getName(), "Tên danh mục trong DB phải được cập nhật");
        assertEquals("Danh mục sách văn học được cập nhật", categoryFromDb.getDescription(), "Mô tả danh mục trong DB phải được cập nhật");
        assertEquals("sach-van-hoc-cap-nhat", categoryFromDb.getSeo(), "SEO trong DB phải được cập nhật");

        assertNotNull(updatedCategory.getUpdatedDate(), "Ngày cập nhật không được để trống");
        assertTrue(updatedCategory.getUpdatedDate().after(updatedCategory.getCreatedDate()), "Ngày cập nhật phải sau ngày tạo");
        assertEquals(1, updatedCategory.getCreatedBy(), "CreatedBy phải được giữ nguyên");
        assertEquals(1, updatedCategory.getUpdatedBy(), "UpdatedBy phải được thiết lập");
    }

    // Test 53: Cập nhật danh mục thất bại khi ID không tồn tại
    @Test
    @Order(2)
    @Transactional
    @Rollback(value = true)
    public void testUpdateCategory_Fail_NonExistentId() {
        Categories category = new Categories();
        category.setId(999); // ID không tồn tại
        category.setDescription("Mô tả mặc định");
        category.setName("Danh mục mặc định");
        category.setSeo("danh-muc-mac-dinh");

        Categories existingCategory = entityManager.find(Categories.class, 999);
        assertNull(existingCategory, "Danh mục với ID 999 không nên tồn tại trước khi cập nhật");

        Categories updatedCategory = categoriesService.saveOrUpdate(category);

        Categories afterUpdateCategory = entityManager.find(Categories.class, 999);
        assertNull(afterUpdateCategory, "Danh mục với ID 999 không nên tồn tại trong cơ sở dữ liệu vì ID không hợp lệ");
        assertNull(updatedCategory, "saveOrUpdate nên trả về null khi ID không tồn tại");
    }

    // Test 54: Cập nhật danh mục thất bại khi tên quá ngắn
    @Test
    @Order(3)
    @Transactional
    @Rollback(value = true)
    public void testUpdateCategory_Fail_NameTooShort() {
        Categories category = new Categories();
        category.setName("Sách Văn Học");
        category.setDescription("Danh mục sách văn học Việt Nam và thế giới");
        category.setSeo("sach-van-hoc");
        Categories savedCategory = categoriesService.saveOrUpdate(category);

        savedCategory.setName("AB");
        Categories updatedCategory = categoriesService.saveOrUpdate(savedCategory);

        assertEquals("AB", updatedCategory.getName(), "Tên danh mục đã được cập nhật nhưng không nên vì tên quá ngắn (thiếu ràng buộc)");
    }

    // Test 55: Cập nhật danh mục thất bại khi mô tả là null
    @Test
    @Order(4)
    @Transactional
    @Rollback(value = true)
    public void testUpdateCategory_Fail_DescriptionIsNull() {
        Categories category = new Categories();
        category.setName("Sách Văn Học");
        category.setDescription("Danh mục sách văn học Việt Nam và thế giới");
        category.setSeo("sach-van-hoc");
        Categories savedCategory = categoriesService.saveOrUpdate(category);

        savedCategory.setDescription(null);

        assertThrows(PersistenceException.class, () -> {
            categoriesService.saveOrUpdate(savedCategory);
            entityManager.flush(); // Đảm bảo flush để kiểm tra ràng buộc null
        }, "Cập nhật thất bại vì mô tả không được để null");

        assertFalse(true, "Test thất bại vì tham số null không được phép");
    }

    // Test 56: Cập nhật danh mục thất bại khi SEO là null
    @Test
    @Order(5)
    @Transactional
    @Rollback(value = true)
    public void testUpdateCategory_Fail_SeoIsNull() {
        Categories category = new Categories();
        category.setName("Sách Văn Học");
        category.setDescription("Danh mục sách văn học Việt Nam và thế giới");
        category.setSeo("sach-van-hoc");
        Categories savedCategory = categoriesService.saveOrUpdate(category);

        savedCategory.setSeo(null);
        Categories updatedCategory = categoriesService.saveOrUpdate(savedCategory);

        assertNull(updatedCategory.getSeo(), "SEO là null sau khi cập nhật");
        assertEquals("Sách Văn Học", updatedCategory.getName(), "Tên danh mục không được thay đổi");
        assertEquals("Danh mục sách văn học Việt Nam và thế giới", updatedCategory.getDescription(), "Mô tả danh mục không được thay đổi");

        assertFalse(true, "Test thất bại vì tham số null không được phép");
    }
}