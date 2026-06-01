package co.edu.uptc.viewController;

import co.edu.uptc.Model.Client;
import co.edu.uptc.Services.SessionManager;
import co.edu.uptc.Services.SistemaController;
import co.edu.uptc.View.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditarPerfilClienteController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    
    @FXML private Button btnToggleMenu;
    @FXML private VBox sideMenuContainer;

    @FXML private ResourceBundle resources;

    @FXML
    public void initialize() {
        precargarDatos();
    }

    private void precargarDatos() {
        if (!SessionManager.getInstance().haySesionCliente()) return;
        
        Client cliente = SessionManager.getInstance().getClienteActual();
        txtNombre.setText(cliente.getUserName());
        txtTelefono.setText(cliente.getPhoneNumber());
        txtCorreo.setText(cliente.getEmail());
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
    private void cancelar() {
        try {
            App.setRoot("perfilCliente");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void guardarCambios() {
        String nuevoNombre = txtNombre.getText().trim();
        String nuevoTelefono = txtTelefono.getText().trim();
        String nuevoCorreo = txtCorreo.getText().trim();

        // 1. Validar que no haya campos vacíos
        if (nuevoNombre.isEmpty() || nuevoTelefono.isEmpty() || nuevoCorreo.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campos Incompletos", "Por favor llene todos los campos solicitados.");
            return;
        }

        // 2. Confirmación
        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Cambios");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de que desea guardar los cambios?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isEmpty() || resultado.get() != ButtonType.OK) return;

        // 3. Aplicar cambios
        Client actual = SessionManager.getInstance().getClienteActual();
        
        // Opción A: Si tu clase Client usa Setters
        actual.setUserName(nuevoNombre);
        actual.setPhoneNumber(nuevoTelefono);
        actual.setEmail(nuevoCorreo);
        
        // Opción B: Si tu clase Client es inmutable y requiere crear un nuevo objeto (como tu Admin)
        // Client clienteActualizado = new Client(actual.getDocumentNumber(), actual.getPassword(), nuevoNombre, nuevoCorreo, nuevoTelefono, actual.isEmpresarial());

        // 4. Actualizar en tu base de datos o fachada (Asegúrate de tener un método actualizarCliente en SistemaController)
        // boolean exito = SistemaController.getInstance().actualizarCliente(actual.getDocumentNumber(), actual);
        boolean exito = true; // Reemplaza esto con el llamado real a tu sistema

        if (exito) {
            mostrarAlerta(AlertType.INFORMATION, "Éxito", "El perfil se actualizó correctamente.");
            try {
                App.setRoot("perfilCliente");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta(AlertType.ERROR, "Error", "Ocurrió un problema al actualizar el perfil.");
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