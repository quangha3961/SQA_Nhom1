package com.example.ProductService;

import beebooks.StartServer;
import beebooks.entities.Product;
import beebooks.entities.ProductImage;
import beebooks.service.ProductService;
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
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class ProductServiceAddTest {

    @Autowired
    private ProductService productService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 107: Thêm sản phẩm thành công với avatar và pictures hợp lệ
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testAddProduct_Success_WithAvatarAndPictures() throws IOException {
        Product product = new Product();
        product.setTitle("Sách Lập Trình Java");
        product.setPrice(BigDecimal.valueOf(100000));
        product.setPrice_sale(BigDecimal.valueOf(90000));
        product.setPublicationYear("2023");
        product.setQuantity(50);
        product.setShortDes("Mô tả ngắn về sách lập trình Java");
        product.setDetails("Mô tả chi tiết về sách lập trình Java");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile picture1 = new MockMultipartFile("productPictures", "picture1.jpg", "image/jpeg", "picture1 content".getBytes());
        MockMultipartFile picture2 = new MockMultipartFile("productPictures", "picture2.jpg", "image/jpeg", "picture2 content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{picture1, picture2};

        Product savedProduct = productService.add(product, avatar, pictures);

        assertNotNull(savedProduct.getId(), "ID sản phẩm không được để trống");
        assertEquals("Sách Lập Trình Java", savedProduct.getTitle(), "Tên sản phẩm không đúng");
        assertEquals(BigDecimal.valueOf(100000), savedProduct.getPrice(), "Giá sản phẩm không đúng");
        assertEquals(BigDecimal.valueOf(90000), savedProduct.getPrice_sale(), "Giá khuyến mãi không đúng");
        assertEquals("2023", savedProduct.getPublicationYear(), "Năm xuất bản không đúng");
        assertEquals(50, savedProduct.getQuantity(), "Số lượng không đúng");
        assertEquals("Mô tả ngắn về sách lập trình Java", savedProduct.getShortDes(), "Mô tả ngắn không đúng");
        assertEquals("Mô tả chi tiết về sách lập trình Java", savedProduct.getDetails(), "Mô tả chi tiết không đúng");
        assertEquals("product/avatar/avatar.jpg", savedProduct.getAvatar(), "Avatar sản phẩm không đúng");
        assertEquals(new Slugify().slugify("Sách Lập Trình Java"), savedProduct.getSeo(), "SEO sản phẩm không đúng");
        assertEquals(1, savedProduct.getProductStatus(), "Trạng thái sản phẩm không đúng");

        Set<ProductImage> productImages = savedProduct.getProductImage();
        assertEquals(2, productImages.size(), "Số lượng hình ảnh sản phẩm không đúng");
        assertTrue(productImages.stream().anyMatch(pi -> pi.getPath().equals("product/pictures/picture1.jpg")), "Hình ảnh picture1.jpg không tồn tại");
        assertTrue(productImages.stream().anyMatch(pi -> pi.getPath().equals("product/pictures/picture2.jpg")), "Hình ảnh picture2.jpg không tồn tại");

        Product productFromDb = entityManager.find(Product.class, savedProduct.getId());
        assertNotNull(productFromDb, "Sản phẩm không được tìm thấy trong DB");
    }

    // Test 108: Thêm sản phẩm  chỉ với avatar, không có pictures
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testAddProduct_Success_WithAvatarOnly() throws IOException {
        Product product = new Product();
        product.setTitle("Sách Lập Trình Python");
        product.setPrice(BigDecimal.valueOf(120000));
        product.setPrice_sale(null);
        product.setPublicationYear("2022");
        product.setQuantity(30);
        product.setShortDes("Mô tả ngắn về sách lập trình Python");
        product.setDetails("Mô tả chi tiết về sách lập trình Python");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{};

        Product savedProduct = productService.add(product, avatar, pictures);

        assertNotNull(savedProduct.getId(), "ID sản phẩm không được để trống");
        assertEquals("product/avatar/avatar.jpg", savedProduct.getAvatar(), "Avatar sản phẩm không đúng");
        assertTrue(savedProduct.getProductImage().isEmpty(), "Danh sách hình ảnh sản phẩm phải rỗng");
    }

    // Test 109: Thêm sản phẩm thất bại khi title là null
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testAddProduct_Fail_TitleIsNull() throws IOException {
        Product product = new Product();
        product.setTitle(null); // Title không được null
        product.setPrice(BigDecimal.valueOf(100000));
        product.setShortDes("Mô tả ngắn");
        product.setDetails("Mô tả chi tiết");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{};

        assertThrows(Exception.class, () -> {
            productService.add(product, avatar, pictures);
            entityManager.flush(); // Đảm bảo flush để kiểm tra ràng buộc null
        }, "Thêm sản phẩm thất bại vì title không được để null");

        assertFalse(true, "Test thất bại vì tham số null không được phép");
    }

    // Test 110: Thêm sản phẩm thất bại khi avatar và pictures là null
    @Test
    @Order(4)
    @Transactional
    @Rollback
    public void testAddProduct_Fail_NoAvatarNoPictures() throws IOException {
        Product product = new Product();
        product.setTitle("Sách Lập Trình C++");
        product.setPrice(BigDecimal.valueOf(150000));
        product.setPrice_sale(null);
        product.setPublicationYear("2021");
        product.setQuantity(20);
        product.setShortDes("Mô tả ngắn về sách lập trình C++");
        product.setDetails("Mô tả chi tiết về sách lập trình C++");

        MockMultipartFile avatar = null;
        MockMultipartFile[] pictures = null;

        Product savedProduct = productService.add(product, avatar, pictures);

        assertNotNull(savedProduct.getId(), "ID sản phẩm không được để trống");
        assertNull(savedProduct.getAvatar(), "Avatar sản phẩm là null");
        assertTrue(savedProduct.getProductImage().isEmpty(), "Danh sách hình ảnh sản phẩm phải rỗng");

        assertFalse(true, "Test thất bại vì tham số null không được phép");
    }
}