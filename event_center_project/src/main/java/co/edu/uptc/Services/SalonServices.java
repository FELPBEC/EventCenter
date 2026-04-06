package co.edu.uptc.Services;

import co.edu.uptc.Model.Salon;
import java.util.List;
/**Clase de servicios de salones que sirve para agregar, eliminar, modificar y leer salones {@link Salon}
 * 
 * @author Felipe Becerra
 * @version v 1.0
 */
public class SalonServices {

    private List<Salon> listSalones;
    public SalonServices() {
    }


    /**Método que guarda un nuevo salón en la lista y en el archivo json
     * 
     * @param salon el nuevo salón que se ingresa
     */
    public boolean addNewSalon(Salon salon, List<Salon> listSalones){
        if (buscarSalonPorNombre(salon.getSalonName(),listSalones) != null) {
            return false; // El salón ya existe
        }
        listSalones.add(salon);
        return true;
    }

    /**Método que envía un salón através de su id
     * 
     * @param id identificador númerico del salón
     * @return  un objeto salon
     */
    public Salon sendSalonById(int id,List<Salon> listSalons ){
        return listSalons.stream().filter(s->s.getId()==id).findFirst().orElse(null);
    }

    /**Método para buscar un salón atraves de su id 
     * importante para evitar excepciones si no se encuentra en la lista
     * 
     * @param id identificador númerico del salón
     * @return  verdadero si se encontro el salón
     * @return falso si no se encontro el salón
     */
    public boolean searchSalonById(int id, List<Salon> listSalons){
        if(sendSalonById(id,listSalons)!=null){
            return true;
        }else{
            return false;
        }
    }

    /**Método que busca la posición del salón en la lista a traves de su id
     * importante para eliminar y actualizar salones
     * 
     * @param id identificador númerico del salón
     * @return la posición del salón en la lista si lo encuentra
     * @return una posición superior a la lista;
     */
    public int sendSalonPosition(int id, List<Salon> listSalons){
        int position= listSalons.size()+1;
        for (int i = 0; i < listSalons.size(); i++) {
            if(id==listSalons.get(i).getId()){
                position=i;
            }
        }
        return position;
    }
    
    /**Método para eliminar un salón a través de su posición en la lista
     * Hace uso del método sendSalonPosition para enviarle la posición con base en la id
     * @param id identificador númerico del salón
     */
    public void deleteSalon(int id,List<Salon> listSalons ){
        listSalons.removeIf(salon -> salon.getId()==id);
    }

     /**
     * Modifica los datos de un salón existente.
     * @param id El ID del salón que se desea modificar.
     * @param updatedSalon el salón modificado
     * @param listSalons lista de salones donde se hara la modificación
     */
    public boolean updateSalon(int id,Salon updatedSalon,List<Salon> listSalons) {
        Salon salonEncontrado = sendSalonById(id,listSalons);
        if (salonEncontrado != null) {
            for (int i = 0; i < listSalons.size(); i++) {
            if (listSalons.get(i).getId() == id) {
                listSalons.set(i, updatedSalon);
            }
        }
            return true;
        }
        return false;
    }

    /**Método para generar de forma automatica el siguiente identificador númerico de salón
     * 
     * @return el nuevo identificador númerico de salón
     */
    public int generateNewId(List<Salon> listSalons){
        int biggestId=0;
        for (int i = 0; i < listSalons.size(); i++) {
            if(listSalons.get(i).getId()>biggestId){
                biggestId=listSalons.get(i).getId();
            }
        }
        return biggestId+1;
    }

    /**
     * Busca un salón específico por su nombre (ignorando mayúsculas y minúsculas).
     * @param nombre El nombre del salón a buscar.
     * @return El objeto Salon si lo encuentra, de lo contrario retorna null.
     */
    public Salon buscarSalonPorNombre(String nombre,List<Salon> listSalones) {
        for (Salon salon : listSalones) {
            if (salon.getSalonName().equalsIgnoreCase(nombre)) {
                return salon;
            }
        }
        return null;
    }

   


    
}
