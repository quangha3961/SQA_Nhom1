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
public class AuthorServiceUpdateTest {

    @Autowired
    private AuthorService authorService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 1 : Cập nhật tác giả thành công
    @Test
    @Order(1)
    @Transactional
    @Rollback(value = true)
    public void testUpdateAuthor_Success() {

        Author author = new Author();
        author.setName("Nguyễn Du");
        author.setBiography("Tác giả của Truyện Kiều");
        Author savedAuthor = authorService.saveOrUpdate(author);

        assertNotNull(savedAuthor.getId(), "Tác giả phải được lưu trước khi cập nhật");

        // Update the author's details
        savedAuthor.setName("Nguyễn Du Updated");
        savedAuthor.setBiography("Tác giả của Truyện Kiều, phiên bản cập nhật");
        Author updatedAuthor = authorService.saveOrUpdate(savedAuthor);

        assertEquals(savedAuthor.getId(), updatedAuthor.getId(), "ID không được thay đổi sau khi cập nhật");
        assertEquals("Nguyễn Du Updated", updatedAuthor.getName(), "Tên tác giả phải được cập nhật");
        assertEquals("Tác giả của Truyện Kiều, phiên bản cập nhật", updatedAuthor.getBiography(), "Tiểu sử phải được cập nhật");

        Author authorFromDb = entityManager.find(Author.class, savedAuthor.getId());
        assertEquals("Nguyễn Du Updated", authorFromDb.getName(), "Tên tác giả trong DB phải được cập nhật");
        assertEquals("Tác giả của Truyện Kiều, phiên bản cập nhật", authorFromDb.getBiography(), "Tiểu sử trong DB phải được cập nhật");

        assertNotNull(updatedAuthor.getUpdatedDate(), "Ngày cập nhật không được để trống");
        assertTrue(updatedAuthor.getUpdatedDate().after(updatedAuthor.getCreatedDate()), "Ngày cập nhật phải sau ngày tạo");
        assertEquals(1, updatedAuthor.getCreatedBy(), "CreatedBy phải được giữ nguyên");
        assertEquals(1, updatedAuthor.getUpdatedBy(), "UpdatedBy phải được thiết lập");
    }


    // Test 2: Cập nhật tác giả với tên quá ngắn (giả sử có ràng buộc)
    @Test
    @Order(2)
    @Transactional
    @Rollback(value = true)
    public void testUpdateAuthor_Fail_NameTooShort() {

        Author author = new Author();
        author.setName("Nguyễn Du");
        author.setBiography("Tác giả của Truyện Kiều");
        Author savedAuthor = authorService.saveOrUpdate(author);

        savedAuthor.setName("AB");
        Author updatedAuthor = authorService.saveOrUpdate(savedAuthor);

        assertNotEquals("AB", updatedAuthor.getName(), "Sai tên tác giả");
    }

    // Test 3: Cập nhật tác giả với tiểu sử null

    @Test
    @Order(3)
    @Transactional
    @Rollback(value = true)
    public void testUpdateAuthor_Fail_BiographyIsNull() {
        Author author = new Author();
        author.setName("Nguyễn Du");
        author.setBiography("Tác giả của Truyện Kiều");
        Author savedAuthor = authorService.saveOrUpdate(author);

        savedAuthor.setBiography(null);

        assertThrows(PersistenceException.class, () -> {
            authorService.saveOrUpdate(savedAuthor);
        }, "Không thể để tiểu sử null");
    }
}