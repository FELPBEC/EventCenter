package co.edu.uptc.Util;

import co.edu.uptc.Services.BookingServices;
import co.edu.uptc.Services.SalonServices;
import co.edu.uptc.Model.Salon;
import java.util.Comparator;
import java.util.List;
public class RankedSalon {
    private SalonServices salonServices = new SalonServices();
    private BookingServices bookingServices = new BookingServices();
    public RankedSalon() {
        this.salonServices = new SalonServices();
        this.bookingServices = new BookingServices();
    }   
    public RankedSalon(SalonServices salonServices, BookingServices bookingServices) {
        this.salonServices = salonServices;
        this.bookingServices = bookingServices;
    }
    /**Método que identifica el número de reservaciones por salón y se lo asigna
     * @param salonList lista de salones a las que se les asignara el número de reservaciobnes
     */
    public void setNumberOfReservations(){
        List<Salon> salonList = salonServices.getListSalones();
        for (int i = 0; i < salonList.size(); i++) {
            Salon salon = salonList.get(i);
            int numberOfReservations=bookingServices.sendBookingListBySalon(salon.getId()).size();
            salon.setNumberOfReservations(numberOfReservations);
            salonServices.updateSalon(salon.getId(), salon);  
        }
    }

    /**Método que envía la lista de los 5 mejores salones según el número de reservaciones
     * 
     * @return
     */
public List<Salon> sendTop5BestSalons() {
    return salonServices.getListSalones().stream()
        // Ordenamos comparando el número de reservaciones de forma inversa (descendente)
        .sorted(Comparator.comparingInt(Salon::getNumberOfReservations).reversed())
        // Tomamos solo los primeros 5
        .limit(5)
        .toList();
}
}
