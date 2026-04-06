package co.edu.uptc.Persistence;

import java.util.List;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import co.edu.uptc.Model.Booking;
/**Clase BookingJsonRepository que importa o exporta listas desde archivos .json
 * 
 * @author Felipe Becerra
 * @version v 1.0
 */
public class BookingJsonRepository {
    private JsonRepository<Booking> repository;

    public BookingJsonRepository(String pathname) {
        Type type= new TypeToken<List<Booking>>(){
        }.getType();
        repository= new JsonRepository<>(pathname, type);
    }

    /**Método que envía la lista de reservas {@link Booking} guardada en el repositorio Json
     * 
     * @return lista de reservas almacenadas en Json
     */
    public List<Booking> sendJsonBookingList(){
        return repository.findAll();
    }

    /**Método que guarda una lista de reservas en un archivo Json
     * 
     * @param bookingList lista de reservas que se guardara en el Json
     */
    public void saveBookingList(List<Booking> bookingList){
        repository.updateAll(bookingList);
    }
    
}
