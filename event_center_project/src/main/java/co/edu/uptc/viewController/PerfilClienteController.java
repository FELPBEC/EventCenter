package co.edu.uptc.viewController;

import co.edu.uptc.Model.Client;
import co.edu.uptc.Services.SessionManager;
import co.edu.uptc.View.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.ResourceBundle;

public class PerfilClienteController {

    @FXML private Label lblNombre;
    @FXML private Label lblCedula;
    @FXML private Label lblTelefono;
    @FXML private Label lblCorreo;
    
    @FXML private Button btnToggleMenu;
    @FXML private VBox sideMenuContainer; // Referencia al menú lateral inyectado

    @FXML private ResourceBundle resources;

    @FXML
    public void initialize() {
        cargarDatosPerfil();
    }

    private void cargarDatosPerfil() {
        // Validación de seguridad básica
        if (!SessionManager.getInstance().haySesionCliente()) return;
        
        Client cliente = SessionManager.getInstance().getClienteActual();
        
        // NOTA: Ajusta estos getters según cómo estén en tu clase Client
        lblNombre.setText(cliente.getUserName());
        lblCedula.setText(Integer.toString(cliente.getId()));
        lblTelefono.setText(cliente.getPhoneNumber());
        lblCorreo.setText(cliente.getEmail());
    }

    @FXML
    private void toggleMenu() {
        if (sideMenuContainer != null) {
            boolean visible = sideMenuContainer.isVisible();
            sideMenuContainer.setVisible(!visible);
            sideMenuContainer.setManaged(!visible);
        }
    }

    @FXML
    private void modificarPerfil() {
        try {
            App.setRoot("editarPerfilCliente");
        } catch (IOException e) {
            mostrarAlerta(AlertType.ERROR, "Error de Navegación", "No se pudo cargar la vista de edición.");
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}