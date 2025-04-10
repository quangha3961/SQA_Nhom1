package com.example.ManufacturerService;

import beebooks.StartServer;
import beebooks.entities.Manufacturer;
import beebooks.service.ManufacturerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class ManufacturerServiceDeleteTest {

    @Autowired
    private ManufacturerService manufacturerService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 85: Kiểm tra xóa nhà sản xuất qua entity
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testDelete_ManufacturerEntity() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("Manufacturer To Delete");
        manufacturer.setAddress("Address To Delete");
        Manufacturer savedManufacturer = manufacturerService.saveOrUpdate(manufacturer);

        Assertions.assertNotNull(savedManufacturer.getId(), "Nhà sản xuất phải được lưu trước khi xóa");

        manufacturerService.delete(savedManufacturer);

        Manufacturer deletedManufacturer = entityManager.find(Manufacturer.class, savedManufacturer.getId());
        Assertions.assertNull(deletedManufacturer, "Nhà sản xuất phải được xóa khỏi cơ sở dữ liệu");
    }

    // Test 86: Kiểm tra xóa nhà sản xuất qua ID
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testDeleteById() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("Manufacturer To Delete By Id");
        manufacturer.setAddress("Address To Delete By Id");
        Manufacturer savedManufacturer = manufacturerService.saveOrUpdate(manufacturer);

        Assertions.assertNotNull(savedManufacturer.getId(), "Nhà sản xuất phải được lưu trước khi xóa");

        manufacturerService.deleteById(savedManufacturer.getId());

        Manufacturer deletedManufacturer = entityManager.find(Manufacturer.class, savedManufacturer.getId());
        Assertions.assertNull(deletedManufacturer, "Nhà sản xuất phải được xóa khỏi cơ sở dữ liệu khi xóa bằng ID");
    }
}
