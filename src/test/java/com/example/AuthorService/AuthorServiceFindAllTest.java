package com.example.AuthorService;

import beebooks.StartServer;
import beebooks.entities.Author;
import beebooks.service.AuthorService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class AuthorServiceFindAllTest {

    @Autowired
    private AuthorService authorService;
    //Test 10: Kiểm tra danh sách tác giả
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testFindAll_NotEmpty() {
        List<Author> authors = authorService.findAll();

        Assertions.assertFalse(authors.isEmpty(), "Danh sách tác giả không được rỗng");
    }
    //Test 11: Danh sách tác giả phải chứa ID 52(1 id có trong csdl)
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testFindAll_ContainsKnownAuthor() {
        List<Author> authors = authorService.findAll();

        boolean hasAuthorWithId52 = authors.stream().anyMatch(author -> author.getId() == 52);
        Assertions.assertTrue(hasAuthorWithId52, "Danh sách phải chứa tác giả với ID 52");
    }
}