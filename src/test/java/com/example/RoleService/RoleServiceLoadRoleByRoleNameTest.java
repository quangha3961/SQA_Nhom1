package com.example.RoleService;

import beebooks.StartServer;
import beebooks.entities.Role;
import beebooks.service.RoleService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = StartServer.class)
public class RoleServiceLoadRoleByRoleNameTest {

    @Autowired
    private RoleService roleService;

    // Test 131: Tìm role "ADMIN" - Thành công (Pass)
    @Test
    @Order(1)
    @Transactional
    @Rollback
    public void testLoadRoleByRoleName_Admin_Success() {
        // Tạo và lưu role "ADMIN"
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        adminRole.setDescription("ADMIN"); // Mô tả giống với name
        //roleService.saveOrUpdate(adminRole);

        Role foundRole = roleService.loadRoleByRoleName("ADMIN");

        assertNotNull(foundRole, "Role ADMIN phải được tìm thấy");
        assertEquals("ADMIN", foundRole.getName(), "Tên role phải là ADMIN");
        assertEquals("ADMIN", foundRole.getDescription(), "Mô tả role phải giống với tên");
    }

    // Test 132: Tìm role "ADMIN" - Thất bại (Fail)
    @Test
    @Order(2)
    @Transactional
    @Rollback
    public void testLoadRoleByRoleName_Admin_Fail() {
        // Không tạo role "ADMIN" trong DB

        Role foundRole = roleService.loadRoleByRoleName("ADMIN");

        assertNull(foundRole, "Role ADMIN không tồn tại, phải trả về null");
    }

    // Test 133: Tìm role "GUEST" - Thành công (Pass)
    @Test
    @Order(3)
    @Transactional
    @Rollback
    public void testLoadRoleByRoleName_Guest_Success() {
        // Tạo và lưu role "GUEST"
        Role guestRole = new Role();
        guestRole.setName("GUEST");
        guestRole.setDescription("GUEST"); // Mô tả giống với name
        //roleService.saveOrUpdate(guestRole);

        Role foundRole = roleService.loadRoleByRoleName("GUEST");

        assertNotNull(foundRole, "Role GUEST phải được tìm thấy");
        assertEquals("GUEST", foundRole.getName(), "Tên role phải là GUEST");
        assertEquals("GUEST", foundRole.getDescription(), "Mô tả role phải giống với tên");
    }

    // Test 134: Tìm role "GUEST" - Thất bại (Fail)
    @Test
    @Order(4)
    @Transactional
    @Rollback
    public void testLoadRoleByRoleName_Guest_Fail() {
        // Không tạo role "GUEST" trong DB

        Role foundRole = roleService.loadRoleByRoleName("GUEST");

        assertNull(foundRole, "Role GUEST không tồn tại, phải trả về null");
    }

    // Test 135: Tìm role với tên null - Thất bại (Fail)
    @Test
    @Order(5)
    @Transactional
    @Rollback
    public void testLoadRoleByRoleName_NullName_Fail() {
        Role foundRole = roleService.loadRoleByRoleName(null);

        assertNull(foundRole, "Tìm role với tên null phải trả về null");
    }
}