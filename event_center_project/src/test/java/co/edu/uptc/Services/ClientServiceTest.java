package co.edu.uptc.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.Model.Client;

public class ClientServiceTest {

    private ClientService clientService;
    private List<Client> clientList;

    @BeforeEach
    void setUp() {
        clientService = new ClientService();
        clientList = new ArrayList<>();
        clientList.add(new Client("pepe", 1234, "1234", "3142159789", "julian.moreno21@gmail.com", true));
        clientList.add(new Client("luis", 45, "abc", "3000000000", "luis@test.com", false));
    }

    @Test
    void testBuscarClientPorId() {
        Client found = clientService.buscarClientPorId(1234, clientList);
        assertNotNull(found);
        assertEquals("pepe", found.getUserName());
        assertEquals(1234, found.getId());

        Client missing = clientService.buscarClientPorId(9999, clientList);
        assertNull(missing);
    }

    @Test
    void testRegistrarCliente() {
        Client newClient = new Client("maria", 78, "pass78", "3100000000", "maria@test.com", false);
        boolean created = clientService.registrarCliente(newClient, clientList);

        assertTrue(created);
        assertEquals(3, clientList.size());
        assertSame(newClient, clientService.buscarClientPorId(78, clientList));

        Client duplicate = new Client("copia", 1234, "pass", "3000000001", "copia@test.com", false);
        boolean createdDuplicate = clientService.registrarCliente(duplicate, clientList);
        assertFalse(createdDuplicate);
        assertEquals(3, clientList.size());
    }

    @Test
    void testValidateAccess() {
        assertTrue(clientService.validateAccess(1234, "1234", clientList));
        assertFalse(clientService.validateAccess(1234, "wrong", clientList));
        assertFalse(clientService.validateAccess(9999, "1234", clientList));
    }

    @Test
    void testModificarCliente() {
        Client updated = new Client("pepe editado", 1234, "newpass", "3140000000", "nuevo@mail.com", true);

        boolean modified = clientService.modificarCliente(1234, updated, clientList);
        assertTrue(modified);

        Client found = clientService.buscarClientPorId(1234, clientList);
        assertNotNull(found);
        assertEquals("pepe editado", found.getUserName());
        assertEquals("newpass", found.getPassword());
        assertEquals("nuevo@mail.com", found.getEmail());

        boolean modifiedMissing = clientService.modificarCliente(9999, updated, clientList);
        assertFalse(modifiedMissing);
    }

    @Test
    void testEliminarCliente() {
        boolean removed = clientService.eliminarCliente(45, clientList);
        assertTrue(removed);
        assertEquals(1, clientList.size());
        assertNull(clientService.buscarClientPorId(45, clientList));

        boolean removedMissing = clientService.eliminarCliente(9999, clientList);
        assertFalse(removedMissing);
        assertEquals(1, clientList.size());
    }
}
