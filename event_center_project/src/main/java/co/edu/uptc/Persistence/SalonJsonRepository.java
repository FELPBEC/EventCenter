package co.edu.uptc.Persistence;

import java.util.List;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import co.edu.uptc.Model.Salon;
/**Clase SalonJsonRepository que importa y exporta una lista de salones {@link Salon} 
 * 
 * @author Felipe Becerra
 * @version v 1.0
*/
public class SalonJsonRepository {
    private JsonRepository<Salon> repository;

    /**Método constructor de la clase SalonJsonRepository que incializa el repostirio de tipo Json
     * 
     * @param pathname La dirección del archivo Json donde del que se exportara e importara la lista de salones
     */
    public SalonJsonRepository(String pathname) {
        Type type= new TypeToken<List<Salon>>(){
        }.getType();
        repository= new JsonRepository<>(pathname, type);
    }

    /**Método que envía la lista de salones del archivo .Json
     * 
     * @return la lista de salones del .Json
     */
    public List<Salon> sendSalonList(){
        return repository.findAll();
    }

    /**Método que guarda la lista de salones en archivo .Json
     * 
     * @param salonList la lista que se debe guardar en el archivo .Json
     */
    public void saveSalonList(List<Salon> salonList){
        repository.updateAll(salonList);
    }

    
}
