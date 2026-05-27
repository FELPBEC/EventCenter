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
        clientList = new ArrayList<>();
        clientList.add(new Client("pepe", 1234, "1234", "3142159789", "julian.moreno21@gmail.com", true));
        clientList.add(new Client("luis", 45, "abc", "3000000000", "luis@test.com", false));
        
        // Inyectamos la lista simulada usando el nuevo constructor
        clientService = new ClientService(clientList);
    }

    @Test
    void testBuscarClientPorId() {
        // Llamamos al método modificado
        Client found = clientService.buscarClientPorId(1234);
        assertNotNull(found);
        assertEquals("pepe", found.getUserName());
        assertEquals(1234, found.getId());

        Client missing = clientService.buscarClientPorId(9999);
        assertNull(missing);
    }

    @Test
    void testRegistrarCliente() {
        Client newClient = new Client("maria", 789, "xyz", "3200000000", "maria@test.com", true);

        boolean added = clientService.registrarCliente(newClient);
        assertTrue(added);
        assertEquals(3, clientService.getListClients().size());

        // Intentar registrar con ID duplicado
        Client duplicate = new Client("pepe 2", 1234, "000", "111", "mail", false);
        boolean addedDuplicate = clientService.registrarCliente(duplicate);
        assertFalse(addedDuplicate);
        assertEquals(3, clientService.getListClients().size());
    }

    @Test
    void testValidateAccess() {
        assertTrue(clientService.validateAccess(1234, "1234"));
        assertFalse(clientService.validateAccess(1234, "wrong"));
        assertFalse(clientService.validateAccess(9999, "1234"));
    }

    @Test
    void testModificarCliente() {
        Client updated = new Client("pepe editado", 1234, "newpass", "3140000000", "nuevo@mail.com", true);

        boolean modified = clientService.modificarCliente(1234, updated);
        assertTrue(modified);

        Client found = clientService.buscarClientPorId(1234);
        assertNotNull(found);
        assertEquals("pepe editado", found.getUserName());
        assertEquals("newpass", found.getPassword());
        assertEquals("nuevo@mail.com", found.getEmail());

        boolean modifiedMissing = clientService.modificarCliente(9999, updated);
        assertFalse(modifiedMissing);
    }

    @Test
    void testEliminarCliente() {
        boolean removed = clientService.eliminarCliente(45);
        assertTrue(removed);
        assertEquals(1, clientService.getListClients().size());
        assertNull(clientService.buscarClientPorId(45));

        boolean removedMissing = clientService.eliminarCliente(99);
        assertFalse(removedMissing);
    }
}