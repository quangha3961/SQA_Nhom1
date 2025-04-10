package com.example.ManufacturerService;

import beebooks.StartServer;
import beebooks.dto.SearchModel;
import beebooks.entities.Manufacturer;
import beebooks.service.ManufacturerService;
import beebooks.service.PagerData;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class ManufacturerServiceSearchTest {

    @Autowired
    private ManufacturerService manufacturerService;

    // Test 89: Tìm nhà sản xuất có trong csdl theo tên
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testSearchWithExistingName() {
        // First, create a manufacturer to search for
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("NXB Kim Đồng");
        manufacturer.setAddress("123 Đường Láng, Hà Nội");
        manufacturerService.saveOrUpdate(manufacturer);

        SearchModel searchModel = new SearchModel();
        searchModel.keyword = "NXB Kim Đồng";

        PagerData<Manufacturer> result = manufacturerService.search(searchModel);

        Assertions.assertFalse(result.getData().isEmpty(), "Có nhà sản xuất với tên 'NXB Kim Đồng' trong danh sách");
    }

    // Test 90: Tìm nhà sản xuất có trong csdl theo địa chỉ
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testSearchWithExistingAddress() {
        // First, create a manufacturer to search for
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("NXB Kim Đồng");
        manufacturer.setAddress("123 Đường Láng, Hà Nội");
        manufacturerService.saveOrUpdate(manufacturer);

        SearchModel searchModel = new SearchModel();
        searchModel.keyword = "Đường Láng";

        PagerData<Manufacturer> result = manufacturerService.search(searchModel);

        Assertions.assertFalse(result.getData().isEmpty(), "Có nhà sản xuất với địa chỉ chứa 'Đường Láng' trong danh sách");
    }

    // Test 91: Tìm nhà sản xuất không có trong csdl
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testSearchWithNonExistentKeyword() {
        SearchModel searchModel = new SearchModel();
        searchModel.keyword = "abcxyz";

        PagerData<Manufacturer> result = manufacturerService.search(searchModel);

        Assertions.assertTrue(result.getData().isEmpty(), "Không có nhà sản xuất với từ khóa 'abcxyz' trong danh sách");
    }

    // Test 92: Tìm theo ID nhà sản xuất có trong csdl
    @Test
    @Order(4)
    @Transactional
    @Rollback
    public void testSearchById() {
        // First, create a manufacturer to search for
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("NXB Kim Đồng");
        manufacturer.setAddress("123 Đường Láng, Hà Nội");
        Manufacturer savedManufacturer = manufacturerService.saveOrUpdate(manufacturer);

        SearchModel searchModel = new SearchModel();
        searchModel.id = savedManufacturer.getId();

        PagerData<Manufacturer> result = manufacturerService.search(searchModel);

        Assertions.assertFalse(result.getData().isEmpty(), "Phải có kết quả với ID tồn tại");
        Assertions.assertEquals(savedManufacturer.getId(), result.getData().get(0).getId(), "ID kết quả phải khớp");
    }

    // Test 93: Tìm theo ID nhà sản xuất không có trong csdl
    @Test
    @Order(5)
    @Transactional
    @Rollback
    public void testSearchByNonExistentId() {
        SearchModel searchModel = new SearchModel();
        searchModel.id = 999; // Assuming ID 999 does not exist

        PagerData<Manufacturer> result = manufacturerService.search(searchModel);

        Assertions.assertTrue(result.getData().isEmpty(), "Không có kết quả với ID 999 không tồn tại");
    }
}