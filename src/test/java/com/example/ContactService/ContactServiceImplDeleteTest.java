package com.example.ContactService;

import beebooks.StartServer;
import beebooks.entities.Contact;
import beebooks.service.ContactService;
import beebooks.service.ContactServiceImpl;
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
public class ContactServiceImplDeleteTest {

    @Autowired
    private ContactServiceImpl contactServiceImpl;

    @Autowired
    private ContactService contactService;

    @PersistenceContext
    private EntityManager entityManager;

    // Test 72: Xóa liên hệ với ID tồn tại
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testDeleteContact_ExistingId() {
        // First, create and save a contact
        Contact contact = new Contact();
        contact.setName("Nguyen Van A");
        contact.setEmail("nguyenvana@example.com");
        contact.setMassage("This is a test message");
        Contact savedContact = contactService.saveOrUpdate(contact);

        Assertions.assertNotNull(savedContact.getId(), "Liên hệ phải được lưu trước khi xóa");

        boolean result = contactServiceImpl.deleteContact(savedContact.getId());

        Assertions.assertTrue(result, "Phải xóa được liên hệ với ID tồn tại");

        Contact deletedContact = entityManager.find(Contact.class, savedContact.getId());
        Assertions.assertNull(deletedContact, "Liên hệ phải được xóa khỏi cơ sở dữ liệu");
    }

    // Test 73: Xóa liên hệ với ID không tồn tại
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testDeleteContact_NonExistentId() {
        boolean result = contactServiceImpl.deleteContact(999); // Assuming ID 999 does not exist

        Assertions.assertFalse(result, "Không thể xóa liên hệ với ID không tồn tại");
    }

    // Test 74: Xóa liên hệ với ID nhỏ hơn 1
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testDeleteContact_InvalidId() {
        boolean result = contactServiceImpl.deleteContact(0);

        Assertions.assertFalse(result, "Không thể xóa liên hệ với ID nhỏ hơn 1");
    }

    // Test 75: Xóa liên hệ với ID âm
    @Test
    @Order(4)
    @Transactional
    @Rollback
    public void testDeleteContact_NegativeId() {
        boolean result = contactServiceImpl.deleteContact(-1);

        Assertions.assertFalse(result, "Không thể xóa liên hệ với ID âm");
    }
}