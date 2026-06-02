package co.edu.uptc.Services;

import co.edu.uptc.Model.Admin;
import co.edu.uptc.Model.Booking;
import co.edu.uptc.Model.Client;
import co.edu.uptc.Model.Salon;
import co.edu.uptc.Security.PasswordSecurityService;
import co.edu.uptc.Security.ValidationService;

import java.util.List;

/**
 * Controlador principal del sistema (Fachada/Singleton).
 * Centraliza acceso a servicios. La vista nunca toca la lógica directamente.
 */
public class SistemaController {

    private static SistemaController instance;

    private final AdminServices          adminServices;
    private final ClientService          clientService;
    private final SalonServices          salonServices;
    private final BookingServices        bookingServices;
    private final PasswordSecurityService passwordService;
    private final ValidationService      validationService;

    private SistemaController() {
        this.adminServices     = new AdminServices();
        this.clientService     = new ClientService();
        this.salonServices     = new SalonServices();
        this.bookingServices   = new BookingServices();
        this.passwordService   = new PasswordSecurityService();
        this.validationService = new ValidationService();
    }

    public static SistemaController getInstance() {
        if (instance == null) {
            instance = new SistemaController();
        }
        return instance;
    }

    // =========================================================================
    // VALIDACIONES PÚBLICAS (las usa la vista antes de llamar al login)
    // =========================================================================

    /** Valida formato de cédula colombiana (3–10 dígitos, sin ceros puros) */
    public boolean isCedulaValida(String cedula) {
        return validationService.isValidCedula(cedula);
    }

    /** Valida formato de contraseña: mínimo 8 chars, 1 mayúscula, 1 carácter especial */
    public boolean isPasswordValida(String rawPassword) {
        return passwordService.isValidFormat(rawPassword);
    }

    /** Encripta una contraseña válida. Usar al registrar o actualizar contraseña. */
    public String encriptarPassword(String rawPassword) {
        return passwordService.encrypt(rawPassword);
    }

    // =========================================================================
    // CLIENTES
    // =========================================================================

    /**
     * Login de cliente con verificación BCrypt.
     * @param cedulaStr cédula como String (viene del TextField)
     * @param rawPassword contraseña en texto plano
     */
    public boolean loginCliente(String cedulaStr, String rawPassword) {
        if (!validationService.isValidCedula(cedulaStr)) return false;
        int id = validationService.parseCedula(cedulaStr);
        Client cliente = clientService.buscarClientPorId(id);
        if (cliente == null) return false;
        return passwordService.verify(rawPassword, cliente.getPassword());
    }

    /** @deprecated Usa loginCliente(String, String) para BCrypt */
    public boolean loginCliente(int id, String password) {
        return clientService.validateAccess(id, password);
    }

    public Client obtenerCliente(int id) {
        return clientService.buscarClientPorId(id);
    }

    public boolean registrarCliente(Client cliente) {
        return clientService.registrarCliente(cliente);
    }

    public boolean actualizarCliente(int id, Client clienteActualizado) {
        return clientService.modificarCliente(id, clienteActualizado);
}

    // =========================================================================
    // ADMINISTRADORES
    // =========================================================================

    /**
     * Login de admin con validación de cédula colombiana y verificación BCrypt.
     * @param cedulaStr cédula como String (viene del TextField)
     * @param rawPassword contraseña en texto plano que escribe el usuario
     * @return true si las credenciales son correctas
     */
    public boolean loginAdmin(String cedulaStr, String rawPassword) {
        if (!validationService.isValidCedula(cedulaStr)) return false;
        int id = validationService.parseCedula(cedulaStr);
        Admin admin = adminServices.sendAdminById(id);
        if (admin == null) return false;
        return passwordService.verify(rawPassword, admin.getPassword());
    }

    /** @deprecated Usa loginAdmin(String, String) para BCrypt */
    public boolean loginAdmin(int id, String password) {
        return adminServices.validateAccess(id, password);
    }

    public Admin obtenerAdmin(int id) {
        return adminServices.sendAdminById(id);
    }

    public boolean actualizarAdmin(int id, Admin adminActualizado) {
    return adminServices.updateAdmin(id, adminActualizado);
    }

    // =========================================================================
    // SALONES
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
    // RESERVAS
    // =========================================================================

    public void crearReserva(Booking reserva) {
        bookingServices.saveNewBooking(reserva);
    }

    public List<Booking> obtenerTodasLasReservas() {
        return bookingServices.getListBooking();
    }
    public List<Booking> sendBookingByClient(int id){
        return bookingServices.sendBookingListByClient(id);
    }
    public void cancelBooking(int id){
        bookingServices.cancelBooking(id);
    }
    public double calculatePrice(Booking booking){
        return bookingServices.calculatePriceBooking(booking);
    }
}