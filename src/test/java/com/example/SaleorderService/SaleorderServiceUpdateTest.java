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
public class SaleorderServiceUpdateTest {

    @Autowired
    private SaleorderService saleorderService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 142: Cập nhật đơn hàng hợp lệ
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testUpdateSaleorder_Success() {
        Saleorder saleorder = new Saleorder();
        saleorder.setCode("ORDER001");
        saleorder.setCustomer_name("Nguyen Van A");
        saleorder.setCustomer_address("123 Đường Láng, Hà Nội");
        saleorder.setCustomer_phone("0123456789");
        saleorder.setCustomer_email("nguyenvana@example.com");
        saleorder.setTotal(BigDecimal.valueOf(5));
        saleorder.setTotalPrice(BigDecimal.valueOf(500000));
        saleorder.setSeo("order-001");
        saleorder.setPaymentType(1);
        saleorder.setPaymentStatus(1);

        Saleorder savedSaleorder = saleorderService.saveOrUpdate(saleorder);

        // Cập nhật đơn hàng
        savedSaleorder.setCode("ORDER001_UPDATED");
        savedSaleorder.setCustomer_name("Nguyen Van B");
        savedSaleorder.setCustomer_address("456 Nguyễn Trãi, Hà Nội");
        savedSaleorder.setCustomer_phone("0987654321");
        savedSaleorder.setCustomer_email("nguyenvanb@example.com");
        savedSaleorder.setTotal(BigDecimal.valueOf(10));
        savedSaleorder.setTotalPrice(BigDecimal.valueOf(1000000));
        savedSaleorder.setReason("Hủy vì khách yêu cầu");
        savedSaleorder.setSeo("order-001-updated");
        savedSaleorder.setPaymentType(2); // Thanh toán bằng VNPay
        savedSaleorder.setPaymentStatus(2); // Đang giao hàng

        Saleorder updatedSaleorder = saleorderService.saveOrUpdate(savedSaleorder);

        assertEquals(savedSaleorder.getId(), updatedSaleorder.getId(), "ID không được thay đổi sau khi cập nhật");
        assertEquals("ORDER001_UPDATED", updatedSaleorder.getCode(), "Mã đơn hàng phải được cập nhật");
        assertEquals("Nguyen Van B", updatedSaleorder.getCustomer_name(), "Tên khách hàng phải được cập nhật");
        assertEquals("456 Nguyễn Trãi, Hà Nội", updatedSaleorder.getCustomer_address(), "Địa chỉ khách hàng phải được cập nhật");
        assertEquals("0987654321", updatedSaleorder.getCustomer_phone(), "Số điện thoại khách hàng phải được cập nhật");
        assertEquals("nguyenvanb@example.com", updatedSaleorder.getCustomer_email(), "Email khách hàng phải được cập nhật");
        assertEquals(BigDecimal.valueOf(10), updatedSaleorder.getTotal(), "Tổng số sản phẩm phải được cập nhật");
        assertEquals(BigDecimal.valueOf(1000000), updatedSaleorder.getTotalPrice(), "Tổng giá trị đơn hàng phải được cập nhật");
        assertEquals("Hủy vì khách yêu cầu", updatedSaleorder.getReason(), "Lý do hủy đơn phải được cập nhật");
        assertEquals("order-001-updated", updatedSaleorder.getSeo(), "SEO đơn hàng phải được cập nhật");
        assertEquals(2, updatedSaleorder.getPaymentType(), "Loại thanh toán phải được cập nhật");
        assertEquals(2, updatedSaleorder.getPaymentStatus(), "Trạng thái thanh toán phải được cập nhật");

        Saleorder saleorderFromDb = entityManager.find(Saleorder.class, savedSaleorder.getId());
        assertEquals("ORDER001_UPDATED", saleorderFromDb.getCode(), "Mã đơn hàng trong DB phải được cập nhật");
    }

    // Test 143: Cập nhật đơn hàng với code null (thất bại)
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testUpdateSaleorder_Fail_CodeIsNull() {
        Saleorder saleorder = new Saleorder();
        saleorder.setCode("ORDER002");
        saleorder.setCustomer_name("Nguyen Van A");
        Saleorder savedSaleorder = saleorderService.saveOrUpdate(saleorder);

        savedSaleorder.setCode(null); // Thử cập nhật với mã đơn hàng null

        // Kiểm tra ngoại lệ khi cố gắng lưu với code null
        assertThrows(PersistenceException.class, () -> {
            saleorderService.saveOrUpdate(savedSaleorder);
        }, "Dự kiến sẽ xảy ra lỗi PersistenceException khi code để trống trong quá trình cập nhật");
    }

    // Test 144: Cập nhật đơn hàng với ID không tồn tại (thất bại)
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testUpdateSaleorder_Fail_NonExistentId() {
        Saleorder saleorder = new Saleorder();
        saleorder.setId(999); // ID không tồn tại
        saleorder.setCode("ORDER999");
        saleorder.setCustomer_name("Nguyen Van A");

        // Kiểm tra khi cố gắng cập nhật với ID không tồn tại
        assertThrows(PersistenceException.class, () -> {
            saleorderService.saveOrUpdate(saleorder);
        }, "Dự kiến sẽ xảy ra lỗi PersistenceException khi cố gắng cập nhật với ID không tồn tại");
    }
}
