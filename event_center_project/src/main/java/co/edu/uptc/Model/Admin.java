package co.edu.uptc.Model;
import co.edu.uptc.Abstracts.Person;

/**Clase que representa a los administradores del centro de reservas,
 *  hereda métodos y atributos de la clase abstracta {@link Person} 
 * 
 * @author Felipe Becerra
 * @version v 1.0
 * 
 */
public class Admin extends Person{
    private int nivelAcceso;
    /**Método constructor vacío de la clase administrador para facilitar su importación desde archivos Json y Csv */
    public Admin() {
    }

    /**Método constructor con párametros de la clase administrador en caso de querer generar uno de forma manual
     * Párametros heredados de la clase abstracta {@link Person}
     * @param userName nombre completo del administrador
     * @param id        identificador númerico del administrador 
     * @param password  contraseña del administrador
     * @param phoneNumber   número de telefono del admnistrador
     * @param email     correo eléctronico del administrador
     * Párametro propio de la clase
     * @param nivelAcceso   nivel de acceso del administrador
     */
    public Admin(String userName, int id, String password, String phoneNumber, String email, int nivelAcceso) {
        super(userName, id, password, phoneNumber, email);
        this.nivelAcceso = nivelAcceso;
    }
    /**Método que envía el nivel de acceso del administrador
     * 
     * @return nivel de acceso de administrador (número)
     */
    public int getNivelAcceso() {
        return nivelAcceso;
    }

    /**Método que modifica el nivel de acceso del administrador
     * 
     * @param nivelAcceso  nuevo nivel de acceso del adminsitrador (número)
     */
    public void setNivelAcceso(int nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }
    
    
    
    

    
}
