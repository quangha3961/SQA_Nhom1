package com.example.CategoriesService;

import beebooks.StartServer;
import beebooks.dto.ProductSearchModel;
import beebooks.entities.Categories;
import beebooks.service.CategoriesService;
import beebooks.service.PagerData;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class CategoriesServiceSearchTest {

    @Autowired
    private CategoriesService categoriesService;

    // Test 66: Tìm danh mục có trong csdl
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testSearchWithExistingSeo() {
        ProductSearchModel searchModel = new ProductSearchModel();
        searchModel.seo = "sach-van-hoc"; // Assuming this SEO exists

        PagerData<Categories> result = categoriesService.search(searchModel);

        Assertions.assertFalse(result.getData().isEmpty(), "Có danh mục với SEO 'sach-van-hoc' trong danh sách");
    }

    // Test 67: Tìm danh mục không có trong csdl
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testSearchWithNonExistentSeo() {
        ProductSearchModel searchModel = new ProductSearchModel();
        searchModel.seo = "abcxyz";

        PagerData<Categories> result = categoriesService.search(searchModel);

        Assertions.assertTrue(result.getData().isEmpty(), "Không có danh mục với SEO 'abcxyz' trong danh sách");
    }

    // Test 68: Tìm theo ID danh mục có trong csdl
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testSearchById() {
        ProductSearchModel searchModel = new ProductSearchModel();
        searchModel.categoryId = 52; // Assuming ID 52 exists

        PagerData<Categories> result = categoriesService.search(searchModel);

        Assertions.assertFalse(result.getData().isEmpty(), "Phải có kết quả với ID tồn tại");
        Assertions.assertEquals(52, result.getData().get(0).getId(), "ID kết quả phải là 52");
    }

    // Test 69: Tìm theo ID danh mục không có trong csdl
    @Test
    @Order(4)
    @Transactional
    @Rollback
    public void testSearchByNonExistentId() {
        ProductSearchModel searchModel = new ProductSearchModel();
        searchModel.categoryId = 20; // Assuming ID 20 does not exist

        PagerData<Categories> result = categoriesService.search(searchModel);

        Assertions.assertTrue(result.getData().isEmpty(), "Không có kết quả với ID 20 không tồn tại");
    }
}