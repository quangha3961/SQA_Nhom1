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

    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testFindAll_NotEmpty() {
        // First, create and save a manufacturer
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("NXB Kim Đồng");
        manufacturer.setAddress("123 Đường Láng, Hà Nội");
        manufacturerService.saveOrUpdate(manufacturer);

        List<Manufacturer> manufacturers = manufacturerService.findAll();

        Assertions.assertFalse(manufacturers.isEmpty(), "Danh sách nhà sản xuất không được rỗng");
    }

    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testFindAll_ContainsKnownManufacturer() {
        // Create and save a manufacturer
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