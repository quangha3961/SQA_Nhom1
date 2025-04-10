package com.example.ContactService;

import beebooks.StartServer;
import beebooks.dto.SearchModel;
import beebooks.entities.Contact;
import beebooks.service.ContactService;
import beebooks.service.PagerData;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class ContactServiceSearchTest {

    @Autowired
    private ContactService contactService;

    // Test 76: Tìm liên hệ có trong csdl theo tên
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testSearchWithExistingName() {
        // First, create a contact to search for
        Contact contact = new Contact();
        contact.setName("Nguyen Van A");
        contact.setEmail("nguyenvana@example.com");
        contact.setMassage("This is a test message");
        contactService.saveOrUpdate(contact);

        SearchModel searchModel = new SearchModel();
        searchModel.keyword = "Nguyen Van A";

        PagerData<Contact> result = contactService.search(searchModel);

        Assertions.assertFalse(result.getData().isEmpty(), "Có liên hệ với tên 'Nguyen Van A' trong danh sách");
    }

    // Test 77: Tìm liên hệ có trong csdl theo email
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testSearchWithExistingEmail() {
        // First, create a contact to search for
        Contact contact = new Contact();
        contact.setName("Nguyen Van A");
        contact.setEmail("nguyenvana@example.com");
        contact.setMassage("This is a test message");
        contactService.saveOrUpdate(contact);

        SearchModel searchModel = new SearchModel();
        searchModel.keyword = "nguyenvana@example.com";

        PagerData<Contact> result = contactService.search(searchModel);

        Assertions.assertFalse(result.getData().isEmpty(), "Có liên hệ với email 'nguyenvana@example.com' trong danh sách");
    }

    // Test 78: Tìm liên hệ có trong csdl theo tin nhắn
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testSearchWithExistingMessage() {
        // First, create a contact to search for
        Contact contact = new Contact();
        contact.setName("Nguyen Van A");
        contact.setEmail("nguyenvana@example.com");
        contact.setMassage("This is a test message");
        contactService.saveOrUpdate(contact);

        SearchModel searchModel = new SearchModel();
        searchModel.keyword = "test message";

        PagerData<Contact> result = contactService.search(searchModel);

        Assertions.assertFalse(result.getData().isEmpty(), "Có liên hệ với tin nhắn chứa 'test message' trong danh sách");
    }

    // Test 79: Tìm liên hệ không có trong csdl
    @Test
    @Order(4)
    @Transactional
    @Rollback
    public void testSearchWithNonExistentKeyword() {
        SearchModel searchModel = new SearchModel();
        searchModel.keyword = "abcxyz";

        PagerData<Contact> result = contactService.search(searchModel);

        Assertions.assertTrue(result.getData().isEmpty(), "Không có liên hệ với từ khóa 'abcxyz' trong danh sách");
    }

    // Test 80: Tìm liên hệ với searchModel null
    @Test
    @Order(5)
    @Transactional
    @Rollback
    public void testSearchWithNullSearchModel() {
        // First, create a contact to ensure the table is not empty
        Contact contact = new Contact();
        contact.setName("Nguyen Van A");
        contact.setEmail("nguyenvana@example.com");
        contact.setMassage("This is a test message");
        contactService.saveOrUpdate(contact);

        PagerData<Contact> result = contactService.search(null);

        Assertions.assertFalse(result.getData().isEmpty(), "Phải có kết quả khi searchModel là null (trả về tất cả)");
    }
}