package com.example.SaleorderService;

import beebooks.StartServer;
import beebooks.dto.OrderSearchModel;
import beebooks.entities.Saleorder;
import beebooks.service.PagerData;
import beebooks.service.SaleorderService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class SaleorderServiceSearchTest {

    @Autowired
    private SaleorderService saleorderService;

    // Test 136: Tìm đơn hàng theo customer_name
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testSearchWithExistingCustomerName() {
        Saleorder saleorder = new Saleorder();
        saleorder.setCode("ORDER001");
        saleorder.setCustomer_name("Nguyen Van A");
        saleorder.setCustomer_address("123 Đường Láng, Hà Nội");
        saleorder.setCustomer_phone("0123456789");
        saleorder.setCustomer_email("nguyenvana@example.com");
        saleorderService.saveOrUpdate(saleorder);

        OrderSearchModel searchModel = new OrderSearchModel();
        searchModel.keyword = "Nguyen Van A";

        PagerData<Saleorder> result = saleorderService.search(searchModel);

        assertFalse(result.getData().isEmpty(), "Có đơn hàng với tên khách hàng 'Nguyen Van A' trong danh sách");
    }

    // Test 137: Tìm đơn hàng theo customer_email
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testSearchWithExistingCustomerEmail() {
        Saleorder saleorder = new Saleorder();
        saleorder.setCode("ORDER002");
        saleorder.setCustomer_name("Nguyen Van B");
        saleorder.setCustomer_email("nguyenvanb@example.com");
        saleorderService.saveOrUpdate(saleorder);

        OrderSearchModel searchModel = new OrderSearchModel();
        searchModel.keyword = "nguyenvanb@example.com";

        PagerData<Saleorder> result = saleorderService.search(searchModel);

        assertFalse(result.getData().isEmpty(), "Có đơn hàng với email 'nguyenvanb@example.com' trong danh sách");
    }

    // Test 138: Tìm đơn hàng theo customer_phone
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testSearchWithExistingCustomerPhone() {
        Saleorder saleorder = new Saleorder();
        saleorder.setCode("ORDER003");
        saleorder.setCustomer_name("Nguyen Van C");
        saleorder.setCustomer_phone("0987654321");
        saleorderService.saveOrUpdate(saleorder);

        OrderSearchModel searchModel = new OrderSearchModel();
        searchModel.keyword = "0987654321";

        PagerData<Saleorder> result = saleorderService.search(searchModel);

        assertFalse(result.getData().isEmpty(), "Có đơn hàng với số điện thoại '0987654321' trong danh sách");
    }

    // Test 139: Tìm đơn hàng theo code
    @Test
    @Order(4)
    @Transactional
    @Rollback
    public void testSearchWithExistingCode() {
        Saleorder saleorder = new Saleorder();
        saleorder.setCode("ORDER004");
        saleorder.setCustomer_name("Nguyen Van D");
        saleorderService.saveOrUpdate(saleorder);

        OrderSearchModel searchModel = new OrderSearchModel();
        searchModel.keyword = "ORDER004";

        PagerData<Saleorder> result = saleorderService.search(searchModel);

        assertFalse(result.getData().isEmpty(), "Có đơn hàng với mã 'ORDER004' trong danh sách");
    }

    // Test 140: Tìm đơn hàng theo customer_address
    @Test
    @Order(5)
    @Transactional
    @Rollback
    public void testSearchWithExistingCustomerAddress() {
        Saleorder saleorder = new Saleorder();
        saleorder.setCode("ORDER005");
        saleorder.setCustomer_name("Nguyen Van E");
        saleorder.setCustomer_address("456 Nguyễn Trãi, Hà Nội");
        saleorderService.saveOrUpdate(saleorder);

        OrderSearchModel searchModel = new OrderSearchModel();
        searchModel.keyword = "Nguyễn Trãi";

        PagerData<Saleorder> result = saleorderService.search(searchModel);

        assertFalse(result.getData().isEmpty(), "Có đơn hàng với địa chỉ 'Nguyễn Trãi' trong danh sách");
    }

    // Test 141: Tìm đơn hàng với từ khóa không tồn tại
    @Test
    @Order(6)
    @Transactional
    @Rollback
    public void testSearchWithNonExistentKeyword() {
        OrderSearchModel searchModel = new OrderSearchModel();
        searchModel.keyword = "abcxyz";

        PagerData<Saleorder> result = saleorderService.search(searchModel);

        assertTrue(result.getData().isEmpty(), "Không có đơn hàng với từ khóa 'abcxyz' trong danh sách");
    }
}