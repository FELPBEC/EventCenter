package co.edu.uptc.Services;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import co.edu.uptc.Model.Booking;
import co.edu.uptc.Persistence.JsonRepository;
import java.lang.reflect.Type;

/**Clase Servicios de Reserva que crea, elimina, lee y modifica las reservas {@link Booking}
 * 
 * @author Felipe Becerra
 * @version v 1.0
 */
public class BookingServices {
    private JsonRepository<Booking> repository;

    /**Método constructor de la clase services que inicializa el repositorio Json
     * 
     */
    public BookingServices() {
        Type type= new TypeToken<List<Booking>>(){
        }.getType();
        repository= new JsonRepository<>("Booking.json", type);
    }

    /**Método que lee la lista de reservas del archivo Json  
     * 
     * @return lista de reservas
     */
    public List<Booking> enlistBookings(){
        return repository.findAll();
    }

    /**Método que guarda la reservación y añade una reservación al salón correspondiente
     * 
     * @param booking la nueva reserva que sera almacenada
     */
    public void saveNewBooking(Booking booking){
        repository.save(booking);
    }

    /**Método que busca y envía una reserva por la id
     * 
     * @param id id de la reserva que se busca
     * @return  la reserva buscada en caso de encontrarla
     * @return  un objeto vacío en caso de no encontrarlo
     */
    public Booking sendBookingById(int id){
        List<Booking> bookingList= enlistBookings();
        return bookingList.stream().filter(b->b.getId()==id).findFirst().orElse(null);
    }

    /**Método que busca y envía una reserva por la id del cliente que la solicito
     * 
     * @param idClient identificador númerico del cliente
     * @return la reserva buscada en caso de encontrarla
     * @return un objeto null en caso de no encontrarla
     */
    public Booking sendBookingByClientId(int idClient){
        List<Booking> bookingList= enlistBookings();
        return bookingList.stream().filter(b->b.getClient().getId()==idClient).findFirst().orElse(null);
    }

    /**Método que busca y envía una reserva por la id del salón
     * 
     * @param idSalon identificador númerico del salón
     * @return  la reserva buscada en caso de encontrarla
     * @return un objeto null en caso de no encontrarla
     */
    public Booking sendBookingBySalonId(int idSalon){
        List<Booking> bookingList= enlistBookings();
        return bookingList.stream().filter(b->b.getSalon().getId()==idSalon).findFirst().orElse(null);
    }
    
    /**Método que verifica si la id de la reserva que se busca existe o no 
     * 
     * @param id identificador númerico de la reserva
     * @return  verdadero si encuentra la reserva y existe
     * @return falso si no la encuentra y no existe
     */
    public boolean foundBookingById(int id){
            if(sendBookingById(id)!=null){
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
    public int sendBookingPosition(int id){
        List<Booking> bookingList= enlistBookings();
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
    public void cancelBooking(int id){
        repository.deleteObject(sendBookingPosition(id));
    }
    
    /**Método que modifica una reserva ya existente a través de la id
     * utiliza el método sendBookingPosition para obtener la posición de la reserva en la lista
     * 
     * @param id identificador númerico de la reserva
     * @param booking nueva reserva o reserva modificada que se sobreescribira en la posición
     */
    public void updateBooking(int id,Booking booking){
        repository.updateNew(sendBookingPosition(id), booking);
    }

    /**Método que envía la nueva id que tendra la reserva
     * 
     * @return la nueva Id que tendrá la reserva
     */
    public int sendNewId(){
        int biggestId=0;
        List<Booking> bookingList= enlistBookings();
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
     public List<Booking> sendBookingListBySalon(int idSalon){
        List<Booking> bookingBySalon= new ArrayList<>();
        List<Booking> allBookingList= enlistBookings();
        for (int i = 0; i < allBookingList.size(); i++) {
            if (allBookingList.get(i).getSalon()!=null && allBookingList.get(i).getSalon().getId()==idSalon) {
                bookingBySalon.add(allBookingList.get(i));
            }
        }
        return bookingBySalon;
    }


    
}
