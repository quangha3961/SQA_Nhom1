package com.example.ProductService;

import beebooks.StartServer;
import beebooks.dto.ProductSearchModel;
import beebooks.entities.Product;
import beebooks.service.PagerData;
import beebooks.service.ProductService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class ProductServiceSearchTest {

    @Autowired
    private ProductService productService;

    // Test 99: Tìm sản phẩm theo SEO
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testSearchWithExistingSeo() throws IOException {
        Product product = new Product();
        product.setTitle("Sách Lập Trình Java");
        product.setPrice(BigDecimal.valueOf(100000));
        product.setShortDes("Mô tả ngắn về sách lập trình Java");
        product.setDetails("Mô tả chi tiết về sách lập trình Java");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{};
        productService.add(product, avatar, pictures);

        ProductSearchModel searchModel = new ProductSearchModel();
        searchModel.seo = product.getSeo();

        PagerData<Product> result = productService.search(searchModel);

        Assertions.assertFalse(result.getData().isEmpty(), "Có sản phẩm với SEO '" + product.getSeo() + "' trong danh sách");
    }

    // Test 100: Tìm sản phẩm theo từ khóa (title)
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testSearchWithExistingKeywordInTitle() throws IOException {
        Product product = new Product();
        product.setTitle("Sách Lập Trình Python");
        product.setPrice(BigDecimal.valueOf(120000));
        product.setShortDes("Mô tả ngắn về sách lập trình Python");
        product.setDetails("Mô tả chi tiết về sách lập trình Python");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{};
        productService.add(product, avatar, pictures);

        ProductSearchModel searchModel = new ProductSearchModel();
        searchModel.keyword = "Python";

        PagerData<Product> result = productService.search(searchModel);

        Assertions.assertFalse(result.getData().isEmpty(), "Có sản phẩm với từ khóa 'Python' trong tiêu đề");
    }

    // Test 101: Tìm sản phẩm theo từ khóa (short_description)
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testSearchWithExistingKeywordInShortDescription() throws IOException {
        Product product = new Product();
        product.setTitle("Sách Lập Trình C++");
        product.setPrice(BigDecimal.valueOf(150000));
        product.setShortDes("Mô tả ngắn về sách lập trình C++");
        product.setDetails("Mô tả chi tiết về sách lập trình C++");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{};
        productService.add(product, avatar, pictures);

        ProductSearchModel searchModel = new ProductSearchModel();
        searchModel.keyword = "mô tả ngắn";

        PagerData<Product> result = productService.search(searchModel);

        Assertions.assertFalse(result.getData().isEmpty(), "Có sản phẩm với từ khóa 'mô tả ngắn' trong mô tả ngắn");
    }

    // Test 102:Tìm sản phẩm theo từ khóa (detail_description)
    @Test
    @Order(4)
    @Transactional
    @Rollback
    public void testSearchWithExistingKeywordInDetailDescription() throws IOException {
        Product product = new Product();
        product.setTitle("Sách Lập Trình Ruby");
        product.setPrice(BigDecimal.valueOf(180000));
        product.setShortDes("Mô tả ngắn về sách lập trình Ruby");
        product.setDetails("Mô tả chi tiết về sách lập trình Ruby");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{};
        productService.add(product, avatar, pictures);

        ProductSearchModel searchModel = new ProductSearchModel();
        searchModel.keyword = "chi tiết";

        PagerData<Product> result = productService.search(searchModel);

        Assertions.assertFalse(result.getData().isEmpty(), "Có sản phẩm với từ khóa 'chi tiết' trong mô tả chi tiết");
    }

    // Test 103: Tìm sản phẩm với từ khóa không tồn tại
    @Test
    @Order(5)
    @Transactional
    @Rollback
    public void testSearchWithNonExistentKeyword() {
        ProductSearchModel searchModel = new ProductSearchModel();
        searchModel.keyword = "abcxyz";

        PagerData<Product> result = productService.search(searchModel);

        Assertions.assertTrue(result.getData().isEmpty(), "Không có sản phẩm với từ khóa 'abcxyz' trong danh sách");
    }
}