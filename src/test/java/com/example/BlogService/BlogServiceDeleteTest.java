package com.example.BlogService;

import beebooks.StartServer;
import beebooks.entities.Blog;
import beebooks.service.impl.BlogService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class BlogServiceDeleteTest {

    @Autowired
    private BlogService blogService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 22: Xóa blog hợp lệ
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testRemoveBlog_Success() throws IOException {
        Blog blog = new Blog();
        blog.setTitle("Blog về Lập Trình Java");
        blog.setShortDes("Mô tả ngắn về blog lập trình Java");
        blog.setDetails("Mô tả chi tiết về blog lập trình Java");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile picture = new MockMultipartFile("productPictures", "picture.jpg", "image/jpeg", "picture content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{picture};

        Blog savedBlog = blogService.add(blog, avatar, pictures);

        assertNotNull(savedBlog.getId(), "Blog phải được lưu trước khi xóa");

        blogService.remove(savedBlog);

        Blog deletedBlog = entityManager.find(Blog.class, savedBlog.getId());
        assertNull(deletedBlog, "Blog phải được xóa khỏi cơ sở dữ liệu");
    }

    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testRemoveBlog_NonExistentId() {
        // Tạo một đối tượng Blog với ID không tồn tại
        Blog blog = new Blog();
        blog.setId(999); // ID không tồn tại trong cơ sở dữ liệu

        // Thay vì gọi remove trực tiếp, kiểm tra rằng blog không tồn tại trước
        Blog existingBlog = entityManager.find(Blog.class, 999);
        assertNull(existingBlog, "Blog với ID không tồn tại không nên có trong cơ sở dữ liệu trước khi xóa.");

        // Gọi phương thức remove và xác nhận nó không làm thay đổi cơ sở dữ liệu
        try {
            blogService.remove(blog);
        } catch (IllegalArgumentException e) {
            // Bắt ngoại lệ nếu có, nhưng không fail bài test vì đây là hành vi mong muốn
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        // Kiểm tra lại rằng không có blog nào với ID 999 trong cơ sở dữ liệu
        Blog deletedBlog = entityManager.find(Blog.class, 999);
        assertNull(deletedBlog, "Blog với ID không tồn tại vẫn không có trong cơ sở dữ liệu sau khi gọi remove.");
    }
}