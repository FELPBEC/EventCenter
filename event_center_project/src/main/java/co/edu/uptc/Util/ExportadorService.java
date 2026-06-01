package co.edu.uptc.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// Librerías de iText para el PDF
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import co.edu.uptc.Model.Booking;
import co.edu.uptc.Model.ReporteIngresos;

/**
 * Servicio encargado de transformar objetos y listas
 * en archivos físicos (JSON, CSV, XML y PDF) guardados en el disco duro.
 */
public class ExportadorService {

    private String obtenerRutaReporte(String nombreArchivo, String extension) {
        String rutaDeEjecucion = System.getProperty("user.dir");
        File carpetaReportes = new File(rutaDeEjecucion, "reportes");
        
        if (!carpetaReportes.exists()) {
            carpetaReportes.mkdirs(); 
        }
        return new File(carpetaReportes, nombreArchivo + extension).getAbsolutePath();
    }

    public boolean exportarReporteIngresosJson(String fechaInicio, String fechaFin, double totalIngresos, List<Booking> reservas, String nombreArchivo){
        ReporteIngresos reporteIngresos = new ReporteIngresos(nombreArchivo, fechaInicio, fechaFin, totalIngresos, reservas);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String rutaCompleta = obtenerRutaReporte(nombreArchivo, ".json");
        try (FileWriter writer = new FileWriter(rutaCompleta)){
            gson.toJson(reporteIngresos, writer);
            return true;
        } catch (Exception e) {
            System.err.println("Error al intentar crear el archivo json " + e.getMessage());
            return false;
        }
    }
    
    public boolean exportarReporteIngresosCSV(String fechaInicio, String fechaFin, double totalIngresos, List<Booking> reservas, String nombreArchivo) {
        String rutaCompleta = obtenerRutaReporte(nombreArchivo, ".csv");
        try (FileWriter writer = new FileWriter(rutaCompleta)) {
            writer.write("Reporte de Ingresos - Centro de Eventos Elite\n");
            writer.write("Periodo:," + fechaInicio + " a " + fechaFin + "\n");
            writer.write("Ingresos Totales:," + totalIngresos + "\n");
            writer.write("\n");
            writer.write("ID Reserva,Cliente,Tipo Cliente,Salon,Fecha,Horas,Costo\n");
            for (Booking reserva : reservas) {
                String tipoCliente = reserva.getClient().isEmpresarial() ? "Empresarial" : "Particular";
                writer.write(reserva.getId() + "," + 
                            reserva.getClient().getUserName() + "," + 
                            tipoCliente + "," + 
                            reserva.getSalon().getSalonName() + "," + 
                            reserva.getStartDate() + "," + 
                            reserva.getAmountOfHours() + "," +
                            reserva.getPrice() + "\n");
            }
            return true; 
        } catch (Exception e) {
            System.err.println("Error al intentar crear el archivo CSV: " + e.getMessage());
            return false; 
        }
    }

    /**
     * Exporta el reporte de ingresos a un archivo XML estructurado.
     */
    public boolean exportarReporteIngresosXML(String fechaInicio, String fechaFin, double totalIngresos, List<Booking> reservas, String nombreArchivo) {
        String rutaCompleta = obtenerRutaReporte(nombreArchivo, ".xml");
        try (FileWriter writer = new FileWriter(rutaCompleta)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<reporteIngresos>\n");
            writer.write("    <meta>\n");
            writer.write("        <institucion>Centro de Eventos Elite</institucion>\n");
            writer.write("        <fechaInicio>" + fechaInicio + "</fechaInicio>\n");
            writer.write("        <fechaFin>" + fechaFin + "</fechaFin>\n");
            writer.write("        <totalIngresos>" + totalIngresos + "</totalIngresos>\n");
            writer.write("    </meta>\n");
            writer.write("    <reservas>\n");
            for (Booking reserva : reservas) {
                String tipoCliente = reserva.getClient().isEmpresarial() ? "Empresarial" : "Particular";
                writer.write("        <reserva id=\"" + reserva.getId() + "\">\n");
                writer.write("            <cliente>" + reserva.getClient().getUserName() + "</cliente>\n");
                writer.write("            <tipoCliente>" + tipoCliente + "</tipoCliente>\n");
                writer.write("            <salon>" + reserva.getSalon().getSalonName() + "</salon>\n");
                writer.write("            <fechaInicioReserva>" + reserva.getStartDate() + "</fechaInicioReserva>\n");
                writer.write("            <horas>" + reserva.getAmountOfHours() + "</horas>\n");
                writer.write("            <costo>" + reserva.getPrice() + "</costo>\n");
                writer.write("        </reserva>\n");
            }
            writer.write("    </reservas>\n");
            writer.write("</reporteIngresos>\n");
            return true;
        } catch (Exception e) {
            System.err.println("Error al intentar crear el archivo XML: " + e.getMessage());
            return false;
        }
    }

    /**
     * Exporta el reporte de ingresos a un PDF elegantemente diseñado usando iText.
     */
    public boolean exportarReporteIngresosPDF(String fechaInicio, String fechaFin, double totalIngresos, List<Booking> reservas, String nombreArchivo) {
        String rutaCompleta = obtenerRutaReporte(nombreArchivo, ".pdf");
        Document documento = new Document();
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(rutaCompleta));
            documento.open();

            // Fuentes estilizadas
            Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.DARK_GRAY);
            Font fuenteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.GRAY);
            Font fuenteTextoBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
            Font fuenteCelda = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

            // Encabezados del PDF
            Paragraph titulo = new Paragraph("Centro de Eventos Elite", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            Paragraph subtitulo = new Paragraph("Reporte Ejecutivo de Ingresos Financieros", fuenteSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(20);
            documento.add(subtitulo);

            // Información general
            documento.add(new Paragraph("Período del Reporte: " + fechaInicio + " hasta " + fechaFin, fuenteCelda));
            Paragraph ingresosParaf = new Paragraph("Ingresos Totales Recaudados: $" + totalIngresos, fuenteTextoBold);
            ingresosParaf.setSpacingAfter(20);
            documento.add(ingresosParaf);

            // Tabla de datos
            PdfPTable tabla = new PdfPTable(6); // 6 columnas
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10);

            // Títulos de columnas
            String[] columnas = {"ID", "Cliente", "Salón", "Fecha", "Horas", "Total"};
            for (String col : columnas) {
                PdfPCell celdaEncabezado = new PdfPCell(new Phrase(col, fuenteTextoBold));
                celdaEncabezado.setBackgroundColor(new BaseColor(245, 222, 179)); // Color arena suave
                celdaEncabezado.setHorizontalAlignment(Element.ALIGN_CENTER);
                celdaEncabezado.setPadding(6);
                tabla.addCell(celdaEncabezado);
            }

            // Filas con la información de las reservas
            for (Booking reserva : reservas) {
                tabla.addCell(new PdfPCell(new Phrase(String.valueOf(reserva.getId()), fuenteCelda)));
                tabla.addCell(new PdfPCell(new Phrase(reserva.getClient().getUserName(), fuenteCelda)));
                tabla.addCell(new PdfPCell(new Phrase(reserva.getSalon().getSalonName(), fuenteCelda)));
                tabla.addCell(new PdfPCell(new Phrase(reserva.getStartDate().toString(), fuenteCelda)));
                tabla.addCell(new PdfPCell(new Phrase(String.valueOf(reserva.getAmountOfHours()), fuenteCelda)));
                tabla.addCell(new PdfPCell(new Phrase("$" + reserva.getPrice(), fuenteCelda)));
            }

            documento.add(tabla);
            documento.close();
            return true;
        } catch (Exception e) {
            System.err.println("Error al generar el PDF: " + e.getMessage());
            return false;
        }
    }
}