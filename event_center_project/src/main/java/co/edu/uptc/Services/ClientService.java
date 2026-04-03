package co.edu.uptc.Services;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import co.edu.uptc.Interfaces.Repository;
import co.edu.uptc.Model.Client;
import co.edu.uptc.Persistence.JsonRepository;

/**
 * Clase responsable de manejar toda la logica del modelo Client.
 * Implementa el CRUD atravez de la comuniacion con el repositorio json.
 * @author Julian Moreno
 * @version 1.0
 */
public class ClientService {
    private Repository<Client> repositoryClient;
    private List<Client> listClient;

    /**
     * Constructor de la clase ClientService.
     * Inicializa el repositorio de la clase Client trayendo la lista de objetos del archivo Cliente.json.
     */
    public ClientService(){
        Type type= new TypeToken<List<Client>>(){}.getType();
        this.repositoryClient= new JsonRepository<>("Cliente.json", type);
        this.listClient= repositoryClient.findAll();
    }   
    /**
     * Registra un nuevo cliente en el sistema tras validar que su ID no exista.
     * @param cliente El objeto Client con los datos a registrar.
     * @return true si el registro fue exitoso, false si la ID ya estaba registrada en el sistema.
     */
    public boolean registrarCliente(Client client){
        if (buscarClientPorId(client.getId())!=null) {
            return false;
        }
        listClient.add(client);
        repositoryClient.save(client);
        return true;
    }
    /**
     * Consulta y retorna un cliente en base a su identificador único (ID).
     * @param id Identificador númerico del cliente a buscar.
     * @return El objeto Client si es encontrado, o null si no existe.
     */
    public Client buscarClientPorId(int idClient){
        for (Client client : listClient) {
            if (client.getId()==idClient) {
                return client;
            }
        }
        return null;
    }

    public boolean validateAccess(int id, String password){
        Client client= buscarClientPorId(id);
        if (client!=null) {
            if (client.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Retorna la lista completa de clientes registrados en el sistema.
     * @return Lista de objetos Client.
     */
    public List<Client> obtenerListaClientes(){
        this.listClient= repositoryClient.findAll();
        return listClient;
    }
    /**
     * Modifica los datos de un cliente existente. El ID no puede ser modificado.
     * @param id Identificador del cliente a modificar.
     * @param nuevoNombre Nuevo nombre del cliente.
     * @param nuevaContrasena Nueva contraseña .
     * @param nuevoTelefono Nuevo número de teléfono.
     * @param nuevoCorreo Nuevo correo electrónico.
     * @param esEmpresarial true si es empresarial, false si es particular.
     * @return true si la modificación fue exitosa, false si el cliente no fue encontrado.
     */
    public boolean modificarCliente(int id, String nuevoNombre, String nuevaContrasena, String nuevoTelefono, String nuevoCorreo, boolean esEmpresarial){
        Client clienteEncontrado= buscarClientPorId(id);
        if (clienteEncontrado!=null) {
            clienteEncontrado.setId(id);
            clienteEncontrado.setEmpresarial(esEmpresarial);
            clienteEncontrado.setPassword(nuevaContrasena);
            clienteEncontrado.setUserName(nuevoNombre);
            clienteEncontrado.setEmail(nuevoCorreo);
            clienteEncontrado.setPhoneNumber(nuevoTelefono);

            repositoryClient.updateAll(listClient);
            return true;
        }
        return false;
    }
    /**
     * Elimina un cliente del sistema en base a su ID.
     * * @param id Identificador único del cliente a eliminar.
     * @return true si el cliente fue eliminado correctamente, false si no se encontró.
     */
    public boolean eliminarCliente(int id){
        Client clienteEncontrado= buscarClientPorId(id);
        if (clienteEncontrado!=null) {
            this.listClient.remove(clienteEncontrado);
            repositoryClient.updateAll(listClient);
            return true;
        }
        return false;
    }

}
