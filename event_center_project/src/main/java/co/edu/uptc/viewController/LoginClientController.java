package co.edu.uptc.viewController;

import java.io.IOException;

import co.edu.uptc.Model.Client;
import co.edu.uptc.Services.SessionManager;
import co.edu.uptc.Services.SistemaController;
import co.edu.uptc.View.App;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class LoginClientController {

    @FXML private TextField txtCedula;
    @FXML private PasswordField txtPassword;

    private SistemaController sistema = SistemaController.getInstance();

    @FXML
    public void switchToLogin() {
        String cedula = txtCedula.getText();
        String password = txtPassword.getText();

        // 1. Validaciones básicas
        if (cedula.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Error", "Por favor, complete todos los campos.");
            return;
        }

        // 2. Intentar login a través de la Fachada
        // SistemaController.loginCliente se encarga de validar formato, 
        // buscar en el servicio y verificar BCrypt.
        if (sistema.loginCliente(cedula, password)) {
            // 1. Obtenemos al cliente del sistema
            Client cliente = sistema.obtenerCliente(Integer.parseInt(cedula));
    
            // 2. Iniciamos la sesión en el Singleton SessionManager
            SessionManager.getInstance().iniciarSesionCliente(cliente);
            // ÉXITO: Aquí realizarías la navegación a la siguiente vista
            System.out.println("Sesión iniciada para: " + SessionManager.getInstance().getClienteActual().getUserName());
            System.out.println("Login exitoso para: " + cedula);
            // AppNavigation.loadMainView(); // Ejemplo de tu método de navegación
        } else {
            // FALLO: Credenciales incorrectas o usuario no encontrado
            mostrarAlerta("Acceso denegado", "Cédula o contraseña incorrectas.");
        }
    }

    @FXML
    public void goRegister() throws IOException {
        App.setRoot("registerClient");
    }

    @FXML
    public void switchToBack()throws IOException {
        App.setRoot("mainView");
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}