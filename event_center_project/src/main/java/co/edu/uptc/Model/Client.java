package co.edu.uptc.Model;
import co.edu.uptc.Abstracts.Person;
/**Clase que representa a los clientes con métodos y atributos heredados de la clase {@link Person}
 * 
 * @author Felipe Becerra
 * @version v 1.0
 */
public class Client extends Person {
    private boolean Empresarial;

    /**Constructor vacio que sirve para que cualquier interfaz de tipo repositorio 
     * pueda crear los objetos de la clase con normalidad
     */
    public Client() {
    }

    /**Constructor con párametros de la clase Cliente
     * 
     * @param userName  nombre del cliente
     * @param id        identificador único del cliente
     * @param password     
     * @param phoneNumber
     * @param email
     * todos los anteriores heredados de la clase abstracta {@link Person}
     * @param empresarial
     */
    public Client(String userName, int id, String password, String phoneNumber, String email, boolean empresarial) {
        super(userName, id, password, phoneNumber, email);
        Empresarial = empresarial;
    }

    public boolean isEmpresarial() {
        return Empresarial;
    }

    public void setEmpresarial(boolean empresarial) {
        Empresarial = empresarial;
    }

    @Override
    public String toString() {
        return "Client [username=" + userName + ", Empresarial=" + Empresarial + ", id=" + id + "]";
    }

}
