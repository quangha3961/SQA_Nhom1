package com.example.ProductService;

import beebooks.StartServer;
import beebooks.entities.Product;
import beebooks.service.ProductService;
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

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class ProductServiceDeleteTest {

    @Autowired
    private ProductService productService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 111: Xóa sản phẩm hợp lệ
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testRemoveProduct_Success() throws IOException {
        Product product = new Product();
        product.setTitle("Sách Lập Trình Java");
        product.setPrice(BigDecimal.valueOf(100000));
        product.setPrice_sale(BigDecimal.valueOf(90000));
        product.setPublicationYear("2023");
        product.setQuantity(50);
        product.setShortDes("Mô tả ngắn về sách lập trình Java");
        product.setDetails("Mô tả chi tiết về sách lập trình Java");

        MockMultipartFile avatar = new MockMultipartFile("productAvatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        MockMultipartFile picture = new MockMultipartFile("productPictures", "picture.jpg", "image/jpeg", "picture content".getBytes());
        MockMultipartFile[] pictures = new MockMultipartFile[]{picture};

        Product savedProduct = productService.add(product, avatar, pictures);

        assertNotNull(savedProduct.getId(), "Sản phẩm phải được lưu trước khi xóa");

        productService.remove(savedProduct);

        Product deletedProduct = entityManager.find(Product.class, savedProduct.getId());
        assertNull(deletedProduct, "Sản phẩm phải được xóa khỏi cơ sở dữ liệu");
    }

    // Test 112: Xóa sản phẩm với ID không tồn tại
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testRemoveProduct_NonExistentId() {
        Product product = new Product();
        product.setId(999); // ID không tồn tại

        // Kiểm tra xem sản phẩm có tồn tại trong cơ sở dữ liệu không
        Product existingProduct = entityManager.find(Product.class, product.getId());

        if (existingProduct != null) {
            // Nếu sản phẩm tồn tại, thực hiện xóa
            productService.remove(existingProduct);
        } else {
            // Nếu sản phẩm không tồn tại, không làm gì hoặc log thông báo
            System.out.println("Sản phẩm với ID " + product.getId() + " không tồn tại trong DB.");
        }

        Product deletedProduct = entityManager.find(Product.class, product.getId());
        assertNull(deletedProduct, "Sản phẩm với ID không tồn tại nên không có trong DB");
    }

}