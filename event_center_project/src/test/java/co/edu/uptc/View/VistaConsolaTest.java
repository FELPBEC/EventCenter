package co.edu.uptc.View;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import co.edu.uptc.Model.Client;
import co.edu.uptc.Persistence.ClientJsonRepository;

public class VistaConsolaTest {

    private final InputStream originalIn = System.in;

    @AfterEach
    void restoreSystemIn() {
        System.setIn(originalIn);
    }

    @Test
    void testSeleccionarIdioma() throws Exception {
        System.setIn(new ByteArrayInputStream("2\n".getBytes()));
        VistaConsola vista = new VistaConsola();

        vista.seleccionarIdioma();

        ResourceBundle bundle = (ResourceBundle) getPrivateField(vista, "mensajes");
        assertEquals("en", bundle.getLocale().getLanguage());
    }

    @Test
    void testIniciarMenu() {
        System.setIn(new ByteArrayInputStream("3\n".getBytes()));
        VistaConsola vista = new VistaConsola();

        assertDoesNotThrow(vista::iniciarMenu);
    }

    @Test
    void testRegistrarCliente() throws Exception {
        String input = "999\nTest User\npass999\nemail@test.com\n3000000000\n1\n4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        VistaConsola vista = new VistaConsola();
        setPrivateField(vista, "listClient", new ArrayList<Client>());

        ClientJsonRepository stubRepository = new ClientJsonRepository("Cliente.json") {
            @Override
            public void saveClientList(List<Client> clientList) {
                
            }
        };
        setPrivateField(vista, "clientJsonRepository", stubRepository);

        assertDoesNotThrow(vista::registrarCliente);

        @SuppressWarnings("unchecked")
        List<Client> listClient = (List<Client>) getPrivateField(vista, "listClient");
        assertEquals(1, listClient.size());
        Client created = listClient.get(0);
        assertEquals(999, created.getId());
        assertEquals("Test User", created.getUserName());
        assertTrue(created.isEmpresarial());
    }

    private static Object getPrivateField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    private static void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
