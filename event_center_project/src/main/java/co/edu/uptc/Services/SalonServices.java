package co.edu.uptc.Services;

import co.edu.uptc.Model.Salon;
import co.edu.uptc.Persistence.JsonRepository;
import java.util.List;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
/**Clase de servicios de salones que sirve para agregar, eliminar, modificar y leer salones {@link Salon}
 * 
 * @author Felipe Becerra
 * @version v 1.0
 */
public class SalonServices {
    private JsonRepository<Salon> repository;

    public SalonServices() {
        Type type= new TypeToken<List<Salon>>(){
        }.getType();
        repository= new JsonRepository<>("Salon.json", type);
    }

    /**Método que lee el archivo json y envía la lista de salones 
     * 
     * @return lista de salones
     */
    public List<Salon> enlistSalons(){
        return repository.findAll();
    }

    /**Método que guarda un nuevo salón en la lista y en el archivo json
     * 
     * @param salon el nuevo salón que se ingresa
     */
    public void addNewSalon(Salon salon){
        repository.save(salon);
    }

    /**Método que envía un salón através de su id
     * 
     * @param id identificador númerico del salón
     * @return  un objeto salon
     */
    public Salon sendSalonById(int id){
        return enlistSalons().stream().filter(s->s.getId()==id).findFirst().orElse(null);
    }

    /**Método para buscar un salón atraves de su id 
     * importante para evitar excepciones si no se encuentra en la lista
     * 
     * @param id identificador númerico del salón
     * @return  verdadero si se encontro el salón
     * @return falso si no se encontro el salón
     */
    public boolean searchSalonById(int id){
        if(sendSalonById(id)!=null){
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
    public int sendSalonPosition(int id){
        int position= enlistSalons().size()+1;
        for (int i = 0; i < enlistSalons().size(); i++) {
            if(id==enlistSalons().get(i).getId()){
                position=i;
            }
        }
        return position;
    }
    
    /**Método para eliminar un salón a través de su posición en la lista
     * Hace uso del método sendSalonPosition para enviarle la posición con base en la id
     * @param id identificador númerico del salón
     */
    public void deleteSalon(int id){
        repository.deleteObject(sendSalonPosition(id));
    }

    /**Método para actualizar un salon con base en su posición
     * Hace uso del método sendSalonPosition para enviar la posición con base en la id
     * @param id    identificador númerico del salón que se desea modificar
     * @param salon el nuevo salón modificado
     */
    public void updateSalon(int id, Salon salon){
        repository.updateNew(sendSalonPosition(id), salon);
    }

    /**Método para generar de forma automatica el siguiente identificador númerico de salón
     * 
     * @return el nuevo identificador númerico de salón
     */
    public int generateNewId(){
        int biggestId=0;
        for (int i = 0; i < enlistSalons().size(); i++) {
            if(enlistSalons().get(i).getId()>biggestId){
                biggestId=enlistSalons().get(i).getId();
            }
        }
        return biggestId+1;
    }

    


    
}
