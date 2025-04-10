package com.example.AuthorService;

import beebooks.StartServer;
import beebooks.entities.Author;
import beebooks.service.AuthorService;
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
public class AuthorServiceDeleteTest {

    @Autowired
    private AuthorService authorService;

    @PersistenceContext
    private EntityManager entityManager;
    //Test 8: Tạo 1 tác giả để xóa, xoá theo tác giả
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testDelete_AuthorEntity() {
        Author author = new Author();
        author.setName("Author To Delete");
        author.setBiography("This author will be deleted");
        Author savedAuthor = authorService.saveOrUpdate(author);


        Assertions.assertNotNull(savedAuthor.getId(), "Tác giả phải được lưu trước khi xóa");

        authorService.delete(savedAuthor);

        Author deletedAuthor = entityManager.find(Author.class, savedAuthor.getId());
        Assertions.assertNull(deletedAuthor, "Tác giả phải được xóa khỏi cơ sở dữ liệu");
    }
    //Test 9: Xóa theo id tác giả
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testDeleteById() {
        // Create a new author to delete
        Author author = new Author();
        author.setName("Author To Delete By Id");
        author.setBiography("This author will be deleted by ID");
        Author savedAuthor = authorService.saveOrUpdate(author);

        Assertions.assertNotNull(savedAuthor.getId(), "Tác giả phải được lưu trước khi xóa");

        authorService.deleteById(savedAuthor.getId());

        Author deletedAuthor = entityManager.find(Author.class, savedAuthor.getId());
        Assertions.assertNull(deletedAuthor, "Tác giả phải được xóa khỏi cơ sở dữ liệu khi xóa bằng ID");
    }
}