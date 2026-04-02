package co.edu.uptc.Model;
/**Clase reserva que modela las reservas de salones que hacen los clientes
 *  
 * @author Felipe Becerra
 * @version v 1.0
 */
public class Booking {
    private String id;
    private Client client;
    private Salon salon;
    //las fechas y horas se modelan como Strings y no como LocalDate o LocalTime para poder almacenar en repositorios .Json
    private String date;
    private String starTime;
    private String endTime;

    /**Constructor vacío para facilitar la carga de objetos de tipo Reservas por parte de los repositorios
     * 
     */
    public Booking() {
    }
    
    /**Método constructor con párametros en caso de desear crear una reservación manualmente 
     * 
     * @param id id de la reservación 
     * @param client    el cliente que realizó la reservación {@link Client}
     * @param salon el salón que fue reservado {@link Salon}
     * @param date fecha para la que se reservo el salón 
     * @param starTime  hora de comienzo de la reservación
     * @param endTime hora de finalización de la reservación 
     */
    public Booking(String id, Client client, Salon salon, String date, String starTime, String endTime){
        this.id = id;
        this.client = client;
        this.salon = salon;
        this.date = date;
        this.starTime = starTime;
        this.endTime = endTime;
    }

    /**Método que envía la id de la reserva
     * 
     * @return id de la reserva 
     */
    public String getId() {
        return id;
    }

    /**Método que modifica la id de la reserva
     * 
     * @param id la nueva id de la reserva
     */
    public void setId(String id) {
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
    public String getDate() {
        return date;
    }

    /**Método que modifica la fecha en que se realizó la reservación 
     * 
     * @param date nueva fecha en la que se realizó la reservación (cadena de texto String)
     */
    public void setDate(String date) {
        this.date = date;
    }
    
    /**Método para envíar la hora de comienzo de la reservación 
     * 
     * @return hora de comienzo reservación (cadena de texto String)
     */
    public String getStarTime() {
        return starTime;
    }

    
    public void setStarTime(String starTime) {
        this.starTime = starTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "booking [id=" + id + ", clien=" + client + ", salon=" + salon + ", date=" + date + ", starTime="
                + starTime + ", endTime=" + endTime + "]";
    }
    
    
    
}
