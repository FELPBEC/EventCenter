package co.edu.uptc.Services;

import co.edu.uptc.Model.Admin;
import co.edu.uptc.Model.Booking;
import co.edu.uptc.Model.Client;
import co.edu.uptc.Model.Salon;

import java.util.List;

/**
 * Controlador principal del sistema (Fachada/Singleton).
 * Centraliza el acceso a todos los servicios para que la Vista (Consola o JavaFX)
 * no interactúe directamente con la lógica de negocio ni cree múltiples instancias.
 */
public class SistemaController {

    // 1. Instancia única (Singleton)
    private static SistemaController instance;

    // 2. Servicios centralizados
    private AdminServices adminServices;
    private ClientService clientService;
    private SalonServices salonServices;
    private BookingServices bookingServices;
    
    private SistemaController() {
        this.adminServices = new AdminServices();
        this.clientService = new ClientService();
        this.salonServices = new SalonServices();
        this.bookingServices = new BookingServices();
    }

    // 4. Método para obtener la instancia única en cualquier parte del proyecto
    public static SistemaController getInstance() {
        if (instance == null) {
            instance = new SistemaController();
        }
        return instance;
    }

    // =========================================================================
    //                        MÉTODOS DE CLIENTES
    // =========================================================================

    public boolean loginCliente(int id, String password) {
        return clientService.validateAccess(id, password);
    }

    public Client obtenerCliente(int id) {
        return clientService.buscarClientPorId(id);
    }

    public boolean registrarCliente(Client cliente) {
        return clientService.registrarCliente(cliente);
    }

    // =========================================================================
    //                        MÉTODOS DE ADMINISTRADORES
    // =========================================================================

    public boolean loginAdmin(int id, String password) {
        return adminServices.validateAccess(id, password);
    }

    public Admin obtenerAdmin(int id) {
        return adminServices.sendAdminById(id);
    }

    // =========================================================================
    //                        MÉTODOS DE SALONES
    // =========================================================================

    public List<Salon> obtenerTodosLosSalones() {
        return salonServices.getListSalones();
    }

    public boolean agregarSalon(Salon salon) {
        return salonServices.addNewSalon(salon);
    }

    public Salon buscarSalonPorNombre(String nombre) {
        return salonServices.buscarSalonPorNombre(nombre);
    }

    public Salon buscarSalonPorId(int id) {
        return salonServices.sendSalonById(id);
    }

    public boolean modificarSalon(int id, Salon salonActualizado) {
        return salonServices.updateSalon(id, salonActualizado);
    }

    // =========================================================================
    //                        MÉTODOS DE RESERVAS
    // =========================================================================

    public void crearReserva(Booking reserva) {
        bookingServices.saveNewBooking(reserva);
    }

    public List<Booking> obtenerTodasLasReservas() {
        return bookingServices.getListBooking();
    }

    // Agrega aquí más métodos intermediarios a medida que los necesites (ej. filtros, reportes)
}