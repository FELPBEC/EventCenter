package co.edu.uptc.Services;

import co.edu.uptc.Model.Admin;
import co.edu.uptc.Persistence.JsonRepository;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.util.List;

/**Clase de servicio de administradores que sirve para añadir, eliminar, leer o modificar a un administrador
 * 
 * @author Felipe Becerra
 * @version v 1.0
 */
public class AdminServices {
    private JsonRepository<Admin> repository;

    /**Método constructor de la clase  de administración de servicios
     * Genera un nuevo repositorio tipo Json para modificar la lista de Administradores
     */
    public AdminServices() {
        Type type = new TypeToken<List<Admin>>(){
        }.getType();
        repository= new JsonRepository<>("Admins.json", type);
    }

    /**Método que lee el archivo Json y retorna una lista de administradores
     * 
     * @return  lista de administradores 
     */
    public List<Admin> enlistAdmins(){
        return repository.findAll();
    }

    /**Método que almacena un nuevo Administrador y lo agrega a la lista
     * 
     * @param admin el nuevo administrador
     */
    public void saveNewAdmin(Admin admin){
        repository.save(admin);
    }

    /**Método que envía un admnistrador atraves de su ID
     * 
     * @param id id del administrador
     * @return  devuelve un administrador en caso de encontrarlo
     * @return en caso de no encontrarlo devuelve un objeto vacio
     */
    public Admin sendAdminById(int id){
        return enlistAdmins().stream().filter(a->a.getId()==id).findFirst().orElse(null);
    }

    /**Método que valida que un administrador exista a traves de su ID
     * importantisimo para evitar excepciones en los metodos de busqueda y actualización
     * @param id la id del administrador que se busca
     * @return verdadero si se encuentra
     * @return falso si no se encuentra
     */
    public boolean searchAdminById(int id){
        if(sendAdminById(id)!=null){
            return true;
        }else{
            return false;
        }
    }
    
    /**Método que envía la posición del administrador en la lista mediante la id, 
     * útil para encontrar elimar o modificar por posición
     * @param id    identificador del admnistrador
     * @return      la posición del administrador en caso de encontrarlo
     * @return      en caso de no encontrarlo enviara una posición inválida
     */
    public int sendAdminPosition(int id){
        int position=enlistAdmins().size()+1;
        for (int i = 0; i < enlistAdmins().size(); i++) {
            if(enlistAdmins().get(i).getId()==id){
                position=i;
            }
        }
        return position;
    }
    
    /**Método para eliminar un administrador por posición en la lista
     * Hace uso del método sendAdminPosition para determinar la posición con base en la id
     * 
     * @param id identificador númerico de un administrador en la lista
     */
    public void fireAdmin(int id){
        repository.deleteObject(sendAdminPosition(id));
    }

    /**Método que actualiza un administrador a traves de su posición
     * Hace uso del método sendAdminPosition para determinar la posición con base en la id
     * 
     * @param id posición del administrador en la lista
     * @param admin el administrador actualizado
     */
    public void updateAdmin(int id,Admin admin){
        repository.updateNew(sendAdminPosition(id), admin);
    }

    /**Método que envía la información de un administrador a través de su id
     * 
     * @param id    identificador númerico del administrador
     * @return      una cadena de texto con los datos del administrador
     */
    public String showAdminInfo(int id){
        return sendAdminById(id).toString();
    }

    /**Método que envía un booleando dependiendo de si las credenciales proporcionadas son correctas o no
     * 
     * @param id    identificador númerico del administrador
     * @param password  contraseña del administrador que debe coincidir con la id propocionada
     * @return  verdadero si correspondente
     * @return falso si no corresponden 
     */
    public boolean validateAccess(int id, String password){
        Admin admin= sendAdminById(id);
        if (admin.getPassword().equals(password)) {
            return true;
        }else{
            return false;
        }
    }
    
}
