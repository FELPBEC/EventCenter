package co.edu.uptc.viewController;

import java.io.IOException;
import co.edu.uptc.Model.Client;
import co.edu.uptc.Services.SessionManager;
import co.edu.uptc.Services.SistemaController;
import co.edu.uptc.View.App;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;

public class LoginClientController {

    // Estos IDs deben coincidir exactamente con tu archivo FXML
    @FXML private TextField txtCedula;
    @FXML private PasswordField txtPassword;

    private SistemaController sistema = SistemaController.getInstance();
    @FXML
    private Hyperlink hypRegister;

    @FXML
    public void switchToLogin() {
        String cedula = txtCedula.getText() != null ? txtCedula.getText().trim() : "";
        String password = txtPassword.getText() != null ? txtPassword.getText() : "";

        // 1. Validación de campos vacíos
        if (cedula.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Campos Vacíos", "Por favor, complete todos los campos.");
            return;
        }

        try {
            // 2. La fachada (SistemaController) ya valida el formato y verifica BCrypt internamente
            if (sistema.loginCliente(cedula, password)) {
                
                // 3. Obtenemos al cliente usando su cédula (parseada a entero)
                Client cliente = sistema.obtenerCliente(Integer.parseInt(cedula));
        
                // 4. Iniciamos la sesión en el Singleton SessionManager de tu proyecto
                SessionManager.getInstance().iniciarSesionCliente(cliente);
                
                System.out.println("Login exitoso para: " + cedula);
                
                // 5. Navegación nativa de tu proyecto hacia la vista interior/principal
                // Nota: Asegúrate de cambiar "mainView" por el nombre FXML de la pantalla a la que deba ir el cliente
                App.setRoot("mainView"); 
                
            } else {
                // FALLO: Credenciales incorrectas
                mostrarAlerta("Acceso denegado", "Cédula o contraseña incorrectas.");
            }
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "La cédula debe ser un número entero válido.");
        } catch (Exception e) {
            // Captura cualquier otro error inesperado de lectura de archivos JSON, etc.
            mostrarAlerta("Error crítico", "Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    @FXML
    public void goRegister() throws IOException {
        App.setRoot("registerClient");
    }

    @FXML
    public void switchToBack() throws IOException {
        App.setRoot("mainView");
    }

    // Tu método original de alertas de 2 parámetros
    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}