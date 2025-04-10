package com.example.BlogService;

import beebooks.StartServer;
import beebooks.dto.BlogSearchModel;
import beebooks.entities.Blog;
import beebooks.service.PagerData;
import beebooks.service.impl.BlogService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class BlogServiceSearchTest {

    @Autowired
    private BlogService blogService;

    // Test 24: Tìm blog theo SEO
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testSearchWithExistingSeo() throws IOException {
        Blog blog = new Blog();
        blog.setTitle("Blog về Lập Trình Java");
        blog.setShortDes("Mô tả ngắn về blog lập trình Java");
        blog.setDetails("Mô tả chi tiết về blog lập trình Java");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{};
        blogService.add(blog, avatar, pictures);

        BlogSearchModel searchModel = new BlogSearchModel();
        searchModel.seo = blog.getSeo();

        PagerData<Blog> result = blogService.search(searchModel);

        assertFalse(result.getData().isEmpty(), "Có blog với SEO '" + blog.getSeo() + "' trong danh sách");
    }

    // Test 25: Tìm blog theo từ khóa (title)
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testSearchWithExistingKeywordInTitle() throws IOException {
        Blog blog = new Blog();
        blog.setTitle("Blog về Lập Trình Python");
        blog.setShortDes("Mô tả ngắn về blog lập trình Python");
        blog.setDetails("Mô tả chi tiết về blog lập trình Python");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{};
        blogService.add(blog, avatar, pictures);

        BlogSearchModel searchModel = new BlogSearchModel();
        searchModel.keyword = "Python";

        PagerData<Blog> result = blogService.search(searchModel);

        assertFalse(result.getData().isEmpty(), "Có blog với từ khóa 'Python' trong tiêu đề");
    }

    // Test 26: Tìm blog theo từ khóa
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testSearchWithExistingKeywordInShortDescription() throws IOException {
        Blog blog = new Blog();
        blog.setTitle("Blog về Lập Trình C++");
        blog.setShortDes("Mô tả ngắn về blog lập trình C++");
        blog.setDetails("Mô tả chi tiết về blog lập trình C++");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{};
        blogService.add(blog, avatar, pictures);

        BlogSearchModel searchModel = new BlogSearchModel();
        searchModel.keyword = "mô tả ngắn";

        PagerData<Blog> result = blogService.search(searchModel);

        assertFalse(result.getData().isEmpty(), "Có blog với từ khóa 'mô tả ngắn' trong mô tả ngắn");
    }

    // Test 27: Tìm blog theo từ khóa (detail_description)
    @Test
    @Order(4)
    @Transactional
    @Rollback
    public void testSearchWithExistingKeywordInDetailDescription() throws IOException {
        Blog blog = new Blog();
        blog.setTitle("Blog về Lập Trình Ruby");
        blog.setShortDes("Mô tả ngắn về blog lập trình Ruby");
        blog.setDetails("Mô tả chi tiết về blog lập trình Ruby");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{};
        blogService.add(blog, avatar, pictures);

        BlogSearchModel searchModel = new BlogSearchModel();
        searchModel.keyword = "chi tiết";

        PagerData<Blog> result = blogService.search(searchModel);

        assertFalse(result.getData().isEmpty(), "Có blog với từ khóa 'chi tiết' trong mô tả chi tiết");
    }

    // Test 28: Tìm blog với từ khóa không tồn tại
    @Test
    @Order(5)
    @Transactional
    @Rollback
    public void testSearchWithNonExistentKeyword() {
        BlogSearchModel searchModel = new BlogSearchModel();
        searchModel.keyword = "abcxyz";

        PagerData<Blog> result = blogService.search(searchModel);

        assertTrue(result.getData().isEmpty(), "Không có blog với từ khóa 'abcxyz' trong danh sách");
    }

    // Test 29: Tìm blog với categoryId (không set categoryBlog, nên trả về tất cả)
    @Test
    @Order(6)
    @Transactional
    @Rollback
    public void testSearchWithCategoryId() throws IOException {
        Blog blog = new Blog();
        blog.setTitle("Blog về Lập Trình Go");
        blog.setShortDes("Mô tả ngắn về blog lập trình Go");
        blog.setDetails("Mô tả chi tiết về blog lập trình Go");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{};
        blogService.add(blog, avatar, pictures);

        BlogSearchModel searchModel = new BlogSearchModel();
        searchModel.categoryId = 1; // Không set categoryBlog, nên không ảnh hưởng

        PagerData<Blog> result = blogService.search(searchModel);

        assertFalse(result.getData().isEmpty(), "Phải có kết quả vì không set categoryBlog");
    }
}