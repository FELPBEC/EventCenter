package co.edu.uptc.Services;

import java.util.ArrayList;
import java.util.List;
import co.edu.uptc.Model.Booking;
import co.edu.uptc.Util.DateConvertor;
import java.time.LocalDateTime;

/**Clase Servicios de Reserva que crea, elimina, lee y modifica las reservas {@link Booking}
 * 
 * @author Felipe Becerra
 * @version v 1.0
 */
public class BookingServices {

    /**Método constructor de la clase services que inicializa el repositorio Json
     * 
     */
    public BookingServices() {
    }

    /**Método que agrega una reserva a la lista de reservas
     * 
     * @param booking nueva reserva que se va a guardar
     * @param bookingList   lista de reservas donde se va a guardar
     */
    public void saveNewBooking(Booking booking,List<Booking>bookingList){
        bookingList.add(booking);
    }
    /**Método que busca y envía una reserva por la id
     * 
     * @param id id de la reserva que se busca
     * @return  la reserva buscada en caso de encontrarla
     * @return  un objeto vacío en caso de no encontrarlo
     */
    public Booking sendBookingById(int id, List<Booking> bookingList){
        return bookingList.stream().filter(b->b.getId()==id).findFirst().orElse(null);
    }

    /**Método que busca y envía una reserva por la id del cliente que la solicito
     * 
     * @param idClient identificador númerico del cliente
     * @return la reserva buscada en caso de encontrarla
     * @return un objeto null en caso de no encontrarla
     */
    public Booking sendBookingByClientId(int idClient,List<Booking> bookingList ){
        return bookingList.stream().filter(b->b.getClient().getId()==idClient).findFirst().orElse(null);
    }

    /**Método que busca y envía una reserva por la id del salón
     * 
     * @param idSalon identificador númerico del salón
     * @return  la reserva buscada en caso de encontrarla
     * @return un objeto null en caso de no encontrarla
     */
    public Booking sendBookingBySalonId(int idSalon,List<Booking> bookingList ){
        return bookingList.stream().filter(b->b.getSalon().getId()==idSalon).findFirst().orElse(null);
    }
    
    /**Método que verifica si la id de la reserva que se busca existe o no 
     * 
     * @param id identificador númerico de la reserva
     * @return  verdadero si encuentra la reserva y existe
     * @return falso si no la encuentra y no existe
     */
    public boolean foundBookingById(int id, List<Booking> bookingList){
            if(sendBookingById(id, bookingList)!=null){
                return true;
            }else{
                return false;
            }
        }
    
    /**Método que envía la posición de la reserva en la lista a través de la id
     * 
     * @param id identificador númerico de la reserva
     * @return la posición de la reserva en la lista
     */
    public int sendBookingPosition(int id, List<Booking> bookingList){
        int position=bookingList.size()+1;
        for (int i = 0; i < bookingList.size(); i++) {
            if(bookingList.get(i).getId()==id){
                position=i;
            }
        }
        return position;
    }

    /**Método para eliminar una reserva atraves de su id
     * utiliza el método sendBookingPosition para obtener la posición de la reserva en la lista
     * le resta una reservación 
     * @param id   identificador númerico de la reserva
     */
    public void cancelBooking(int id,List<Booking> bookingList){
        bookingList.removeIf(booking->booking.getId()==id);
    }
    

     public boolean updateBooking(int id,Booking updatedBooking,List<Booking> bookingList) {
        Booking bookingFound = sendBookingById(id,bookingList);
        if (bookingFound != null) {
            for (int i = 0; i < bookingList.size(); i++) {
            if (bookingList.get(i).getId() == id) {
                bookingList.set(i, updatedBooking);
            }
        }
            return true;
        }
        return false;
    }
    /**Método que envía la nueva id que tendra la reserva
     * 
     * @return la nueva Id que tendrá la reserva
     */
    public int sendNewId(List<Booking> bookingList){
        int biggestId=0;
        for (int i = 0; i < bookingList.size(); i++) {
            if(bookingList.get(i).getId()>biggestId){
                biggestId=bookingList.get(i).getId();
            }
        }
        return biggestId+1;
    }

    /**Método que envía la lista de reservaciones de un mismo salón
     * 
     * @param idSalon id del salón del que se buscan las reservas
     * @return  una lista con todas las reservas del salón
     */
    public List<Booking> sendBookingListBySalon(int idSalon, List<Booking> allBookingList){
        List<Booking> bookingBySalon= new ArrayList<>();
        for (int i = 0; i < allBookingList.size(); i++) {
            if (allBookingList.get(i).getSalon()!=null && allBookingList.get(i).getSalon().getId()==idSalon) {
                bookingBySalon.add(allBookingList.get(i));
            }
        }
        return bookingBySalon;
    }

    /**Método que envía una lista de reservaciones por cliente
     * 
     * @param idClient identificador númerico del cliente
     * @return  una lista con las reservas que ha hecho el cliente
     */
    public List<Booking> sendBookingListByClient(int idClient, List<Booking>allBookingList){
        List<Booking> bookingByClient= new ArrayList<>();
        for (int i = 0; i < allBookingList.size(); i++) {
            if (allBookingList.get(i).getClient()!=null && allBookingList.get(i).getClient().getId()==idClient) {
                bookingByClient.add(allBookingList.get(i));
            }
        }
        return bookingByClient;
    }

    /**Cálcula el precio de una reserva aplicando descuentos
     *  
     * @param idBooking identificador númerico de la reserva
     * @return  el precio de la reserva (double)
     */
    public double calculatePriceBooking(Booking booking, List<Booking>bookingList){
        double price=(booking.getSalon().getPriceByHour())*(booking.getAmountOfHours());
        //Descuento a cliente empresarial
        if(booking.getClient().isEmpresarial()){
            price=price-(price*0.10);
        }
        //Descuento por reservas mayores o iguales a 8 horas
        if(booking.getAmountOfHours()>=8){
            price=price-(price*0.15);
        }
        //Descuento para clientes que tengan más de 3 reservaciones hechas
        if(sendBookingListByClient(booking.getClient().getId(),bookingList).size()>3){
            price=price-(price*0.2);
        }
        booking.setPrice(price);
        return price;
    }
    /**
     * Filtra y obtiene una lista de reservas cuya fecha de inicio se encuentra 
     * dentro de un rango de fechas especificado (ambas fechas inclusive).
     * @param fechaInicioStr La fecha inicial del rango en formato de texto (ej. "yyyy/MM/dd/HH:mm:ss").
     * @param fechaFinStr La fecha final del rango en formato de texto (ej. "yyyy/MM/dd/HH:mm:ss").
     * @return Una lista de objetos {@link Booking} que se encuentran dentro del rango de fechas dado. 
     * Si no se encuentran reservas, retorna una lista vacía.
     * @throws java.time.format.DateTimeParseException Si el formato de las fechas ingresadas no es el correcto.
     */
    public List<Booking> obtenerReservasPorRango(String fechaInicioStr, String fechaFinStr, List<Booking> bookingList) {
        DateConvertor convertor = new co.edu.uptc.Util.DateConvertor();
        LocalDateTime inicio = convertor.StringToLocalDateTime(fechaInicioStr);
        LocalDateTime fin = convertor.StringToLocalDateTime(fechaFinStr);

        List<Booking> filtradas = new ArrayList<>();
        
        for (Booking b : bookingList) {
            LocalDateTime fechaReserva = convertor.StringToLocalDateTime(b.getStartDate());
            if ((fechaReserva.isEqual(inicio) || fechaReserva.isAfter(inicio)) && 
                (fechaReserva.isEqual(fin) || fechaReserva.isBefore(fin))) {
                filtradas.add(b);
            }
        }
        return filtradas;
    }

    /**
     * Calcula la suma total de los ingresos generados a partir de una lista de reservas.
     * Itera sobre la lista proporcionada y suma el precio de cada una.
     * @param reservas La lista de objetos {@link Booking} sobre la cual se calcularán los ingresos.
     * @return El valor total (double) de los ingresos generados por las reservas de la lista. 
     * Retorna 0.0 si la lista está vacía.
     */
    public double calcularTotalIngresos(List<Booking> reservas) {
        double total = 0;
        for (Booking b : reservas) {
            total += b.getPrice();
        }
        return total;
    }

    
}
