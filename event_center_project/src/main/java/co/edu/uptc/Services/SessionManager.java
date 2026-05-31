package co.edu.uptc.Services;

import co.edu.uptc.Model.Admin;
import co.edu.uptc.Model.Client;

/**
 * Singleton que mantiene la sesion activa durante toda la ejecucion.
 * Soporta sesion de Admin y sesion de Client de forma independiente.
 * Solo uno puede tener sesion activa a la vez en el flujo normal,
 * pero la estructura permite manejarlos por separado sin conflicto.
 */
public class SessionManager {

    private static SessionManager instance;

    private Admin  adminActual;
    private Client clienteActual;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // Sesion Admin
    // -------------------------------------------------------------------------

    public void iniciarSesionAdmin(Admin admin) {
        this.adminActual   = admin;
        this.clienteActual = null; // limpia cliente si habia uno activo
    }

    public void cerrarSesionAdmin() {
        this.adminActual = null;
    }

    public Admin getAdminActual() {
        return adminActual;
    }

    public boolean haySesionAdmin() {
        return adminActual != null;
    }

    // -------------------------------------------------------------------------
    // Sesion Cliente
    // -------------------------------------------------------------------------

    public void iniciarSesionCliente(Client cliente) {
        this.clienteActual = cliente;
        this.adminActual   = null; // limpia admin si habia uno activo
    }

    public void cerrarSesionCliente() {
        this.clienteActual = null;
    }

    public Client getClienteActual() {
        return clienteActual;
    }

    public boolean haySesionCliente() {
        return clienteActual != null;
    }

    // -------------------------------------------------------------------------
    // Cerrar cualquier sesion activa (util para el boton cerrar sesion general)
    // -------------------------------------------------------------------------

    public void cerrarSesion() {
        this.adminActual   = null;
        this.clienteActual = null;
    }
}