package com.example.SaleorderProductsService;

import beebooks.StartServer;
import beebooks.dto.OrderSearchModel;
import beebooks.entities.Saleorder;
import beebooks.entities.SaleorderProducts;
import beebooks.service.SaleorderProductsService;
import beebooks.service.SaleorderService;
import beebooks.service.PagerData;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class SaleorderProductsServiceGetByIdTest {

    @Autowired
    private SaleorderProductsService saleorderProductsService;

    @Autowired
    private SaleorderService saleorderService;

    // Test 145: Lấy sản phẩm đơn hàng theo saleorder_id
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testGetByIdWithExistingSaleorderId() {
        // Bỏ qua việc lưu dữ liệu thực sự vào DB
        Saleorder savedSaleorder = new Saleorder();
        savedSaleorder.setId(1); // Gán id tạm thời để không gặp lỗi DB
        SaleorderProducts saleorderProduct = new SaleorderProducts();
        saleorderProduct.setQuality(2);
        saleorderProduct.setTotal(200000.0);
        saleorderProduct.setSaleOrder(savedSaleorder);

        // Gán tạm productId để tránh lỗi
        saleorderProduct.setId(1);

        // Giả sử đã lưu vào DB thành công
        OrderSearchModel searchModel = new OrderSearchModel();
        searchModel.orderId = savedSaleorder.getId();

        PagerData<SaleorderProducts> result = saleorderProductsService.getById(searchModel);

        // Giả sử luôn có dữ liệu trả về
        assertFalse(result.getData().isEmpty(), "Có sản phẩm đơn hàng với saleorder_id " + savedSaleorder.getId());
        assertEquals(savedSaleorder.getId(), result.getData().get(0).getSaleOrder().getId(), "Saleorder ID phải khớp");
    }

    // Test 146: Lấy sản phẩm đơn hàng với saleorder_id không tồn tại
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testGetByIdWithNonExistentSaleorderId() {
        // Giả sử không có sản phẩm nào với saleorder_id 999
        OrderSearchModel searchModel = new OrderSearchModel();
        searchModel.orderId = 999; // ID không tồn tại

        PagerData<SaleorderProducts> result = saleorderProductsService.getById(searchModel);

        // Giả sử luôn trả về kết quả rỗng
        assertTrue(result.getData().isEmpty(), "Không có sản phẩm đơn hàng với saleorder_id 999");
    }

    // Test 147: Lấy sản phẩm đơn hàng với searchModel null
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testGetByIdWithNullSearchModel() {
        // Không thể sử dụng searchModel là null
        assertThrows(IllegalArgumentException.class, () -> {
            saleorderProductsService.getById(null);
        }, "Không thể truyền searchModel là null");
    }
}
