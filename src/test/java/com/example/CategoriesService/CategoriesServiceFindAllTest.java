package com.example.CategoriesService;

import beebooks.StartServer;
import beebooks.entities.Categories;
import beebooks.service.CategoriesService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class CategoriesServiceFindAllTest {

    @Autowired
    private CategoriesService categoriesService;

    // Test 64: Kiểm tra danh sách không rỗng khi có ít nhất một danh mục
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testFindAll_NotEmpty() {
        // Tạo và lưu danh mục để đảm bảo danh sách không rỗng
        Categories category = new Categories();
        category.setName("Sách Văn Học");
        category.setDescription("Danh mục sách văn học Việt Nam và thế giới");
        category.setSeo("sach-van-hoc");
        categoriesService.saveOrUpdate(category);

        List<Categories> categories = categoriesService.findAll();

        Assertions.assertFalse(categories.isEmpty(), "Danh sách danh mục không được rỗng");
    }

    // Test 65: Kiểm tra danh sách chứa một danh mục đã lưu
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testFindAll_ContainsKnownCategory() {
        // Tạo và lưu danh mục để đảm bảo chúng ta có một danh mục đã biết
        Categories category = new Categories();
        category.setName("Sách Văn Học");
        category.setDescription("Danh mục sách văn học Việt Nam và thế giới");
        category.setSeo("sach-van-hoc");
        Categories savedCategory = categoriesService.saveOrUpdate(category);

        List<Categories> categories = categoriesService.findAll();

        boolean hasCategoryWithKnownId = categories.stream().anyMatch(c -> c.getId() == savedCategory.getId());
        Assertions.assertTrue(hasCategoryWithKnownId, "Danh sách phải chứa danh mục với ID " + savedCategory.getId());

        Categories foundCategory = categories.stream().filter(c -> c.getId() == savedCategory.getId()).findFirst().orElse(null);
        Assertions.assertNotNull(foundCategory, "Danh mục phải tồn tại trong danh sách");
        Assertions.assertEquals("Sách Văn Học", foundCategory.getName(), "Tên danh mục phải khớp");
        Assertions.assertEquals("Danh mục sách văn học Việt Nam và thế giới", foundCategory.getDescription(), "Mô tả danh mục phải khớp");
        Assertions.assertEquals("sach-van-hoc", foundCategory.getSeo(), "SEO danh mục phải khớp");
    }
}
