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
public class ProductServiceUpdateTest {

    @Autowired
    private ProductService productService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 104: Cập nhật sản phẩm thành công với avatar và pictures mới
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testUpdateProduct_Success_WithNewAvatarAndPictures() throws IOException {
        Product product = new Product();
        product.setTitle("Sách Lập Trình Java");
        product.setPrice(BigDecimal.valueOf(100000));
        product.setPrice_sale(BigDecimal.valueOf(90000));
        product.setPublicationYear("2023");
        product.setQuantity(50);
        product.setShortDes("Mô tả ngắn về sách lập trình Java");
        product.setDetails("Mô tả chi tiết về sách lập trình Java");

        MockMultipartFile initialAvatar = new MockMultipartFile("productAvatar", "initial-avatar.jpg", "image/jpeg", "initial avatar content".getBytes());
        MockMultipartFile initialPicture = new MockMultipartFile("productPictures", "initial-picture.jpg", "image/jpeg", "initial picture content".getBytes());
        MockMultipartFile[] initialPictures = new MockMultipartFile[]{initialPicture};

        Product savedProduct = productService.add(product, initialAvatar, initialPictures);

        // Cập nhật sản phẩm
        savedProduct.setTitle("Sách Lập Trình Java Cập Nhật");
        savedProduct.setPrice(BigDecimal.valueOf(120000));
        savedProduct.setPrice_sale(BigDecimal.valueOf(110000));
        savedProduct.setShortDes("Mô tả ngắn cập nhật");
        savedProduct.setDetails("Mô tả chi tiết cập nhật");

        // Xóa tất cả các hình ảnh cũ nếu có
        savedProduct.getProductImage().clear();  // Xóa tất cả các hình ảnh cũ

        MockMultipartFile newAvatar = new MockMultipartFile("productAvatar", "new-avatar.jpg", "image/jpeg", "new avatar content".getBytes());
        MockMultipartFile newPicture1 = new MockMultipartFile("productPictures", "new-picture1.jpg", "image/jpeg", "new picture1 content".getBytes());
        MockMultipartFile newPicture2 = new MockMultipartFile("productPictures", "new-picture2.jpg", "image/jpeg", "new picture2 content".getBytes());
        MockMultipartFile[] newPictures = new MockMultipartFile[]{newPicture1, newPicture2};

        Product updatedProduct = productService.update(savedProduct, newAvatar, newPictures);

        assertEquals(savedProduct.getId(), updatedProduct.getId(), "ID không được thay đổi sau khi cập nhật");
        assertEquals("Sách Lập Trình Java Cập Nhật", updatedProduct.getTitle(), "Tên sản phẩm phải được cập nhật");
        assertEquals(BigDecimal.valueOf(120000), updatedProduct.getPrice(), "Giá sản phẩm phải được cập nhật");
        assertEquals(BigDecimal.valueOf(110000), updatedProduct.getPrice_sale(), "Giá khuyến mãi phải được cập nhật");
        assertEquals("Mô tả ngắn cập nhật", updatedProduct.getShortDes(), "Mô tả ngắn phải được cập nhật");
        assertEquals("Mô tả chi tiết cập nhật", updatedProduct.getDetails(), "Mô tả chi tiết phải được cập nhật");
        assertEquals("product/avatar/new-avatar.jpg", updatedProduct.getAvatar(), "Avatar sản phẩm phải được cập nhật");
        assertEquals(new Slugify().slugify("Sách Lập Trình Java Cập Nhật"), updatedProduct.getSeo(), "SEO sản phẩm phải được cập nhật");
        assertEquals(1, updatedProduct.getProductStatus(), "Trạng thái sản phẩm không được thay đổi");

        Set<ProductImage> productImages = updatedProduct.getProductImage();
        assertEquals(2, productImages.size(), "Số lượng hình ảnh sản phẩm phải là 2");
        assertTrue(productImages.stream().anyMatch(pi -> pi.getPath().equals("product/pictures/new-picture1.jpg")), "Hình ảnh new-picture1.jpg không tồn tại");
        assertTrue(productImages.stream().anyMatch(pi -> pi.getPath().equals("product/pictures/new-picture2.jpg")), "Hình ảnh new-picture2.jpg không tồn tại");
    }


    // Test 105: Cập nhật sản phẩm thất bại khi avatar và pictures là null
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testUpdateProduct_Fail_AvatarAndPicturesNull() throws IOException {
        Product product = new Product();
        product.setTitle("Sách Lập Trình Python");
        product.setPrice(BigDecimal.valueOf(120000));
        product.setPrice_sale(null);
        product.setPublicationYear("2022");
        product.setQuantity(30);
        product.setShortDes("Mô tả ngắn về sách lập trình Python");
        product.setDetails("Mô tả chi tiết về sách lập trình Python");

        MockMultipartFile initialAvatar = new MockMultipartFile("productAvatar", "initial-avatar.jpg", "image/jpeg", "initial avatar content".getBytes());
        MockMultipartFile[] initialPictures = new MockMultipartFile[]{};  // Không có ảnh

        Product savedProduct = productService.add(product, initialAvatar, initialPictures);

        // Cập nhật sản phẩm
        savedProduct.setTitle("Sách Lập Trình Python Cập Nhật");
        savedProduct.setPrice(BigDecimal.valueOf(130000));

        MockMultipartFile newAvatar = null;
        MockMultipartFile[] newPictures = null;

        Product updatedProduct = productService.update(savedProduct, newAvatar, newPictures);

        assertEquals("product/avatar/initial-avatar.jpg", updatedProduct.getAvatar(), "Avatar sản phẩm không được thay đổi");
        assertTrue(updatedProduct.getProductImage().isEmpty(), "Danh sách hình ảnh sản phẩm vẫn phải rỗng");
        assertEquals("Sách Lập Trình Python Cập Nhật", updatedProduct.getTitle(), "Tên sản phẩm phải được cập nhật");
    }

    // Test 106: Cập nhật sản phẩm thất bại khi ID không tồn tại
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testUpdateProduct_Fail_NonExistentId() throws IOException {
        Product product = new Product();
        product.setId(999); // ID không tồn tại
        product.setTitle("Sách Lập Trình C++");
        product.setPrice(BigDecimal.valueOf(150000));
        product.setShortDes("Mô tả ngắn");
        product.setDetails("Mô tả chi tiết");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{};

        // Đảm bảo có lỗi xảy ra khi cố gắng cập nhật với ID không tồn tại
        assertThrows(IllegalArgumentException.class, () -> {
            productService.update(product, avatar, pictures);
        }, "Dự kiến sẽ xảy ra lỗi khi cố gắng cập nhật với ID không tồn tại");
    }
}
