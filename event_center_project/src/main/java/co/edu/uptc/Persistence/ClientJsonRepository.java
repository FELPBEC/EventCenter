package co.edu.uptc.Persistence;

import java.util.List;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import co.edu.uptc.Model.Client;

/**Clase ClientJsonRepository que importa y exporta listas desde un archivo Json
 * 
 * @author Felipe Becerra
 * @version v 1.0
 */
public class ClientJsonRepository {
     private JsonRepository<Client> repositoryClient;

     /**Método constructor de la clase que inicializa el JsonRepository
      * 
      * @param pathname
      */
     public ClientJsonRepository(String pathname) {
        Type type= new TypeToken<List<Client>>(){}.getType();
        this.repositoryClient= new JsonRepository<>(pathname, type);
     }

     /**Método que envía la lista de clientes desde el archivo .Json
      * 
      * @return la lista de clientes del Json
      */
     public List<Client> sendJsonClientList(){
        return repositoryClient.findAll();
     }

     /**Método que guarda una lista de clientes en un archivo .Json
      * 
      * @param clientList
      */
     public void saveClientList(List<Client> clientList){
        repositoryClient.updateAll(clientList);
     }
     
}
