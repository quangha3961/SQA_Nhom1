package com.example.BlogService;

import beebooks.StartServer;
import beebooks.entities.Blog;
import beebooks.entities.BlogImage;
import beebooks.service.impl.BlogService;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class BlogServiceAddTest {

    @Autowired
    private BlogService blogService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 18: Thêm blog hợp lệ với avatar và pictures
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testAddBlog_Success_WithAvatarAndPictures() throws IOException {
        Blog blog = new Blog();
        blog.setTitle("Blog về Lập Trình Java");
        blog.setShortDes("Mô tả ngắn về blog lập trình Java");
        blog.setDetails("Mô tả chi tiết về blog lập trình Java");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile picture1 = new MockMultipartFile("productPictures", "picture1.jpg", "image/jpeg", "picture1 content".getBytes());
        MockMultipartFile picture2 = new MockMultipartFile("productPictures", "picture2.jpg", "image/jpeg", "picture2 content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{picture1, picture2};

        Blog savedBlog = blogService.add(blog, avatar, pictures);

        assertNotNull(savedBlog.getId(), "ID blog không được để trống");
        assertEquals("Blog về Lập Trình Java", savedBlog.getTitle(), "Tên blog không đúng");
        assertEquals("Mô tả ngắn về blog lập trình Java", savedBlog.getShortDes(), "Mô tả ngắn không đúng");
        assertEquals("Mô tả chi tiết về blog lập trình Java", savedBlog.getDetails(), "Mô tả chi tiết không đúng");
        assertEquals("product/avatar/avatar.jpg", savedBlog.getAvatar(), "Avatar blog không đúng");
        assertEquals(new Slugify().slugify("Blog về Lập Trình Java"), savedBlog.getSeo(), "SEO blog không đúng");

        Set<BlogImage> blogImages = savedBlog.getBlogImages();
        assertEquals(2, blogImages.size(), "Số lượng hình ảnh blog không đúng");
        assertTrue(blogImages.stream().anyMatch(pi -> pi.getPath().equals("product/pictures/picture1.jpg")), "Hình ảnh picture1.jpg không tồn tại");
        assertTrue(blogImages.stream().anyMatch(pi -> pi.getPath().equals("product/pictures/picture2.jpg")), "Hình ảnh picture2.jpg không tồn tại");

        Blog blogFromDb = entityManager.find(Blog.class, savedBlog.getId());
        assertNotNull(blogFromDb, "Blog không được tìm thấy trong DB");
    }

    // Test 19: Thêm blog hợp lệ chỉ với avatar, không có pictures
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testAddBlog_Success_WithAvatarOnly() throws IOException {
        Blog blog = new Blog();
        blog.setTitle("Blog về Lập Trình Python");
        blog.setShortDes("Mô tả ngắn về blog lập trình Python");
        blog.setDetails("Mô tả chi tiết về blog lập trình Python");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{};

        Blog savedBlog = blogService.add(blog, avatar, pictures);

        assertNotNull(savedBlog.getId(), "ID blog không được để trống");
        assertEquals("product/avatar/avatar.jpg", savedBlog.getAvatar(), "Avatar blog không đúng");
        assertTrue(savedBlog.getBlogImages().isEmpty(), "Danh sách hình ảnh blog phải rỗng");
    }

    // Test 20: Thêm blog với title null
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testAddBlog_Fail_TitleIsNull() throws IOException {
        Blog blog = new Blog();
        blog.setTitle(null); // Title is nullable = false
        blog.setShortDes("Mô tả ngắn");
        blog.setDetails("Mô tả chi tiết");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{};

        assertThrows(Exception.class, () -> {
            blogService.add(blog, avatar, pictures);
        }, "Dự kiến sẽ xảy ra lỗi khi title là null");
    }

    // Test 21: Thêm blog với avatar null và pictures null
    @Test
    @Order(4)
    @Transactional
    @Rollback
    public void testAddBlog_Success_NoAvatarNoPictures() throws IOException {
        Blog blog = new Blog();
        blog.setTitle("Blog về Lập Trình C++");
        blog.setShortDes("Mô tả ngắn về blog lập trình C++");
        blog.setDetails("Mô tả chi tiết về blog lập trình C++");

        MockMultipartFile avatar = null;
        MockMultipartFile[] pictures = null;

        Blog savedBlog = blogService.add(blog, avatar, pictures);

        assertNotNull(savedBlog.getId(), "ID blog không được để trống");
        assertNotNull(savedBlog.getAvatar(), "Avatar blog phải là null");
        assertTrue(savedBlog.getBlogImages().isEmpty(), "Danh sách hình ảnh blog phải rỗng");
    }
}