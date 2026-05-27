package co.edu.uptc.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.Model.Salon;

public class SalonServicesTest {

    private SalonServices salonServices;
    private List<Salon> salonList;

    @BeforeEach
    void setUp() {
        salonList = new ArrayList<>();
        salonList.add(new Salon(1, "Afrodita", 200, 230000.0));
        salonList.add(new Salon(2, "Zeus", 12, 120000.0));
        salonList.add(new Salon(4, "Apolo", 200, 400000.0));
        
        // Inyectamos la lista simulada usando el nuevo constructor
        salonServices = new SalonServices(salonList);
    }

    @Test
    void testAddNewSalon() {
        Salon newSalon = new Salon(5, "Hera", 150, 180000.0);

        // Llamada al método corregido
        boolean added = salonServices.addNewSalon(newSalon);

        assertTrue(added);
        assertEquals(4, salonServices.getListSalones().size());
        assertEquals("Hera", salonServices.buscarSalonPorNombre("Hera").getSalonName());

        // Intentar agregar un salón con nombre repetido
        Salon duplicatedSalon = new Salon(6, "Afrodita", 100, 100000.0);
        boolean addedDuplicate = salonServices.addNewSalon(duplicatedSalon);
        
        assertFalse(addedDuplicate);
    }

    @Test
    void testGenerateNewId() {
        // Llamada al método corregido sin parámetro
        assertEquals(5, salonServices.generateNewId());

        salonServices.getListSalones().add(new Salon(10, "Titan", 300, 500000.0));
        assertEquals(11, salonServices.generateNewId());
    }

    @Test
    void testSendSalonById() {
        Salon salon = salonServices.sendSalonById(4);

        assertNotNull(salon);
        assertEquals("Apolo", salon.getSalonName());

        assertNull(salonServices.sendSalonById(99));
    }

    @Test
    void testUpdateSalon() {
        Salon updatedSalon = new Salon(2, "Zeus Plus", 20, 140000.0);

        // Llamada al método corregido
        boolean updated = salonServices.updateSalon(2, updatedSalon);

        assertTrue(updated);
        assertEquals("Zeus Plus", salonServices.sendSalonById(2).getSalonName());
        assertEquals(140000.0, salonServices.sendSalonById(2).getPriceByHour());

        boolean updatedMissing = salonServices.updateSalon(99, updatedSalon);
        assertFalse(updatedMissing);
    }
}
