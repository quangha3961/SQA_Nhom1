package com.example.CategoriesBlogService;

import beebooks.StartServer;
import beebooks.entities.CategoriesBlog;
import beebooks.service.impl.CategoriesBlogService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class CategoriesBlogServiceDeleteTest {

    @Autowired
    private CategoriesBlogService categoriesBlogService;

    @PersistenceContext
    private EntityManager entityManager;
    //Test 38: Xóa 1 blog
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testDelete_CategoriesBlogEntity() {
        CategoriesBlog categoriesBlog = new CategoriesBlog();
        categoriesBlog.setName("CategoriesBlog To Delete");
        categoriesBlog.setDescription("This categories blog will be deleted");
        categoriesBlog.setSeo("categories-blog-to-delete");
        CategoriesBlog savedCategoriesBlog = categoriesBlogService.saveOrUpdate(categoriesBlog);

        Assertions.assertNotNull(savedCategoriesBlog.getId(), "Danh mục blog phải được lưu trước khi xóa");

        categoriesBlogService.delete(savedCategoriesBlog);

        CategoriesBlog deletedCategoriesBlog = entityManager.find(CategoriesBlog.class, savedCategoriesBlog.getId());
        Assertions.assertNull(deletedCategoriesBlog, "Danh mục blog phải được xóa khỏi cơ sở dữ liệu");
    }
    //Test 39: xóa blog theo id
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testDeleteById() {
        CategoriesBlog categoriesBlog = new CategoriesBlog();
        categoriesBlog.setName("CategoriesBlog To Delete By Id");
        categoriesBlog.setDescription("This categories blog will be deleted by ID");
        categoriesBlog.setSeo("categories-blog-to-delete-by-id");
        CategoriesBlog savedCategoriesBlog = categoriesBlogService.saveOrUpdate(categoriesBlog);

        Assertions.assertNotNull(savedCategoriesBlog.getId(), "Danh mục blog phải được lưu trước khi xóa");

        categoriesBlogService.deleteById(savedCategoriesBlog.getId());

        CategoriesBlog deletedCategoriesBlog = entityManager.find(CategoriesBlog.class, savedCategoriesBlog.getId());
        Assertions.assertNull(deletedCategoriesBlog, "Danh mục blog phải được xóa khỏi cơ sở dữ liệu khi xóa bằng ID");
    }
}