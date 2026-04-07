package co.edu.uptc.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.Model.Admin;


public class AdminServicesTest {

    private AdminServices adminServices;
    private List<Admin> adminList;

    @BeforeEach
    void setUp() {
        adminServices = new AdminServices();
        adminList = new ArrayList<>();

        adminList.add(new Admin("Admin1", 1, "pass1", "111111111", "admin1@test.com"));
        adminList.add(new Admin("Admin2", 2, "pass2", "222222222", "admin2@test.com"));
        adminList.add(new Admin("Admin3", 3, "pass3", "333333333", "admin3@test.com"));
    }

    @Test
    void testFireAdmin() {
        assertTrue(adminServices.searchAdminById(2, adminList));

        adminServices.fireAdmin(2, adminList);

        assertFalse(adminServices.searchAdminById(2, adminList));
        assertEquals(2, adminList.size());

        // Validación extra 🔥
        assertNull(adminServices.sendAdminById(2, adminList));
    }

    @Test
    void testSaveNewAdmin() {
        Admin newAdmin = new Admin("NewAdmin", 4, "newpass", "444444444", "new@test.com");

        adminServices.saveNewAdmin(newAdmin, adminList);

        assertEquals(4, adminList.size());
        assertTrue(adminServices.searchAdminById(4, adminList));

        Admin savedAdmin = adminServices.sendAdminById(4, adminList);

        assertNotNull(savedAdmin);
        assertEquals("NewAdmin", savedAdmin.getUserName());
        assertEquals(4, savedAdmin.getId());
    }

    @Test
    void testSearchAdminById() {
        assertTrue(adminServices.searchAdminById(1, adminList));
        assertTrue(adminServices.searchAdminById(3, adminList));
        assertFalse(adminServices.searchAdminById(99, adminList));
    }

    @Test
    void testSendAdminById() {
        Admin admin = adminServices.sendAdminById(1, adminList);

        assertNotNull(admin);
        assertEquals("Admin1", admin.getUserName());
        assertEquals(1, admin.getId());

        Admin notFound = adminServices.sendAdminById(99, adminList);
        assertNull(notFound);
    }


}
