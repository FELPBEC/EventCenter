package co.edu.uptc.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ReporteIngresosTest {
    List<Booking> bookings = new ArrayList<>();

    {
        bookings.add(new Booking());
    }

    ReporteIngresos reporte = new ReporteIngresos(
                "Mi Empresa",
                "2026-04-01",
                "2026-04-30",
                500.0,
                bookings
        );
    @Test
    void testGetEmpresa() {
        assertEquals("Mi Empresa", reporte.getEmpresa());
    }

    @Test
    void testGetFechaInicio() {
        assertEquals("2026-04-01", reporte.getFechaInicio());
    }

    @Test
    void testGetFechaFinal() {
        assertEquals("2026-04-30", reporte.getFechaFinal());
    }

    @Test
    void testGetIngresosTotales() {
        assertEquals(500.0, reporte.getIngresosTotales());
    }

    @Test
    void testGetListBooking() {
        assertEquals(bookings, reporte.getListBooking());
    }

    @Test
    void testGetTotalReservasAtendidas() {
        assertEquals(1, reporte.getTotalReservasAtendidas());
    }

    @Test
    void testSetEmpresa() {
        reporte.setEmpresa("Nueva Empresa");
        assertEquals("Nueva Empresa", reporte.getEmpresa());
    }

    @Test
    void testSetFechaFinal() {
        reporte.setFechaFinal("2026-05-01");
        assertEquals("2026-05-01", reporte.getFechaFinal());
    }

    @Test
    void testSetFechaInicio() {
        reporte.setFechaInicio("2026-03-01");
        assertEquals("2026-03-01", reporte.getFechaInicio());
    }

    @Test
    void testSetIngresosTotales() {
        reporte.setIngresosTotales(750.0);
        assertEquals(750.0, reporte.getIngresosTotales());
    }

    @Test
    void testSetListBooking() {
        List<Booking> nuevaLista = new ArrayList<>();
        nuevaLista.add(new Booking());
        nuevaLista.add(new Booking());
        reporte.setListBooking(nuevaLista);
        assertEquals(nuevaLista, reporte.getListBooking());
        
    }

    @Test
    void testSetTotalReservasAtendidas() {
        reporte.setTotalReservasAtendidas(5);
        assertEquals(5, reporte.getTotalReservasAtendidas());
    }
}
