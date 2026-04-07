package co.edu.uptc.Util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import co.edu.uptc.Model.Booking;
import co.edu.uptc.Model.Client;
import co.edu.uptc.Model.Salon;

public class ExportadorServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void testExportarReporteIngresosCSV() throws IOException {
        String originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());

        try {
            ExportadorService exportador = new ExportadorService();
            List<Booking> reservas = new ArrayList<>();

            Client client = new Client("pepe", 1234, "1234", "3142159789", "julian.moreno21@gmail.com", true);
            Salon salon = new Salon(1, "Afrodita", 200, 230000.0);
            Booking booking = new Booking(1, client, salon, "2026/08/25/12:12:12", 2, "2026/08/25/14:12:12");
            booking.setPrice(414000.0);
            reservas.add(booking);

            boolean exported = exportador.exportarReporteIngresosCSV("2026-08-01", "2026-08-31", 414000.0, reservas, "reporte_test");
            assertTrue(exported);

            Path output = tempDir.resolve("reportes").resolve("reporte_test.csv");
            assertTrue(Files.exists(output), "El archivo CSV debe existir");

            String content = Files.readString(output);
            assertTrue(content.contains("Reporte de Ingresos - Centro de Eventos Elite"));
            assertTrue(content.contains("Periodo:,2026-08-01 a 2026-08-31"));
            assertTrue(content.contains("Afrodita"));
            assertTrue(content.contains("414000.0"));
        } finally {
            System.setProperty("user.dir", originalUserDir);
        }
    }

    @Test
    void testExportarReporteIngresosJson() throws IOException {
        String originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());

        try {
            ExportadorService exportador = new ExportadorService();
            List<Booking> reservas = new ArrayList<>();

            Client client = new Client("pepe", 1234, "1234", "3142159789", "julian.moreno21@gmail.com", true);
            Salon salon = new Salon(1, "Afrodita", 200, 230000.0);
            Booking booking = new Booking(1, client, salon, "2026/08/25/12:12:12", 2, "2026/08/25/14:12:12");
            booking.setPrice(414000.0);
            reservas.add(booking);

            boolean exported = exportador.exportarReporteIngresosJson("2026-08-01", "2026-08-31", 414000.0, reservas, "reporte_test_json");
            assertTrue(exported);

            Path output = tempDir.resolve("reportes").resolve("reporte_test_json.json");
            assertTrue(Files.exists(output), "El archivo JSON debe existir");

            String content = Files.readString(output);
            assertTrue(content.contains("\"empresa\": \"reporte_test_json\""));
            assertTrue(content.contains("\"fechaInicio\": \"2026-08-01\""));
            assertTrue(content.contains("\"fechaFinal\": \"2026-08-31\""));
            assertTrue(content.contains("Afrodita"));
            assertTrue(content.contains("414000.0"));
        } finally {
            System.setProperty("user.dir", originalUserDir);
        }
    }
}
