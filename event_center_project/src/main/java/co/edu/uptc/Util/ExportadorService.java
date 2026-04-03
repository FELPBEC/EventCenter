package co.edu.uptc.Util;

import java.io.FileWriter;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.edu.uptc.Model.Booking;
import co.edu.uptc.Model.ReporteIngresos;

/**
 * Servicio encargado de transformar objetos y listas
 * en archivos físicos (JSON y CSV) guardados en el disco duro.
 * @author Julian Moreno
 * @version v 1.0
 */
public class ExportadorService {
    /**
     * Exporta el reporte de ingresos a un archivo JSON usando Gson.
     * @param fechaInicio String de la fecha inicial (ej. "2026-01-01")
     * @param fechaFin String de la fecha final (ej. "2026-01-31")
     * @param totalIngresos El dinero total ya calculado
     * @param reservas Lista de reservas filtradas por ese periodo
     * @param nombreArchivo Nombre del archivo que el usuario eligió en la Vista
     * @return true si se exportó bien, false si ocurrió un error en el disco.
     */
    public boolean exportarReporteIngresosJson(String fechaInicio, String fechaFin, double totalIngresos, List<Booking> reservas, String nombreArchivo){
        ReporteIngresos reporteIngresos= new ReporteIngresos(nombreArchivo, fechaInicio, fechaFin, totalIngresos, reservas);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer= new FileWriter(nombreArchivo+".json")){
            gson.toJson(reporteIngresos,writer);
            return true;
        } catch (Exception e) {
            System.err.println("Error al intentar crear el archivo json "+e.getMessage());
            return false;
        }
    }
}
