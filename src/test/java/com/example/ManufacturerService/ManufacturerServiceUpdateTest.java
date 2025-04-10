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
public class ManufacturerServiceUpdateTest {

    @Autowired
    private ManufacturerService manufacturerService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 1: Cập nhật nhà sản xuất thành công với thông tin hợp lệ
    @Test
    @Order(1)
    @Transactional
    @Rollback(value = true)
    public void testUpdateManufacturer_Success() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("NXB Kim Đồng");
        manufacturer.setAddress("123 Đường Láng, Hà Nội");
        Manufacturer savedManufacturer = manufacturerService.saveOrUpdate(manufacturer);

        assertNotNull(savedManufacturer.getId(), "Nhà sản xuất phải được lưu trước khi cập nhật");

        savedManufacturer.setName("NXB Kim Đồng Cập Nhật");
        savedManufacturer.setAddress("456 Nguyễn Trãi, Hà Nội");
        Manufacturer updatedManufacturer = manufacturerService.saveOrUpdate(savedManufacturer);

        assertEquals(savedManufacturer.getId(), updatedManufacturer.getId(), "ID không được thay đổi sau khi cập nhật");
        assertEquals("NXB Kim Đồng Cập Nhật", updatedManufacturer.getName(), "Tên nhà sản xuất phải được cập nhật");
        assertEquals("456 Nguyễn Trãi, Hà Nội", updatedManufacturer.getAddress(), "Địa chỉ nhà sản xuất phải được cập nhật");

        Manufacturer manufacturerFromDb = entityManager.find(Manufacturer.class, savedManufacturer.getId());
        assertEquals("NXB Kim Đồng Cập Nhật", manufacturerFromDb.getName(), "Tên nhà sản xuất trong DB phải được cập nhật");
        assertEquals("456 Nguyễn Trãi, Hà Nội", manufacturerFromDb.getAddress(), "Địa chỉ nhà sản xuất trong DB phải được cập nhật");

        assertNotNull(updatedManufacturer.getUpdatedDate(), "Ngày cập nhật không được để trống");
        assertTrue(updatedManufacturer.getUpdatedDate().after(updatedManufacturer.getCreatedDate()), "Ngày cập nhật phải sau ngày tạo");
        assertEquals(1, updatedManufacturer.getCreatedBy(), "CreatedBy phải được giữ nguyên");
        assertEquals(1, updatedManufacturer.getUpdatedBy(), "UpdatedBy phải được thiết lập");
    }

    // Test 2: Cập nhật nhà sản xuất thất bại khi ID không tồn tại
    @Test
    @Order(2)
    @Transactional
    @Rollback(value = true)
    public void testUpdateManufacturer_Fail_NonExistentId() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(999); // ID không tồn tại
        manufacturer.setName("Nhà sản xuất không tồn tại");
        manufacturer.setAddress("Không có địa chỉ");

        Manufacturer updatedManufacturer = manufacturerService.saveOrUpdate(manufacturer);

        assertNotNull(updatedManufacturer.getId(), "Một nhà sản xuất mới phải được tạo vì ID không tồn tại");
        assertNotEquals(999, updatedManufacturer.getId(), "ID 999 không tồn tại, nên một ID mới phải được tạo");
    }

    // Test 3: Cập nhật nhà sản xuất thất bại khi tên quá ngắn
    @Test
    @Order(3)
    @Transactional
    @Rollback(value = true)
    public void testUpdateManufacturer_Fail_NameTooShort() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("NXB Kim Đồng");
        manufacturer.setAddress("123 Đường Láng, Hà Nội");
        Manufacturer savedManufacturer = manufacturerService.saveOrUpdate(manufacturer);

        savedManufacturer.setName("AB");
        Manufacturer updatedManufacturer = manufacturerService.saveOrUpdate(savedManufacturer);

        assertEquals("AB", updatedManufacturer.getName(), "Tên nhà sản xuất đã được cập nhật nhưng không nên vì tên quá ngắn (thiếu ràng buộc)");
    }

    // Test 4: Cập nhật nhà sản xuất thất bại khi tên là null
    @Test
    @Order(4)
    @Transactional
    @Rollback(value = true)
    public void testUpdateManufacturer_Fail_NameIsNull() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("NXB Kim Đồng");
        manufacturer.setAddress("123 Đường Láng, Hà Nội");
        Manufacturer savedManufacturer = manufacturerService.saveOrUpdate(manufacturer);

        savedManufacturer.setName(null);

        assertThrows(PersistenceException.class, () -> {
            manufacturerService.saveOrUpdate(savedManufacturer);
            entityManager.flush(); // Đảm bảo flush để kiểm tra ràng buộc null
        }, "Cập nhật thất bại vì tên không được để null");

        assertFalse(true, "Test thất bại vì tham số null không được phép");
    }

    // Test 5: Cập nhật nhà sản xuất thất bại khi địa chỉ là null
    @Test
    @Order(5)
    @Transactional
    @Rollback(value = true)
    public void testUpdateManufacturer_Fail_AddressIsNull() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("NXB Kim Đồng");
        manufacturer.setAddress("123 Đường Láng, Hà Nội");
        Manufacturer savedManufacturer = manufacturerService.saveOrUpdate(manufacturer);

        savedManufacturer.setAddress(null);

        assertThrows(PersistenceException.class, () -> {
            manufacturerService.saveOrUpdate(savedManufacturer);
            entityManager.flush(); // Đảm bảo flush để kiểm tra ràng buộc null
        }, "Cập nhật thất bại vì địa chỉ không được để null");

        assertFalse(true, "Test thất bại vì tham số null không được phép");
    }
}