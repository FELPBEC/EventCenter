package co.edu.uptc.Persistence;

import java.util.List;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import co.edu.uptc.Model.Admin;
/**Clase de tipo JsonRepository para administradores {@link Admin} para poder importar y exportar la lista de administradores a archivos .json
 * 
 * @author Felipe Becerra
 * @version v 1.0
 */
public class AdminJsonRepository {
    private JsonRepository<Admin> repository;


/**Método constructor de la clase JsonRepository que instancia el jsonRepository */
    public AdminJsonRepository() {
        Type type = new TypeToken<List<Admin>>(){
        }.getType();
        repository= new JsonRepository<>("Admins.json", type);
    }

    /**Método que envía la lista de administradores 
     * 
     * @return Lista de administradores en Json
     */
    public List<Admin> sendJsonAdminList(){
        return repository.findAll();
    }

    /**Método que guarda la lista de adminsitradores en json
     * 
     * @param adminList lista de aadministradores
     */
    public void saveJsonAdminList(List<Admin> adminList){
        repository.updateAll(adminList);
    }


    
}
