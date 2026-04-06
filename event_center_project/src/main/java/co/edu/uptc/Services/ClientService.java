package co.edu.uptc.Services;

import java.util.List;
import co.edu.uptc.Model.Client;

/**
 * Clase responsable de manejar toda la logica del modelo Client.
 * Implementa el CRUD atravez de la comuniacion con el repositorio json.
 * @author Julian Moreno
 * @version 1.0
 */
public class ClientService {

    /**
     * Constructor de la clase ClientService.
     * Inicializa el repositorio de la clase Client trayendo la lista de objetos del archivo Cliente.json.
     */
    public ClientService(){
    }   
    /**
     * Registra un nuevo cliente en el sistema tras validar que su ID no exista.
     * @param cliente El objeto Client con los datos a registrar.
     * @return true si el registro fue exitoso, false si la ID ya estaba registrada en el sistema.
     */
    public boolean registrarCliente(Client client, List<Client> listClient){
        if (buscarClientPorId(client.getId(),listClient)!=null) {
            return false;
        }
        listClient.add(client);
        return true;
    }
    /**
     * Consulta y retorna un cliente en base a su identificador único (ID).
     * @param id Identificador númerico del cliente a buscar.
     * @return El objeto Client si es encontrado, o null si no existe.
     */
    public Client buscarClientPorId(int idClient, List<Client> listClient){
        for (Client client : listClient) {
            if (client.getId()==idClient) {
                return client;
            }
        }
        return null;
    }

    public boolean validateAccess(int id, String password, List<Client> listClient){
        Client client= buscarClientPorId(id,listClient);
        if (client!=null) {
            if (client.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Modifica los datos de un cliente existente. El ID no puede ser modificado.
     * @param id Identificador del cliente a modificar.
     * @param updatedClient cliente modificado
     * @param clientList lista donde se modificara el cliente
     * @return true si la modificación fue exitosa, false si el cliente no fue encontrado.
     */
    public boolean modificarCliente(int id, Client updatedClient, List<Client> clientList){
       Client clienteEncontrado =  buscarClientPorId(id,clientList);
        if (clienteEncontrado != null) {
            for (int i = 0; i < clientList.size(); i++) {
            if (clientList.get(i).getId() == id) {
                clientList.set(i, updatedClient);
            }
        }
            return true;
        }
        return false;
    }
    /**
     * Elimina un cliente del sistema en base a su ID.
     * * @param id Identificador único del cliente a eliminar.
     * @return true si el cliente fue eliminado correctamente, false si no se encontró.
     */
    public boolean eliminarCliente(int id,List<Client> clientList){
        Client clienteEncontrado= buscarClientPorId(id,clientList);
        if (clienteEncontrado!=null) {
            clientList.removeIf(client->client.getId()==id);
            return true;
        }
        return false;
    }

}
