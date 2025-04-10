package com.example.ManufacturerService;

import beebooks.StartServer;
import beebooks.entities.Manufacturer;
import beebooks.service.ManufacturerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class ManufacturerServiceFindAllTest {

    @Autowired
    private ManufacturerService manufacturerService;

    // Test 87: Kiểm tra danh sách nhà sản xuất không rỗng khi có ít nhất một nhà sản xuất
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testFindAll_NotEmpty() {
        // Tạo và lưu một nhà sản xuất để đảm bảo danh sách không rỗng
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("NXB Kim Đồng");
        manufacturer.setAddress("123 Đường Láng, Hà Nội");
        manufacturerService.saveOrUpdate(manufacturer);

        List<Manufacturer> manufacturers = manufacturerService.findAll();

        Assertions.assertFalse(manufacturers.isEmpty(), "Danh sách nhà sản xuất không được rỗng");
    }

    // Test 88: Kiểm tra danh sách chứa một nhà sản xuất đã lưu
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testFindAll_ContainsKnownManufacturer() {
        // Tạo và lưu một nhà sản xuất để đảm bảo chúng ta có một nhà sản xuất đã biết
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("NXB Kim Đồng");
        manufacturer.setAddress("123 Đường Láng, Hà Nội");
        Manufacturer savedManufacturer = manufacturerService.saveOrUpdate(manufacturer);

        List<Manufacturer> manufacturers = manufacturerService.findAll();

        boolean hasManufacturerWithKnownId = manufacturers.stream().anyMatch(m -> m.getId() == savedManufacturer.getId());
        Assertions.assertTrue(hasManufacturerWithKnownId, "Danh sách phải chứa nhà sản xuất với ID " + savedManufacturer.getId());

        Manufacturer foundManufacturer = manufacturers.stream().filter(m -> m.getId() == savedManufacturer.getId()).findFirst().orElse(null);
        Assertions.assertNotNull(foundManufacturer, "Nhà sản xuất phải tồn tại trong danh sách");
        Assertions.assertEquals("NXB Kim Đồng", foundManufacturer.getName(), "Tên nhà sản xuất phải khớp");
        Assertions.assertEquals("123 Đường Láng, Hà Nội", foundManufacturer.getAddress(), "Địa chỉ nhà sản xuất phải khớp");
    }
}
