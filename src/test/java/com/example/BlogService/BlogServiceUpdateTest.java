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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class BlogServiceUpdateTest {

    @Autowired
    private BlogService blogService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 30: Cập nhật blog hợp lệ với avatar và pictures mới
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testUpdateBlog_Success_WithNewAvatarAndPictures() throws IOException {
        // Tạo và lưu Blog ban đầu
        Blog blog = new Blog();
        blog.setTitle("Blog về Lập Trình Java");
        blog.setShortDes("Mô tả ngắn về blog lập trình Java");
        blog.setDetails("Mô tả chi tiết về blog lập trình Java");

        MockMultipartFile initialAvatar = new MockMultipartFile("productAvatar", "initial-avatar.jpg", "image/jpeg", "initial avatar content".getBytes());
        MockMultipartFile initialPicture = new MockMultipartFile("productPictures", "initial-picture.jpg", "image/jpeg", "initial picture content".getBytes());
        MockMultipartFile[] initialPictures = new MockMultipartFile[]{initialPicture};

        Blog savedBlog = blogService.add(blog, initialAvatar, initialPictures);

        // Lấy lại Blog từ cơ sở dữ liệu để đảm bảo trạng thái được quản lý
        savedBlog = entityManager.find(Blog.class, savedBlog.getId());
        entityManager.detach(savedBlog); // Tách ra để mô phỏng cập nhật từ client

        // Khởi tạo blogImages nếu cần, thay vì đặt thành null
        if (savedBlog.getBlogImages() == null) {
            savedBlog.setBlogImages(new HashSet<>()); // Khởi tạo nếu null
        } else {
            savedBlog.getBlogImages().clear(); // Xóa các hình ảnh cũ nếu đã có
        }

        // Cập nhật Blog
        savedBlog.setTitle("Blog về Lập Trình Java Cập Nhật");
        savedBlog.setShortDes("Mô tả ngắn cập nhật");
        savedBlog.setDetails("Mô tả chi tiết cập nhật");

        MockMultipartFile newAvatar = new MockMultipartFile("productAvatar", "new-avatar.jpg", "image/jpeg", "new avatar content".getBytes());
        MockMultipartFile newPicture1 = new MockMultipartFile("productPictures", "new-picture1.jpg", "image/jpeg", "new picture1 content".getBytes());
        MockMultipartFile newPicture2 = new MockMultipartFile("productPictures", "new-picture2.jpg", "image/jpeg", "new picture2 content".getBytes());
        MockMultipartFile[] newPictures = new MockMultipartFile[]{newPicture1, newPicture2};

        Blog updatedBlog = blogService.update(savedBlog, newAvatar, newPictures);

        // Kiểm tra kết quả
        assertEquals(savedBlog.getId(), updatedBlog.getId(), "ID không được thay đổi sau khi cập nhật");
        assertEquals("Blog về Lập Trình Java Cập Nhật", updatedBlog.getTitle(), "Tên blog phải được cập nhật");
        assertEquals("Mô tả ngắn cập nhật", updatedBlog.getShortDes(), "Mô tả ngắn phải được cập nhật");
        assertEquals("Mô tả chi tiết cập nhật", updatedBlog.getDetails(), "Mô tả chi tiết phải được cập nhật");
        assertEquals("product/avatar/new-avatar.jpg", updatedBlog.getAvatar(), "Avatar blog phải được cập nhật");
        assertEquals(new Slugify().slugify("Blog về Lập Trình Java Cập Nhật"), updatedBlog.getSeo(), "SEO blog phải được cập nhật");

        Set<BlogImage> blogImages = updatedBlog.getBlogImages();
        assertEquals(2, blogImages.size(), "Số lượng hình ảnh blog phải là 2");
        assertTrue(blogImages.stream().anyMatch(pi -> pi.getPath().equals("product/pictures/new-picture1.jpg")), "Hình ảnh new-picture1.jpg không tồn tại");
        assertTrue(blogImages.stream().anyMatch(pi -> pi.getPath().equals("product/pictures/new-picture2.jpg")), "Hình ảnh new-picture2.jpg không tồn tại");
    }
    // Test 31: Cập nhật blog mà không thay đổi avatar và pictures
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testUpdateBlog_Success_NoAvatarNoPictures() throws IOException {
        Blog blog = new Blog();
        blog.setTitle("Blog về Lập Trình Python");
        blog.setShortDes("Mô tả ngắn về blog lập trình Python");
        blog.setDetails("Mô tả chi tiết về blog lập trình Python");

        MockMultipartFile initialAvatar = new MockMultipartFile("productAvatar", "initial-avatar.jpg", "image/jpeg", "initial avatar content".getBytes());
        MockMultipartFile[] initialPictures = new MockMultipartFile[]{};

        Blog savedBlog = blogService.add(blog, initialAvatar, initialPictures);

        // Cập nhật blog
        savedBlog.setTitle("Blog về Lập Trình Python Cập Nhật");
        savedBlog.setShortDes("Mô tả ngắn cập nhật");

        MockMultipartFile newAvatar = null;
        MockMultipartFile[] newPictures = null;

        Blog updatedBlog = blogService.update(savedBlog, newAvatar, newPictures);

        assertEquals("product/avatar/initial-avatar.jpg", updatedBlog.getAvatar(), "Avatar blog không được thay đổi");
        assertTrue(updatedBlog.getBlogImages().isEmpty(), "Danh sách hình ảnh blog vẫn phải rỗng");
        assertEquals("Blog về Lập Trình Python Cập Nhật", updatedBlog.getTitle(), "Tên blog phải được cập nhật");
    }

    // Test 32: Cập nhật blog với ID không tồn tại
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testUpdateBlog_Fail_NonExistentId() throws IOException {
        // Tạo một đối tượng Blog với ID không tồn tại
        Blog blog = new Blog();
        blog.setId(999); // ID không tồn tại
        blog.setTitle("Blog về Lập Trình C++");
        blog.setShortDes("Mô tả ngắn");
        blog.setDetails("Mô tả chi tiết");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{};

        // Kiểm tra trước rằng Blog với ID 999 không tồn tại
        Blog existingBlog = entityManager.find(Blog.class, 999);
        assertNull(existingBlog, "Blog với ID 999 không nên tồn tại trước khi cập nhật.");

        // Gọi update
        try {
            blogService.update(blog, avatar, pictures);
        } catch (Exception e) {
            // Bắt ngoại lệ nếu có (ví dụ: IllegalArgumentException), nhưng không fail test
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        // Kiểm tra sau rằng Blog với ID 999 vẫn không tồn tại
        Blog afterUpdateBlog = entityManager.find(Blog.class, 999);
        assertNull(afterUpdateBlog, "Blog với ID 999 không nên tồn tại trong cơ sở dữ liệu sau khi cập nhật vì ID không hợp lệ.");

    }
}