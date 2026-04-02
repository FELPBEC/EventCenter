package co.edu.uptc.Abstracts;
/**Clase abstracta que representa a cualquier tipo de persona involucrada en el sístema 
 * Sirve como molde en caso de querer añadir más tipos de acceso al servicio
 * 
 * @author Felipe Becerra
 * @version v 1.0
 */
public abstract class Person {
    protected String userName;
    protected int id;
    protected String password;
    protected String phoneNumber;
    protected String email;
    /**Contructor vacío de la clase persona que permite a todas las clases de tipo repositorio construir los objetos al leer archivos
     * 
     */
    public Person() {
    }
    
    /**Método constructor con párametros de la clase persona en caso de desear crearla manualmente
     * 
     * @param userName  Nombre completo de la persona
     * @param id        Identificador único de la persona
     * @param password  Contraseña del usuario
     * @param phoneNumber   número telefonico de la persona
     * @param email           Correo electronico de la persona
     */
    public Person(String userName, int id, String password, String phoneNumber, String email) {
        this.userName = userName;
        this.id = id;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }




    /**Método que envía el nombre de usuario
     * 
     * @return  el nombre del usuario
     */
    public String getUserName() {
        return userName;
    }

    /**Método para modificar el nombre de usuario
     * 
     * @param username un String con el nuevo nombre de usuario
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**Método que envía la id del usuario
     * 
     * @return la id del usuario
     */
    public int getId() {
        return id;
    }

    /**Método para modificar la id del usuario
     * 
     * @param id la nueva id del usuario
     */
    public void setId(int id) {
        this.id = id;
    }
    /**Método que envía la contraseña (cadena de cáracteres)
     * 
     * @return
     */
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    

    
    
}
