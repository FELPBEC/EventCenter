package co.edu.uptc.Services;

import co.edu.uptc.Model.Admin;
import co.edu.uptc.Persistence.AdminJsonRepository;

import java.util.ArrayList;
import java.util.List;

/**Clase de servicio de administradores que sirve para añadir, eliminar, leer o modificar a un administrador
 * 
 * @author Felipe Becerra
 * @version v 1.0
 */
public class AdminServices {
    private List<Admin> listAdmins;
    private AdminJsonRepository repository;
    /**Método constructor de la clase  de administración de servicios
     *
     */
    public AdminServices() {
        this.repository= new AdminJsonRepository("Admins.json");
        this.listAdmins= repository.sendJsonAdminList();

        if (listAdmins==null) {
            this.listAdmins= new ArrayList<>();
        }
    }

    public AdminServices(List<Admin> listAdminsSimulada) {
        this.listAdmins = listAdminsSimulada;
        this.repository = null; 
    }

    /**Método que almacena un nuevo Administrador y lo agrega a la lista
     * 
     * @param admin el nuevo administrador
     */
    public void saveNewAdmin(Admin admin){
        listAdmins.add(admin);
        if (this.repository != null) {
            repository.saveJsonAdminList(listAdmins);
        }
    }

    /**Método que envía un admnistrador atraves de su ID
     * 
     * @param id id del administrador
     * @return  devuelve un administrador en caso de encontrarlo
     * @return en caso de no encontrarlo devuelve un objeto vacio
     */
    public Admin sendAdminById(int id){
        return listAdmins.stream().filter(a->a.getId()==id).findFirst().orElse(null);
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
    /**Método para eliminar un administrador por posición en la lista
     * Hace uso del método sendAdminPosition para determinar la posición con base en la id
     * 
     * @param id identificador númerico de un administrador en la lista
     * @param adminList la lista donde se buscará al administrador para ser eliminado
     */
    public void fireAdmin(int id){
        listAdmins.removeIf(admin -> admin.getId() == id);
        if (this.repository != null) {
            repository.saveJsonAdminList(listAdmins);
        }
    }

    /**Método que actualiza un administrador a traves de su posición
     * Hace uso del método sendAdminPosition para determinar la posición con base en la id
     * 
     * @param id posición del administrador en la lista
     * @param admin el administrador actualizado
     * @param adminList la lista de administradores en la que vamos a actualizar el administrador
     */
    public boolean updateAdmin(int id,Admin updatedAdmin){
        Admin admin = sendAdminById(id);
        if(admin!=null){
            for (int i = 0; i < listAdmins.size(); i++) {
                if (listAdmins.get(i).getId() == id) {
                    listAdmins.set(i, updatedAdmin);
                    if (this.repository != null) {
                        repository.saveJsonAdminList(listAdmins);
                    }
                    return true;
                }
            }  
        }
        return false;
    }

    /**Método que envía un booleando dependiendo de si las credenciales proporcionadas son correctas o no
     * 
     * @param id    identificador númerico del administrador
     * @param password  contraseña del administrador que debe coincidir con la id propocionada
     * 
     * @return  verdadero si correspondente
     * @return falso si no corresponden 
     * @return en caso de no encontrarse el administrador no lo intentará
     * 
     */
    public boolean validateAccess(int id, String password){
        Admin admin = sendAdminById(id);
        return admin != null && admin.getPassword().equals(password);
    }

    public List<Admin> getListAdmins() {
        return this.listAdmins;
    }
    
}
