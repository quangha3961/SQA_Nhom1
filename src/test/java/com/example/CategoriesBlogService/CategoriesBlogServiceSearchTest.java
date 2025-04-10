package com.example.CategoriesBlogService;

import beebooks.StartServer;
import beebooks.dto.BlogSearchModel;
import beebooks.entities.CategoriesBlog;
import beebooks.service.PagerData;
import beebooks.service.impl.CategoriesBlogService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class CategoriesBlogServiceSearchTest {

    @Autowired
    private CategoriesBlogService categoriesBlogService;

    // Test 42: Tìm danh mục blog có trong csdl
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testSearchWithExistingSeo() {
        // First, create a categories blog to search for
        CategoriesBlog categoriesBlog = new CategoriesBlog();
        categoriesBlog.setName("Tin Tức Sách");
        categoriesBlog.setDescription("Danh mục chứa các bài viết về tin tức sách");
        categoriesBlog.setSeo("tin-tuc-sach");
        categoriesBlogService.saveOrUpdate(categoriesBlog);

        BlogSearchModel searchModel = new BlogSearchModel();
        searchModel.seo = "tin-tuc-sach";

        PagerData<CategoriesBlog> result = categoriesBlogService.search(searchModel);

        Assertions.assertFalse(result.getData().isEmpty(), "Có danh mục blog với SEO 'tin-tuc-sach' trong danh sách");
    }

    // Test 43: Tìm danh mục blog không có trong csdl
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testSearchWithNonExistentSeo() {
        BlogSearchModel searchModel = new BlogSearchModel();
        searchModel.seo = "abcxyz";

        PagerData<CategoriesBlog> result = categoriesBlogService.search(searchModel);

        Assertions.assertTrue(result.getData().isEmpty(), "Không có danh mục blog với SEO 'abcxyz' trong danh sách");
    }

    // Test 44: Tìm theo ID danh mục blog có trong csdl
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testSearchById() {
        // First, create a categories blog to search for
        CategoriesBlog categoriesBlog = new CategoriesBlog();
        categoriesBlog.setName("Tin Tức Sách");
        categoriesBlog.setDescription("Danh mục chứa các bài viết về tin tức sách");
        categoriesBlog.setSeo("tin-tuc-sach");
        CategoriesBlog savedCategoriesBlog = categoriesBlogService.saveOrUpdate(categoriesBlog);

        BlogSearchModel searchModel = new BlogSearchModel();
        searchModel.categoryId = savedCategoriesBlog.getId();

        PagerData<CategoriesBlog> result = categoriesBlogService.search(searchModel);

        Assertions.assertFalse(result.getData().isEmpty(), "Phải có kết quả với ID tồn tại");
        Assertions.assertEquals(savedCategoriesBlog.getId(), result.getData().get(0).getId(), "ID kết quả phải khớp");
    }

    // Test 45: Tìm theo ID danh mục blog không có trong csdl
    @Test
    @Order(4)
    @Transactional
    @Rollback
    public void testSearchByNonExistentId() {
        BlogSearchModel searchModel = new BlogSearchModel();
        searchModel.categoryId = 999; // Assuming ID 999 does not exist

        PagerData<CategoriesBlog> result = categoriesBlogService.search(searchModel);

        Assertions.assertTrue(result.getData().isEmpty(), "Không có kết quả với ID 999 không tồn tại");
    }
}