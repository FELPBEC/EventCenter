package co.edu.uptc.Abstracts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

    @Test
    void testConstructorVacio() {
        Person person = new Person() {
        };

        assertAll("Persona vacía",
            () -> assertNull(person.getUserName(), "userName debe ser null"),
            () -> assertEquals(0, person.getId(), "id debe ser 0"),
            () -> assertNull(person.getPassword(), "password debe ser null"),
            () -> assertNull(person.getPhoneNumber(), "phoneNumber debe ser null"),
            () -> assertNull(person.getEmail(), "email debe ser null")
        );
    }

    @Test
    void testConstructorConParametros() {
        Person person = new Person("Juan Pérez", 12345, "secreto", "3101234567", "juan@correo.com") {
        };

        assertAll("Constructor con parámetros",
            () -> assertEquals("Juan Pérez", person.getUserName()),
            () -> assertEquals(12345, person.getId()),
            () -> assertEquals("secreto", person.getPassword()),
            () -> assertEquals("3101234567", person.getPhoneNumber()),
            () -> assertEquals("juan@correo.com", person.getEmail())
        );
    }

    @Test
    void testSettersAndGetters() {
        Person person = new Person() {
        };

        person.setUserName("Ana Maria");
        person.setId(98765);
        person.setPassword("clave123");
        person.setPhoneNumber("3207654321");
        person.setEmail("ana@correo.com");

        assertAll("Setters y getters",
            () -> assertEquals("Ana Maria", person.getUserName()),
            () -> assertEquals(98765, person.getId()),
            () -> assertEquals("clave123", person.getPassword()),
            () -> assertEquals("3207654321", person.getPhoneNumber()),
            () -> assertEquals("ana@correo.com", person.getEmail())
        );
    }
}
