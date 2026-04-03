package co.edu.uptc.Util;

import java.util.ArrayList;
import java.util.List;
import co.edu.uptc.Model.Salon;
import co.edu.uptc.Services.SalonServices;
/**Clase filtro de salones encargada de filtrar salones por precio, fechas y capacidad
 * 
 * @author Felipe Becerra
 * @version v 1.0
 */
public class FiltrerService {
    private boolean filtrerByPrice;
    private boolean filtrerByCapacity;
    private boolean filtrerByDate;
    private SalonServices salonServices= new SalonServices();
    private List<Salon> allSalons= salonServices.enlistSalons();
    
    /**Método constructor vacío que inicializa los filtros en falso (osea todos apagados)
     * 
     */
    public FiltrerService() {
        filtrerByCapacity=false;
        filtrerByDate=false;
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



    public boolean isFiltrerByCapacity() {
        return filtrerByCapacity;
    }



    public void setFiltrerByCapacity(boolean filtrerByCapacity) {
        this.filtrerByCapacity = filtrerByCapacity;
    }



    public boolean isFiltrerByDate() {
        return filtrerByDate;
    }



    public void setFiltrerByDate(boolean filtrerByDate) {
        this.filtrerByDate = filtrerByDate;
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
            if(allSalons.get(i).getPriceByHour()<=budget){
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

    
}
