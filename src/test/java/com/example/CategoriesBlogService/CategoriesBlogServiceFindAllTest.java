package com.example.CategoriesBlogService;

import beebooks.StartServer;
import beebooks.entities.CategoriesBlog;
import beebooks.service.impl.CategoriesBlogService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class CategoriesBlogServiceFindAllTest {

    @Autowired
    private CategoriesBlogService categoriesBlogService;
    //test 40: tìm toàn bộ danh sách blog
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testFindAll_NotEmpty() {
        // First, create and save a categories blog
        CategoriesBlog categoriesBlog = new CategoriesBlog();
        categoriesBlog.setName("Tin Tức Sách");
        categoriesBlog.setDescription("Danh mục chứa các bài viết về tin tức sách");
        categoriesBlog.setSeo("tin-tuc-sach");
        categoriesBlogService.saveOrUpdate(categoriesBlog);

        List<CategoriesBlog> categoriesBlogs = categoriesBlogService.findAll();

        Assertions.assertFalse(categoriesBlogs.isEmpty(), "Danh sách danh mục blog không được rỗng");
    }
    //test 41 tìm blog với danh mục Tin tức sách
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testFindAll_ContainsKnownCategoriesBlog() {
        // Create and save a categories blog
        CategoriesBlog categoriesBlog = new CategoriesBlog();
        categoriesBlog.setName("Tin Tức Sách");
        categoriesBlog.setDescription("Danh mục chứa các bài viết về tin tức sách");
        categoriesBlog.setSeo("tin-tuc-sach");
        CategoriesBlog savedCategoriesBlog = categoriesBlogService.saveOrUpdate(categoriesBlog);

        List<CategoriesBlog> categoriesBlogs = categoriesBlogService.findAll();

        boolean hasCategoriesBlogWithKnownId = categoriesBlogs.stream().anyMatch(cb -> cb.getId() == savedCategoriesBlog.getId());
        Assertions.assertTrue(hasCategoriesBlogWithKnownId, "Danh sách phải chứa danh mục blog với ID " + savedCategoriesBlog.getId());

        CategoriesBlog foundCategoriesBlog = categoriesBlogs.stream().filter(cb -> cb.getId() == savedCategoriesBlog.getId()).findFirst().orElse(null);
        Assertions.assertNotNull(foundCategoriesBlog, "Danh mục blog phải tồn tại trong danh sách");
        Assertions.assertEquals("Tin Tức Sách", foundCategoriesBlog.getName(), "Tên danh mục blog phải khớp");
        Assertions.assertEquals("Danh mục chứa các bài viết về tin tức sách", foundCategoriesBlog.getDescription(), "Mô tả danh mục blog phải khớp");
        Assertions.assertEquals("tin-tuc-sach", foundCategoriesBlog.getSeo(), "SEO danh mục blog phải khớp");
    }
}