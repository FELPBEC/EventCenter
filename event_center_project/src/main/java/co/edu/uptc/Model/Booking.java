package co.edu.uptc.Model;
/**Clase reserva que modela las reservas de salones que hacen los clientes
 *  
 * @author Felipe Becerra
 * @version v 1.1
 */
public class Booking {
    private int id;
    private Client client;
    private Salon salon;
    //La fecha se instancia como String para poder almacenarla en repositorios Json, Csv
    private String startDate;
    private int amountOfHours;
    private String endDate;

    /**Constructor vacío para facilitar la carga de objetos de tipo Reservas por parte de los repositorios
     * 
     */
    public Booking() {
    }
    
    /**Constructor con párametros en caso de desear crear una Reserva manualmente
     * 
     * @param id    identificador númerico de la reserva 
     * @param client    Cliente que realiza la reserva {@link Client}
     * @param salon      Salón que fue reservado {@link Salon}
     * @param startDate Fecha de inició de la reserva
     * @param amountOfHours cantidad de horas que dura la reserva
     * @param endDate    Fecha en que termina la reserva
     */

    public Booking(int id, Client client, Salon salon, String startDate, int amountOfHours, String endDate) {
        this.id = id;
        this.client = client;
        this.salon = salon;
        this.startDate = startDate;
        this.amountOfHours = amountOfHours;
        this.endDate = endDate;
    }



    /**Método que envía la id de la reserva
     * 
     * @return id de la reserva 
     */
    public int getId() {
        return id;
    }

    /**Método que modifica la id de la reserva
     * 
     * @param id la nueva id de la reserva
     */
    public void setId(int id) {
        this.id = id;
    }

    /**Método que envía el objeto cliente {@link Client} que hace la reservación
     * 
     * @return el cliente que hace la reservación
     */
    public Client getClient() {
        return client;
    }

    /**Método que modifica el cliente {@link Client} que hace la reservación 
     * 
     * @param clien el nuevo cliente (o el cliente modificado) que hace la reservación
     */
    public void setClient(Client clien) {
        this.client = clien;
    }

    /**Método que envía el salón {@link Salon} que se reservó
     * 
     * @return el salón que ha sido reservado 
     */
    public Salon getSalon() {
        return salon;
    }
    /**Modifica el salón {@link Salon} que se reservó
     * 
     * @param salon el nuevo salón (o el salón modificado) que se reservó
     */
    public void setSalon(Salon salon) {
        this.salon = salon;
    }

    /**Método que envía la fecha en que se hizo la reservación
     * 
     * @return la fecha en que se hizo la reservación (cadena de texto String)
     */
    public String getStartDate() {
        return startDate;
    }

    /**Método que modifica la fecha en que se realizó la reservación 
     * 
     * @param startDate nueva fecha en la que se realizó la reservación (cadena de texto String)
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    /**Método que envía la cantidad de horas que dura la reservación
     * 
     * @return horas que dura la reservación
     */
    public int getAmountOfHours() {
        return amountOfHours;
    }

    /**Método para modificar la cantidad de horas que dura la reserva
     * 
     * @param amountOfHours nueva cantidad de horas que dura la reservación
     */
    public void setAmountOfHours(int amountOfHours) {
        this.amountOfHours = amountOfHours;
    }

    /**Método que envía la fecha en que finaliza la reserva
     * 
     * @return la fecha en que finaliza la reserva
     */
    public String getEndDate() {
        return endDate;
    }

    /**Método que modifica la fecha en que termina la reserva
     * 
     * @param endDate nueva fecha en que termina la reserva
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    
    

    
    
    
}
