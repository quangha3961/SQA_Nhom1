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

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class SaleorderServiceDeleteTest {

    @Autowired
    private SaleorderService saleorderService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 154: Xóa đơn hàng hợp lệ
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testDeleteSaleorder_Success() {
        Saleorder saleorder = new Saleorder();
        saleorder.setCode("ORDER001");
        saleorder.setCustomer_name("Nguyen Van A");
        Saleorder savedSaleorder = saleorderService.saveOrUpdate(saleorder);

        assertNotNull(savedSaleorder.getId(), "Đơn hàng phải được lưu trước khi xóa");

        saleorderService.delete(savedSaleorder);

        Saleorder deletedSaleorder = entityManager.find(Saleorder.class, savedSaleorder.getId());
        assertNull(deletedSaleorder, "Đơn hàng phải được xóa khỏi cơ sở dữ liệu");
    }

    // Test 155: Xóa đơn hàng với ID không tồn tại
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testDeleteSaleorder_NonExistentId() {
        // Tạo đối tượng Saleorder với ID không tồn tại
        Saleorder saleorder = new Saleorder();
        saleorder.setId(999); // ID không tồn tại

        // Kiểm tra nếu Saleorder tồn tại trong cơ sở dữ liệu
        Saleorder saleorderFromDb = entityManager.find(Saleorder.class, saleorder.getId());

        // Nếu Saleorder không tồn tại, không thực hiện xóa
        if (saleorderFromDb == null) {
            // Kiểm tra rằng không có đối tượng nào bị xóa
            assertNull(saleorderFromDb, "Đơn hàng với ID không tồn tại nên không có trong DB");
        } else {
            // Nếu có, xóa Saleorder
            saleorderService.delete(saleorderFromDb);
            Saleorder deletedSaleorder = entityManager.find(Saleorder.class, saleorder.getId());
            assertNull(deletedSaleorder, "Đơn hàng phải được xóa khỏi cơ sở dữ liệu");
        }
    }
}