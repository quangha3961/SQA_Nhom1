package com.example.CategoriesService;

import beebooks.StartServer;
import beebooks.entities.Categories;
import beebooks.service.CategoriesService;
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
public class CategoriesServiceDeleteTest {

    @Autowired
    private CategoriesService categoriesService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 62: Kiểm tra xóa danh mục qua entity
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testDelete_CategoryEntity() {
        Categories category = new Categories();
        category.setName("Category To Delete");
        category.setDescription("This category will be deleted");
        category.setSeo("category-to-delete");
        Categories savedCategory = categoriesService.saveOrUpdate(category);

        Assertions.assertNotNull(savedCategory.getId(), "Danh mục phải được lưu trước khi xóa");

        categoriesService.delete(savedCategory);

        Categories deletedCategory = entityManager.find(Categories.class, savedCategory.getId());
        Assertions.assertNull(deletedCategory, "Danh mục phải được xóa khỏi cơ sở dữ liệu");
    }

    // Test 63: Kiểm tra xóa danh mục qua ID
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testDeleteById() {
        Categories category = new Categories();
        category.setName("Category To Delete By Id");
        category.setDescription("This category will be deleted by ID");
        category.setSeo("category-to-delete-by-id");
        Categories savedCategory = categoriesService.saveOrUpdate(category);

        Assertions.assertNotNull(savedCategory.getId(), "Danh mục phải được lưu trước khi xóa");

        categoriesService.deleteById(savedCategory.getId());

        Categories deletedCategory = entityManager.find(Categories.class, savedCategory.getId());
        Assertions.assertNull(deletedCategory, "Danh mục phải được xóa khỏi cơ sở dữ liệu khi xóa bằng ID");
    }
}
