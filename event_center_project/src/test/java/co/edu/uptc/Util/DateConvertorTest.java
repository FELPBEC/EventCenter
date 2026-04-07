package co.edu.uptc.Util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class DateConvertorTest {

    private final DateConvertor convertor = new DateConvertor();

    @Test
    void testStringToLocalDateTime() {
        String fecha = "2026/04/06/15:30:45";

        LocalDateTime resultado = convertor.StringToLocalDateTime(fecha);

        assertEquals(2026, resultado.getYear());
        assertEquals(4, resultado.getMonthValue());
        assertEquals(6, resultado.getDayOfMonth());
        assertEquals(15, resultado.getHour());
        assertEquals(30, resultado.getMinute());
        assertEquals(45, resultado.getSecond());
    }

    @Test
    void testLocalDateTimeToString() {
        LocalDateTime fecha = LocalDateTime.of(2026, 4, 6, 15, 30, 45);

        String resultado = convertor.localDateTimeToString(fecha);

        assertEquals("2026/04/06/15:30:45", resultado);
    }

    @Test
    void testRoundTripConversion() {
        String original = "2026/12/31/23:59:59";

        LocalDateTime fecha = convertor.StringToLocalDateTime(original);
        String convertido = convertor.localDateTimeToString(fecha);

        assertEquals(original, convertido);
    }

    @Test
    void testStringToLocalDateTimeInvalidFormat() {
        String fechaInvalida = "06-04-2026 15:30:45";

        assertThrows(Exception.class, () -> convertor.StringToLocalDateTime(fechaInvalida));
    }
}
