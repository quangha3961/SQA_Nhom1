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
public class CategoriesBlogServiceUpdateTest {

    @Autowired
    private CategoriesBlogService categoriesBlogService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 46: Cập nhật danh mục blog thành công
    @Test
    @Order(1)
    @Transactional
    @Rollback(value = true)
    public void testUpdateCategoriesBlog_Success() {
        CategoriesBlog categoriesBlog = new CategoriesBlog();
        categoriesBlog.setName("Tin Tức Sách");
        categoriesBlog.setDescription("Danh mục chứa các bài viết về tin tức sách");
        categoriesBlog.setSeo("tin-tuc-sach");
        CategoriesBlog savedCategoriesBlog = categoriesBlogService.saveOrUpdate(categoriesBlog);

        assertNotNull(savedCategoriesBlog.getId(), "Danh mục blog phải được lưu trước khi cập nhật");

        savedCategoriesBlog.setName("Tin Tức Sách Cập Nhật");
        savedCategoriesBlog.setDescription("Danh mục chứa các bài viết tin tức sách đã cập nhật");
        savedCategoriesBlog.setSeo("tin-tuc-sach-cap-nhat");
        CategoriesBlog updatedCategoriesBlog = categoriesBlogService.saveOrUpdate(savedCategoriesBlog);

        assertEquals(savedCategoriesBlog.getId(), updatedCategoriesBlog.getId(), "ID không được thay đổi sau khi cập nhật");
        assertEquals("Tin Tức Sách Cập Nhật", updatedCategoriesBlog.getName(), "Tên danh mục blog phải được cập nhật");
        assertEquals("Danh mục chứa các bài viết tin tức sách đã cập nhật", updatedCategoriesBlog.getDescription(), "Mô tả danh mục blog phải được cập nhật");
        assertEquals("tin-tuc-sach-cap-nhat", updatedCategoriesBlog.getSeo(), "SEO danh mục blog phải được cập nhật");

        CategoriesBlog categoriesBlogFromDb = entityManager.find(CategoriesBlog.class, savedCategoriesBlog.getId());
        assertEquals("Tin Tức Sách Cập Nhật", categoriesBlogFromDb.getName(), "Tên danh mục blog trong DB phải được cập nhật");
        assertEquals("Danh mục chứa các bài viết tin tức sách đã cập nhật", categoriesBlogFromDb.getDescription(), "Mô tả danh mục blog trong DB phải được cập nhật");
        assertEquals("tin-tuc-sach-cap-nhat", categoriesBlogFromDb.getSeo(), "SEO danh mục blog trong DB phải được cập nhật");

        assertNotNull(updatedCategoriesBlog.getUpdatedDate(), "Ngày cập nhật không được để trống");
        assertTrue(updatedCategoriesBlog.getUpdatedDate().after(updatedCategoriesBlog.getCreatedDate()), "Ngày cập nhật phải sau ngày tạo");
        assertEquals(1, updatedCategoriesBlog.getCreatedBy(), "CreatedBy phải được giữ nguyên");
        assertEquals(1, updatedCategoriesBlog.getUpdatedBy(), "UpdatedBy phải được thiết lập");
    }

    // Test 47: Cập nhật danh mục blog với ID không tồn tại
    @Test
    @Order(2)
    @Transactional
    @Rollback(value = true)
    public void testUpdateCategoriesBlog_Fail_NonExistentId() {
        CategoriesBlog categoriesBlog = new CategoriesBlog();
        categoriesBlog.setId(999); // Assuming ID 999 does not exist
        categoriesBlog.setName("Danh Mục Blog Không Tồn Tại");
        categoriesBlog.setDescription("Mô tả không tồn tại");
        categoriesBlog.setSeo("danh-muc-khong-ton-tai");

        CategoriesBlog updatedCategoriesBlog = categoriesBlogService.saveOrUpdate(categoriesBlog);

        assertNotNull(updatedCategoriesBlog.getId(), "Một danh mục blog mới phải được tạo vì ID không tồn tại");
        assertNotEquals(999, updatedCategoriesBlog.getId(), "ID 999 không tồn tại, nên một ID mới phải được tạo");
    }

    // Test 48: Cập nhật danh mục blog với tên quá ngắn
    @Test
    @Order(3)
    @Transactional
    @Rollback(value = true)
    public void testUpdateCategoriesBlog_Fail_NameTooShort() {
        CategoriesBlog categoriesBlog = new CategoriesBlog();
        categoriesBlog.setName("Tin Tức Sách");
        categoriesBlog.setDescription("Danh mục chứa các bài viết về tin tức sách");
        categoriesBlog.setSeo("tin-tuc-sach");
        CategoriesBlog savedCategoriesBlog = categoriesBlogService.saveOrUpdate(categoriesBlog);

        savedCategoriesBlog.setName("AB");
        CategoriesBlog updatedCategoriesBlog = categoriesBlogService.saveOrUpdate(savedCategoriesBlog);

        assertNotEquals("AB", updatedCategoriesBlog.getName(), "Không được cập nhật");
    }

    // Test 49: Cập nhật danh mục blog với tên null
    @Test
    @Order(4)
    @Transactional
    @Rollback(value = true)
    public void testUpdateCategoriesBlog_Fail_NameIsNull() {
        CategoriesBlog categoriesBlog = new CategoriesBlog();
        categoriesBlog.setName("Tin Tức Sách");
        categoriesBlog.setDescription("Danh mục chứa các bài viết về tin tức sách");
        categoriesBlog.setSeo("tin-tuc-sach");
        CategoriesBlog savedCategoriesBlog = categoriesBlogService.saveOrUpdate(categoriesBlog);

        savedCategoriesBlog.setName(null);

        assertThrows(PersistenceException.class, () -> {
            categoriesBlogService.saveOrUpdate(savedCategoriesBlog);
        }, "Dự kiến sẽ xảy ra lỗi PersistenceException khi tên để trống trong quá trình cập nhật");
    }

    // Test 50: Cập nhật danh mục blog với mô tả null
    @Test
    @Order(5)
    @Transactional
    @Rollback(value = true)
    public void testUpdateCategoriesBlog_Fail_DescriptionIsNull() {
        CategoriesBlog categoriesBlog = new CategoriesBlog();
        categoriesBlog.setName("Tin Tức Sách");
        categoriesBlog.setDescription("Danh mục chứa các bài viết về tin tức sách");
        categoriesBlog.setSeo("tin-tuc-sach");
        CategoriesBlog savedCategoriesBlog = categoriesBlogService.saveOrUpdate(categoriesBlog);

        savedCategoriesBlog.setDescription(null);

        assertThrows(PersistenceException.class, () -> {
            categoriesBlogService.saveOrUpdate(savedCategoriesBlog);
        }, "Dự kiến sẽ xảy ra lỗi PersistenceException khi mô tả để trống trong quá trình cập nhật");
    }

    // Test 51: Cập nhật danh mục blog với SEO null (SEO có thể null)
    @Test
    @Order(6)
    @Transactional
    @Rollback(value = true)
    public void testUpdateCategoriesBlog_Success_SeoIsNull() {
        CategoriesBlog categoriesBlog = new CategoriesBlog();
        categoriesBlog.setName("Tin Tức Sách");
        categoriesBlog.setDescription("Danh mục chứa các bài viết về tin tức sách");
        categoriesBlog.setSeo("tin-tuc-sach");
        CategoriesBlog savedCategoriesBlog = categoriesBlogService.saveOrUpdate(categoriesBlog);

        savedCategoriesBlog.setSeo(null);
        CategoriesBlog updatedCategoriesBlog = categoriesBlogService.saveOrUpdate(savedCategoriesBlog);

        assertNotNull(updatedCategoriesBlog.getSeo(), "SEO Không được null");
        assertEquals("Tin Tức Sách", updatedCategoriesBlog.getName(), "Tên danh mục blog không được thay đổi");
        assertEquals("Danh mục chứa các bài viết về tin tức sách", updatedCategoriesBlog.getDescription(), "Mô tả danh mục blog không được thay đổi");
    }
}