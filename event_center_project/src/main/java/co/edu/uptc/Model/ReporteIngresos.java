package co.edu.uptc.Model;

import java.util.List;

/**
 * Clase que sirve como "molde"  para estructurar visualmente el reporte de ingresos antes de exportarlo a JSON.
 * * @author Julian Moreno
 * @version v 1.0
 */
public class ReporteIngresos {
    
    private String empresa;
    private String fechaInicio;
    private String fechaFinal;
    private int totalReservasAtendidas; 
    private double ingresosTotales;
    private List<Booking> listBooking;

    /**
     * Constructor del Reporte.
     * Calcula automáticamente el número de reservas basándose en la lista que se le envie.
     * @param fechaInicio Fecha de inicio del filtro
     * @param fechaFin Fecha de fin del filtro
     * @param ingresosTotales Suma de dinero calculada en el servicio
     * @param detalleReservas Lista de reservas que aplican en ese periodo
     */
    public ReporteIngresos(String empresa, String fechaInicio, String fechaFinal, double ingresosTotales, List<Booking> listBooking) {
        this.empresa = empresa;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.ingresosTotales = ingresosTotales;
        this.listBooking = listBooking;

        if (listBooking!=null) {
            this.totalReservasAtendidas= listBooking.size();
        }else{
            this.totalReservasAtendidas=0;
        }
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public int getTotalReservasAtendidas() {
        return totalReservasAtendidas;
    }

    public void setTotalReservasAtendidas(int totalReservasAtendidas) {
        this.totalReservasAtendidas = totalReservasAtendidas;
    }

    public double getIngresosTotales() {
        return ingresosTotales;
    }

    public void setIngresosTotales(double ingresosTotales) {
        this.ingresosTotales = ingresosTotales;
    }

    public List<Booking> getListBooking() {
        return listBooking;
    }

    public void setListBooking(List<Booking> listBooking) {
        this.listBooking = listBooking;
    }

    

    
}
