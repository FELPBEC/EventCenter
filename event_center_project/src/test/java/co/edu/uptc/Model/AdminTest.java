package co.edu.uptc.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class AdminTest {
    @Test
    void testConstructorVacio() {
    Admin admin = new Admin();

    assertNull(admin.getUserName());
    assertEquals(0, admin.getId());
    assertNull(admin.getPassword());
    assertNull(admin.getPhoneNumber());
    assertNull(admin.getEmail());
    }
    @Test
    void testConstructorConParametros() {
        Admin admin = new Admin(
                "Julian",
                1,
                "julian@test.com",
                "1234",
                "3001234567"
        );

        assertEquals("Julian", admin.getUserName());
        assertEquals(1, admin.getId());
        assertEquals("1234", admin.getPassword());
        assertEquals("3001234567", admin.getPhoneNumber());
        assertEquals("julian@test.com", admin.getEmail());
    }
    
    

}
