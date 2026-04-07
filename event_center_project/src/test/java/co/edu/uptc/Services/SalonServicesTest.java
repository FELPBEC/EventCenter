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
        salonServices = new SalonServices();
        salonList = new ArrayList<>();
        salonList.add(new Salon(1, "Afrodita", 200, 230000.0));
        salonList.add(new Salon(2, "Zeus", 12, 120000.0));
        salonList.add(new Salon(4, "Apolo", 200, 400000.0));
    }

    @Test
    void testAddNewSalon() {
        Salon newSalon = new Salon(5, "Hera", 150, 180000.0);

        boolean added = salonServices.addNewSalon(newSalon, salonList);

        assertTrue(added);
        assertEquals(4, salonList.size());
        assertEquals("Hera", salonServices.buscarSalonPorNombre("Hera", salonList).getSalonName());

        // Intentar agregar un salón con nombre duplicado
        Salon duplicate = new Salon(6, "Afrodita", 100, 150000.0);
        boolean addedDuplicate = salonServices.addNewSalon(duplicate, salonList);
        assertFalse(addedDuplicate);
        assertEquals(4, salonList.size());
    }

    @Test
    void testBuscarSalonPorNombre() {
        Salon salon = salonServices.buscarSalonPorNombre("zeus", salonList);

        assertNotNull(salon);
        assertEquals(2, salon.getId());
        assertEquals("Zeus", salon.getSalonName());

        assertNull(salonServices.buscarSalonPorNombre("NoExiste", salonList));
    }

    @Test
    void testDeleteSalon() {
        salonServices.deleteSalon(2, salonList);

        assertEquals(2, salonList.size());
        assertFalse(salonServices.searchSalonById(2, salonList));

        salonServices.deleteSalon(99, salonList);
        assertEquals(2, salonList.size());
    }

    @Test
    void testGenerateNewId() {
        assertEquals(5, salonServices.generateNewId(salonList));

        salonList.add(new Salon(10, "Titan", 300, 500000.0));
        assertEquals(11, salonServices.generateNewId(salonList));
    }

    @Test
    void testSearchSalonById() {
        assertTrue(salonServices.searchSalonById(1, salonList));
        assertFalse(salonServices.searchSalonById(99, salonList));
    }

    @Test
    void testSendSalonById() {
        Salon salon = salonServices.sendSalonById(4, salonList);

        assertNotNull(salon);
        assertEquals("Apolo", salon.getSalonName());

        assertNull(salonServices.sendSalonById(99, salonList));
    }

    
    @Test
    void testUpdateSalon() {
        Salon updatedSalon = new Salon(2, "Zeus Plus", 20, 140000.0);

        boolean updated = salonServices.updateSalon(2, updatedSalon, salonList);

        assertTrue(updated);
        Salon salon = salonServices.sendSalonById(2, salonList);
        assertEquals("Zeus Plus", salon.getSalonName());
        assertEquals(20, salon.getCapacity());
        assertEquals(140000.0, salon.getPriceByHour());

        assertFalse(salonServices.updateSalon(99, updatedSalon, salonList));
    }
}
