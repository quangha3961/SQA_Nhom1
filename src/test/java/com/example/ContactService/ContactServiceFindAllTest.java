package com.example.ContactService;

import beebooks.StartServer;
import beebooks.entities.Contact;
import beebooks.service.ContactService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class ContactServiceFindAllTest {

    @Autowired
    private ContactService contactService;

    // Test 70: Kiểm tra danh sách liên hệ không rỗng khi có ít nhất một liên hệ
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testFindAll_NotEmpty() {
        // Tạo và lưu một liên hệ để đảm bảo danh sách không rỗng
        Contact contact = new Contact();
        contact.setName("Nguyen Van A");
        contact.setEmail("nguyenvana@example.com");
        contact.setMassage("This is a test message");
        contactService.saveOrUpdate(contact);

        List<Contact> contacts = contactService.findAll();

        Assertions.assertFalse(contacts.isEmpty(), "Danh sách liên hệ không được rỗng");
    }

    // Test 71: Kiểm tra danh sách chứa một liên hệ đã lưu
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testFindAll_ContainsKnownContact() {
        // Tạo và lưu một liên hệ để đảm bảo chúng ta có một liên hệ đã biết
        Contact contact = new Contact();
        contact.setName("Nguyen Van A");
        contact.setEmail("nguyenvana@example.com");
        contact.setMassage("This is a test message");
        Contact savedContact = contactService.saveOrUpdate(contact);

        List<Contact> contacts = contactService.findAll();

        boolean hasContactWithKnownId = contacts.stream().anyMatch(c -> c.getId() == savedContact.getId());
        Assertions.assertTrue(hasContactWithKnownId, "Danh sách phải chứa liên hệ với ID " + savedContact.getId());

        Contact foundContact = contacts.stream().filter(c -> c.getId() == savedContact.getId()).findFirst().orElse(null);
        Assertions.assertNotNull(foundContact, "Liên hệ phải tồn tại trong danh sách");
        Assertions.assertEquals("Nguyen Van A", foundContact.getName(), "Tên liên hệ phải khớp");
        Assertions.assertEquals("nguyenvana@example.com", foundContact.getEmail(), "Email liên hệ phải khớp");
        Assertions.assertEquals("This is a test message", foundContact.getMassage(), "Tin nhắn liên hệ phải khớp");
    }
}
