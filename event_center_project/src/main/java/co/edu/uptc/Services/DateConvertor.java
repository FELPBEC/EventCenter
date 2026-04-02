package co.edu.uptc.Services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import co.edu.uptc.Model.Booking;
/**Clase DateConvertor que permite convertir de una fecha de tipo localDateTime a un String y viseversa
 * de formato LocalDateTime a String para la clase de reservas {@link Booking}
 * de String a LocalDateTime para operaciones lógicas 
 * 
 * @author Felipe Becerra
 * @version v 1.0
 */
public class DateConvertor {
    public static final DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy/MM/dd/HH:mm:ss");

    /**Método que convierte de LocalDateTime (fecha) a String (cadena de texto) 
     * principalmente para almacenar las reservas {@link Booking} en archivos json
     * 
     * @param localDateTime la fecha
     * @return una cadena de texto de la fecha en formato (AÑO/MES/día/HORA:minuto:segundo)
     */
    public String localDateTimeToString(LocalDateTime localDateTime){
        return localDateTime.format(formatter);
    }

    /**Método que convierte de String(cadena de texto) a LocalDateTime(fecha)
     * 
     * @param line La cadena de texto que será convertida a fecha
     * @return  la fecha en formato (AÑO/MES/día/HORA:minuto:segundo)
     */
    public LocalDateTime StringToLocalDateTime(String line){
        return LocalDateTime.parse(line, formatter);
    }

}
