package com.example.AuthorService;

import beebooks.StartServer;
import beebooks.dto.SearchModel;
import beebooks.entities.Author;
import beebooks.service.AuthorService;
import beebooks.service.PagerData;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class AuthorServiceSearchTest {

    @Autowired
    private AuthorService authorService;
    //Test 12: Tìm tác giả có trong csdl
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testSearchWithExistingKeyword() {
        SearchModel searchModel = new SearchModel();
        searchModel.keyword = "Tố Hữu";

        PagerData<Author> result = authorService.search(searchModel);

        Assertions.assertFalse(result.getData().isEmpty(), "Có tác giả Tố Hữu trong danh sách");
    }
    //Test 13: Tìm tác giả không có trong csdl
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testSearchWithExistingKeyword_NotAvailable() {
        SearchModel searchModel = new SearchModel();
        searchModel.keyword = "abcxyz";

        PagerData<Author> result = authorService.search(searchModel);

        Assertions.assertTrue(result.getData().isEmpty(), "Không có trong danh sách");
    }
    //Test 14: Tìm theo id tác giả có trong csdl
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testSearchById() {
        SearchModel searchModel = new SearchModel();
        searchModel.id = 52;

        PagerData<Author> result = authorService.search(searchModel);

        Assertions.assertFalse(result.getData().isEmpty(), "Phải có kết quả với ID tồn tại");
        Assertions.assertEquals(52, result.getData().get(0).getId(), "ID kết quả phải là 52");
    }
    //Test 15: Tìm theo tiểu sử của tác giả có tra csdl
    @Test
    @Order(4)
    @Transactional
    @Rollback
    public void testSearchByBiographyKeyword() {
        SearchModel searchModel = new SearchModel();
        searchModel.keyword = "Tố Hữu là nhà thơ lớn của dân tộc";
        searchModel.setPage(0);

        PagerData<Author> result = authorService.search(searchModel);

        Assertions.assertFalse(result.getData().isEmpty(), "Phải có kết quả nếu biography chứa từ khóa");
    }
    //Test 16: Tìm theo id tác giả không có trong csdl
    @Test
    @Order(5)
    @Transactional
    @Rollback
    public void testSearchByNonExistentId() {
        SearchModel searchModel = new SearchModel();
        searchModel.id = 20; // Assuming ID 20 does not exist in the system

        PagerData<Author> result = authorService.search(searchModel);

        Assertions.assertTrue(result.getData().isEmpty(), "Không có kết quả với ID 20 không tồn tại");
    }
    //Test 17: Tìm theo Tiểu sử của tác giả không có trong csdl
    @Test
    @Order(6)
    @Transactional
    @Rollback
    public void testSearchByNonExistentBiography() {
        SearchModel searchModel = new SearchModel();
        searchModel.keyword = "HTK number one"; // Assuming this biography does not exist

        PagerData<Author> result = authorService.search(searchModel);

        Assertions.assertTrue(result.getData().isEmpty(), "Không có kết quả với tiểu sử 'HTK number one'");
    }
}