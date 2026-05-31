package co.edu.uptc.Security;

/**
 * Servicio de validaciones de negocio colombianas.
 * Por ahora cubre cédula de ciudadanía (3–10 dígitos, solo números).
 */
public class ValidationService {

    /**
     * Valida un número de cédula colombiana.
     * Reglas:
     *   - Solo dígitos numéricos
     *   - Entre 3 y 10 dígitos (cédulas históricas 3-8, NUIP 10)
     *   - No puede ser todo ceros
     *
     * @param cedula cadena con el número de cédula
     * @return true si es válida
     */
    public boolean isValidCedula(String cedula) {
        if (cedula == null || cedula.isBlank()) return false;
        if (!cedula.matches("\\d{3,10}")) return false;
        // Rechazar secuencias de puros ceros
        if (cedula.matches("0+")) return false;
        return true;
    }

    /**
     * Convierte la cédula validada a int para usar con los servicios existentes.
     * Llamar solo después de isValidCedula().
     *
     * @param cedula cadena validada
     * @return valor entero de la cédula
     */
    public int parseCedula(String cedula) {
        return Integer.parseInt(cedula);
    }
}