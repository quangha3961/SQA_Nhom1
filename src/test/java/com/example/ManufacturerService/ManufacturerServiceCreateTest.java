package com.example.ManufacturerService;

import beebooks.StartServer;
import beebooks.entities.Manufacturer;
import beebooks.service.ManufacturerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class ManufacturerServiceCreateTest {

    @Autowired
    private ManufacturerService manufacturerService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 81: Thêm nhà sản xuất hợp lệ
    @Test
    @Order(1)
    @Rollback(value = true)
    public void testCreateManufacturer_SuccessWithRollback() {
        // Kiểm tra thêm nhà sản xuất hợp lệ với rollback. Tất cả các tham số cần hợp lệ
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("NXB Kim Đồng");
        manufacturer.setAddress("123 Đường Láng, Hà Nội");

        if (manufacturer.getName() == null || manufacturer.getAddress() == null || manufacturer.getName().length() < 3) {
            fail("Tên và địa chỉ nhà sản xuất không được phép null hoặc quá ngắn.");
        } else {
            Manufacturer savedManufacturer = manufacturerService.saveOrUpdate(manufacturer);

            assertNotNull(savedManufacturer.getId(), "Id không được để trống.");
            assertEquals("NXB Kim Đồng", savedManufacturer.getName(), "Tên nhà sản xuất không đúng.");
            assertEquals("123 Đường Láng, Hà Nội", savedManufacturer.getAddress(), "Địa chỉ nhà sản xuất không đúng.");

            Manufacturer manufacturerFromDb = entityManager.find(Manufacturer.class, savedManufacturer.getId());
            assertNotNull(manufacturerFromDb, "Nhà sản xuất không được tìm thấy trong DB.");
        }
    }

    // Test 82: Thêm nhà sản xuất không có tên
    @Test
    @Order(2)
    public void testCreateManufacturer_Fail_NameIsNull() {
        // Kiểm tra khi tên nhà sản xuất là null, mong đợi PersistenceException
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setAddress("123 Đường Láng, Hà Nội");

        if (manufacturer.getName() == null || manufacturer.getName().length() < 3) {
            fail("Tên nhà sản xuất không được phép null hoặc quá ngắn.");
        } else {
            assertThrows(PersistenceException.class, () -> {
                manufacturerService.saveOrUpdate(manufacturer);
            }, "Expected PersistenceException when name is null");
        }
    }

    // Test 83: Tên nhà sản xuất quá ngắn
    @Test
    @Order(3)
    public void testCreateManufacturer_Fail_NameTooShort() {
        // Kiểm tra khi tên nhà sản xuất quá ngắn (dưới 3 ký tự), test sẽ thất bại
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("AB");
        manufacturer.setAddress("123 Đường Láng, Hà Nội");

        if (manufacturer.getName() == null || manufacturer.getName().length() < 3) {
            fail("Tên nhà sản xuất quá ngắn.");
        } else {
            Manufacturer savedManufacturer = manufacturerService.saveOrUpdate(manufacturer);

            assertNotNull(savedManufacturer.getId(), "Nhà sản xuất có tên ngắn nhưng được lưu do thiếu ràng buộc.");
        }
    }

    // Test 84: Thêm nhà sản xuất thiếu địa chỉ
    @Test
    @Order(4)
    public void testCreateManufacturer_Fail_AddressIsNull() {
        // Kiểm tra khi địa chỉ nhà sản xuất là null, mong đợi PersistenceException
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("NXB Trẻ");

        if (manufacturer.getAddress() == null) {
            fail("Địa chỉ nhà sản xuất không được phép null.");
        } else {
            assertThrows(PersistenceException.class, () -> {
                manufacturerService.saveOrUpdate(manufacturer);
            }, "Dự kiến sẽ xảy ra lỗi PersistenceException khi địa chỉ để trống.");
        }
    }
}
