package co.edu.uptc.Util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.Model.Booking;
import co.edu.uptc.Model.Salon;
import co.edu.uptc.Services.BookingServices;
import co.edu.uptc.Services.SalonServices;
/**Clase filtro de salones encargada de filtrar salones por precio, fechas y capacidad
 * 
 * @author Felipe Becerra
 * @version v 1.0
 */
public class FiltrerService {
    private boolean filtrerByPrice;
    private boolean filtrerByCapacity;
    private LocalDateTime startDate;
    private int hoursOfBooking;
    private SalonServices salonServices= new SalonServices();
    private BookingServices bookingServices = new BookingServices();
    private List<Booking> allBookings= bookingServices.enlistBookings();
    private List<Salon> allSalons= salonServices.enlistSalons();
    
    /**Método constructor del filtro que solicita en primera instancia una fecha y las horas de reserva
     * dado que es el principal filtro de búsqueda y es importante para no generar reservaciones erroneas
     * los otros filtros de capacidad y precio pueden activarse o desactivarse, por eso es que no son necesarios para el constructor
     * @param startDate  fecha de comienzo de la reserva
     * @param hoursOfBokking    horas de la reserva 
     */
    public FiltrerService(LocalDateTime startDate, int hoursOfBokking) {
        this.startDate=startDate;
        this.hoursOfBooking=hoursOfBokking;
        filtrerByCapacity=false;
        filtrerByPrice=false;
    }

    /**Método que envía un verdadero si el filtro por precio esta activado
     * o un falso si esta desactivado
     * 
     * @return estado del filtro
     */
    public boolean isFiltrerByPrice() {
        return filtrerByPrice;
    }


    /**Método que modifica el estado del filtro por precio
     * 
     * @param filtrerByPrice nuevo estado del filtro
     */
    public void setFiltrerByPrice(boolean filtrerByPrice) {
        this.filtrerByPrice = filtrerByPrice;
    }


    /**Método que envía si el filtro por capacidad esta activado o no
     * 
     * @return estado del filtro
     */
    public boolean isFiltrerByCapacity() {
        return filtrerByCapacity;
    }


    /**Método que modifica si el filtro de capacidad esta activado o no
     * 
     * @param filtrerByCapacity nuevo estado activo o no
     */
    public void setFiltrerByCapacity(boolean filtrerByCapacity) {
        this.filtrerByCapacity = filtrerByCapacity;
    }
    


    /**Método que envía la fecha del filtro
     * 
     * @return fecha del filtro
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**Método que modifica la fecha del filtro
     * 
     * @param startDate nueva fecha del filtro
     */
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    /**Método que obtiene las horas de reserva del filtro
     * 
     * @return  horas de reserva del filtro
     */
    public int getHoursOfBooking() {
        return hoursOfBooking;
    }

    /**Método que modifica las horas de reserva del filtro
     * 
     * @param hoursOfBooking horas de reserva del filtro
     */
    public void setHoursOfBooking(int hoursOfBooking) {
        this.hoursOfBooking = hoursOfBooking;
    }

    /**Filtra los salones por precio y envía una lista con todos los salones con menor o igual presupuesto
     * 
     * @param budget presupuesto del cliente para la reserva del salón
     * @return  lista de salones que cumplen con el presupuesto
     * @return en caso de no encontrar ninguno enviara una lista vacía
     */
    public List<Salon> filterByPrice(double budget){
        List<Salon> salons = new ArrayList<>();
        for (int i = 0; i < allSalons.size(); i++) {
            if((allSalons.get(i).getPriceByHour()*hoursOfBooking)<=budget){
                salons.add(allSalons.get(i));
            }
        }
        return salons;
    }
    
    /**Método que filtra salones por capacidad de personas
     * 
     * @param capacity capacidad de personas requerida para la reserva
     * @return Lista de salones que cumplen con la capacidad requerida
     * @return en caso de no encontrar ningun salón que cumpla con la capacidad envía una lista vacía
     */
    public List<Salon> filtrerByCapacity(int capacity){
        List<Salon> salons = new ArrayList<>();
        for (int i = 0; i < allSalons.size(); i++) {
            if(allSalons.get(i).getCapacity()<=capacity){
                salons.add(allSalons.get(i));
            }
        }
        return salons;
    }

    public List<Salon>filterByDate(){
        List<Salon> salons= new ArrayList<>();
        List<Booking> bookingBySalon= new ArrayList<>();
        for (int i = 0; i < allSalons.size(); i++) {
            int id=allSalons.get(i).getId();
            
        }
        
    }

    
}
