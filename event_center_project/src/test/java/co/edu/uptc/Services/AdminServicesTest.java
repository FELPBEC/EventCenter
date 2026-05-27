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
        adminList = new ArrayList<>();
        adminList.add(new Admin("Admin1", 1, "admin1@test.com", "pass1", "111111111"));
        adminList.add(new Admin("Admin2", 2, "admin2@test.com", "pass2", "222222222"));
        adminList.add(new Admin("Admin3", 3, "admin3@test.com", "pass3", "333333333"));
        
        // Inyectamos la lista simulada usando el nuevo constructor
        adminServices = new AdminServices(adminList);
    }

    @Test
    void testFireAdmin() {
        assertNotNull(adminServices.sendAdminById(2));
        adminServices.fireAdmin(2); 

        assertNull(adminServices.sendAdminById(2));
        assertEquals(2, adminServices.getListAdmins().size());
    }

    @Test
    void testSaveNewAdmin() {
        Admin newAdmin = new Admin("NewAdmin", 4, "new@test.com", "newpass", "444444444");

        adminServices.saveNewAdmin(newAdmin);

        assertEquals(4, adminServices.getListAdmins().size());
        assertNotNull(adminServices.sendAdminById(4));

        Admin savedAdmin = adminServices.sendAdminById(4);

        assertEquals("NewAdmin", savedAdmin.getUserName());
        assertEquals(4, savedAdmin.getId());
    }

    @Test
    void testSendAdminById() {
        Admin admin = adminServices.sendAdminById(1);
        assertNotNull(admin);
        assertEquals("Admin1", admin.getUserName());

        assertNull(adminServices.sendAdminById(99));
    }
}