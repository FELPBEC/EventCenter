package co.edu.uptc.Model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ClientTest {
    @Test
    void testIsEmpresarial() {
        Client client = new Client();

        client.setEmpresarial(true);
        assertTrue(client.isEmpresarial());

        client.setEmpresarial(false);
        assertFalse(client.isEmpresarial());

    }

    @Test
    void testSetEmpresarial() {
        Client client = new Client();
        client.setEmpresarial(true);
        assertTrue(client.isEmpresarial());
    }
}
