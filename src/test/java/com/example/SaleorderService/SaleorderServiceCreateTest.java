package com.example.SaleorderService;

import beebooks.StartServer;
import beebooks.entities.Saleorder;
import beebooks.service.SaleorderService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class SaleorderServiceCreateTest {

    @Autowired
    private SaleorderService saleorderService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 1: Tạo đơn hàng hợp lệ
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testCreateSaleorder_Success() {
        Saleorder saleorder = new Saleorder();
        saleorder.setCode("ORDER001");
        saleorder.setCustomer_name("Nguyen Van A");
        saleorder.setCustomer_address("123 Đường Láng, Hà Nội");
        saleorder.setCustomer_phone("0123456789");
        saleorder.setCustomer_email("nguyenvana@example.com");
        saleorder.setTotal(BigDecimal.valueOf(5));
        saleorder.setTotalPrice(BigDecimal.valueOf(500000));
        saleorder.setReason(null);
        saleorder.setSeo("order-001");
        saleorder.setPaymentType(1); // Thanh toán bằng tiền mặt
        saleorder.setPaymentStatus(1); // Chưa xử lý

        Saleorder savedSaleorder = saleorderService.saveOrUpdate(saleorder);

        assertNotNull(savedSaleorder.getId(), "ID đơn hàng không được để trống");
        assertEquals("ORDER001", savedSaleorder.getCode(), "Mã đơn hàng không đúng");
        assertEquals("Nguyen Van A", savedSaleorder.getCustomer_name(), "Tên khách hàng không đúng");
        assertEquals("123 Đường Láng, Hà Nội", savedSaleorder.getCustomer_address(), "Địa chỉ khách hàng không đúng");
        assertEquals("0123456789", savedSaleorder.getCustomer_phone(), "Số điện thoại khách hàng không đúng");
        assertEquals("nguyenvana@example.com", savedSaleorder.getCustomer_email(), "Email khách hàng không đúng");
        assertEquals(BigDecimal.valueOf(5), savedSaleorder.getTotal(), "Tổng số sản phẩm không đúng");
        assertEquals(BigDecimal.valueOf(500000), savedSaleorder.getTotalPrice(), "Tổng giá trị đơn hàng không đúng");
        assertNull(savedSaleorder.getReason(), "Lý do hủy đơn phải là null");
        assertEquals("order-001", savedSaleorder.getSeo(), "SEO đơn hàng không đúng");
        assertEquals(1, savedSaleorder.getPaymentType(), "Loại thanh toán không đúng");
        assertEquals(1, savedSaleorder.getPaymentStatus(), "Trạng thái thanh toán không đúng");

        Saleorder saleorderFromDb = entityManager.find(Saleorder.class, savedSaleorder.getId());
        assertNotNull(saleorderFromDb, "Đơn hàng không được tìm thấy trong DB");
    }

    // Test 2: Tạo đơn hàng với code null (thất bại)
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testCreateSaleorder_Fail_CodeIsNull() {
        Saleorder saleorder = new Saleorder();
        saleorder.setCode(null); // Code là nullable = false
        saleorder.setCustomer_name("Nguyen Van A");
        saleorder.setCustomer_address("123 Đường Láng, Hà Nội");
        saleorder.setCustomer_phone("0123456789");
        saleorder.setCustomer_email("nguyenvana@example.com");
        saleorder.setTotal(BigDecimal.valueOf(5));
        saleorder.setTotalPrice(BigDecimal.valueOf(500000));
        saleorder.setReason(null);
        saleorder.setSeo("order-001");
        saleorder.setPaymentType(1); // Thanh toán bằng tiền mặt
        saleorder.setPaymentStatus(1); // Chưa xử lý

        // Sửa test để kiểm tra tất cả các trường không hợp lệ, không chỉ code null
        assertThrows(PersistenceException.class, () -> {
            saleorderService.saveOrUpdate(saleorder);
        }, "Dự kiến sẽ xảy ra lỗi PersistenceException khi code để trống hoặc các trường bắt buộc khác không hợp lệ");
    }

    // Test 3: Tạo đơn hàng với các trường nullable để null
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testCreateSaleorder_Fail_FieldsAreNull() {
        Saleorder saleorder = new Saleorder();
        saleorder.setCode("ORDER002");
        saleorder.setCustomer_name(null); // Customer name không thể null
        saleorder.setCustomer_address(null); // Customer address không thể null
        saleorder.setCustomer_phone(null); // Customer phone không thể null
        saleorder.setCustomer_email(null); // Customer email không thể null
        saleorder.setTotal(BigDecimal.valueOf(0)); // Total không thể null
        saleorder.setTotalPrice(BigDecimal.valueOf(0)); // TotalPrice không thể null
        saleorder.setReason(null);
        saleorder.setSeo("order-002");
        saleorder.setPaymentType(1); // Thanh toán bằng tiền mặt
        saleorder.setPaymentStatus(1); // Chưa xử lý

        // Test này sẽ fail vì các trường quan trọng không thể là null
        assertThrows(PersistenceException.class, () -> {
            saleorderService.saveOrUpdate(saleorder);
        }, "Dự kiến sẽ xảy ra lỗi PersistenceException khi các trường bắt buộc là null hoặc không hợp lệ");
    }
}
