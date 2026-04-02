package co.edu.uptc.Model;
/**Clase que sirve de modelo para los salones que serán alquilados por los clientes
 * 
 * @author Felipe Becerra
 * @version v 1.0
 */
public class Salon {
    private int id;
    private String salonName;
    private double priceByHour;

    /**Constructor vacio para la clase,
     * con objetivo de poder cargar objetos guardados en repositorios
     * 
     */
    public Salon() {
    }

    /**Método constructor con párametros en caso de desear crear un salón manualmente
     * 
     * @param id    identificador númerico del salón debe ser único
     * @param salonName nombre del salón por ejemplo: "salón imperial"
     * @param priceByHour   el precio de una hora de alquiler del salón
     */
    public Salon(int id, String salonName, double priceByHour) {
        this.id = id;
        this.salonName = salonName;
        this.priceByHour = priceByHour;
    }

    /**Método que envía la id del salón
     * 
     * @return  la id del salón (número)
     */
    public int getId() {
        return id;
    }

    /**Método que modifica la id del salón
     * 
     * @param id la nueva id del salón (número)
     */
    public void setId(int id) {
        this.id = id;
    }

    /**Método que envía el nombre del salón
     * 
     * @return el nombre del salón (cadena de texto String)
     */
    public String getSalonName() {
        return salonName;
    }

    /**Método que modifica el nombre del salón
     * 
     * @param salonName el nuevo nombre del salón (cadena de texto String)
     */
    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    /**Método que envía el costo por hora de alquiler del salón
     * 
     * @return costo por hora (número décimal)
     */
    public double getPriceByHour() {
        return priceByHour;
    }

    /**Método que modifica el precio de alquiler por hora del salón 
     * 
     * @param priceByHour el nuevo precio de alquiler del salón
     */
    public void setPriceByHour(double priceByHour) {
        this.priceByHour = priceByHour;
    }

    @Override
    public String toString() {
        return "Salon [id=" + id + ", salonName=" + salonName + ", priceByHour=" + priceByHour + "]";
    }

    

    
    
}
