package com.example.AuthorService;

import beebooks.StartServer;
import beebooks.entities.Author;
import beebooks.service.AuthorService;
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
public class AuthorServiceCreateTest {

    @Autowired
    private AuthorService authorService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 4: Thêm tác giả hợp lệ, rollback sau khi test
    @Test
    @Order(1)
    @Rollback(value = true)
    public void testCreateAuthor_SuccessWithRollback() {
        Author author = new Author();
        author.setName("Nguyễn Nhật Ánh");
        author.setBiography("Tác giả nổi tiếng với các tác phẩm văn học thiếu nhi.");
        Author savedAuthor = authorService.saveOrUpdate(author);

        assertNotNull(savedAuthor.getId(), "Id không được để trống.");
        assertEquals("Nguyễn Nhật Ánh", savedAuthor.getName(), "Tên tác giả không đúng.");

        Author authorFromDb = entityManager.find(Author.class, savedAuthor.getId());
        assertNotNull(authorFromDb, "Tác giả không được tìm thấy trong DB.");
    }

    // Test 5: Thêm tác giả không có tên (dùng assertThrows)
    @Test
    @Order(3)
    public void testCreateAuthor_Fail_NameIsNull() {
        Author author = new Author();
        author.setBiography("Tác giả không có tên.");

        assertThrows(PersistenceException.class, () -> {
            authorService.saveOrUpdate(author);
        }, "Expected PersistenceException when name is null");
    }

    // Test 6: Tên tác giả quá ngắn
    @Test
    @Order(4)
    public void testCreateAuthor_Fail_NameTooShort() {
        Author author = new Author();
        author.setName("ABC");
        author.setBiography("Tác giả mới.");

        Author savedAuthor = authorService.saveOrUpdate(author);

        assertNull(savedAuthor.getId(), "Tác giả có tên ngắn không được lưu.");
    }

    // Test 7: Thêm tác giả thiếu tiểu sử
    @Test
    @Order(5)
    public void testCreateAuthor_Fail_BiographyIsNull() {
        Author author = new Author();
        author.setName("Nguyễn Văn Chính");  // Để trống tiểu sử

        assertThrows(PersistenceException.class, () -> {
            authorService.saveOrUpdate(author);
        }, "Dự kiến sẽ xảy ra lỗi PersistenceException khi tiểu sử để trống.");
    }
}
