package co.edu.uptc.viewController;

import co.edu.uptc.Services.SistemaController;
import co.edu.uptc.View.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.ResourceBundle;

public class LoginController {

    @FXML private TextField     txtCedula;
    @FXML private PasswordField txtPassword;
    @FXML private ResourceBundle resources;

    @FXML
    public void initialize() { }

    @FXML
    private void switchToLogin() {
        String cedula   = txtCedula.getText().trim();
        String password = txtPassword.getText();

        // 1. Campos vacios
        if (cedula.isEmpty() && password.isEmpty()) {
            mostrarAlerta(AlertType.WARNING,
                resources.getString("login.alerta.camposVacios.titulo"),
                resources.getString("login.alerta.camposVacios.mensaje"));
            return;
        }

        // 2. Solo cedula vacia
        if (cedula.isEmpty()) {
            mostrarAlerta(AlertType.WARNING,
                resources.getString("login.alerta.cedulaVacia.titulo"),
                resources.getString("login.alerta.cedulaVacia.mensaje"));
            return;
        }

        // 3. Solo contrasena vacia
        if (password.isEmpty()) {
            mostrarAlerta(AlertType.WARNING,
                resources.getString("login.alerta.passwordVacia.titulo"),
                resources.getString("login.alerta.passwordVacia.mensaje"));
            return;
        }

        // 4. Cedula con caracteres no numericos
        if (!cedula.matches("\\d+")) {
            mostrarAlerta(AlertType.ERROR,
                resources.getString("login.alerta.cedulaNoNumerica.titulo"),
                resources.getString("login.alerta.cedulaNoNumerica.mensaje"));
            return;
        }

        // 5. Longitud de cedula colombiana (3 a 10 digitos)
        if (!SistemaController.getInstance().isCedulaValida(cedula)) {
            mostrarAlerta(AlertType.ERROR,
                resources.getString("login.alerta.idInvalida.titulo"),
                resources.getString("login.alerta.idInvalida.mensaje"));
            return;
        }

        // 6. Verificacion de credenciales con BCrypt
        boolean acceso;
        try {
            acceso = SistemaController.getInstance().loginAdmin(cedula, password);
        } catch (Exception e) {
            // Error inesperado en la verificacion (hash malformado en JSON, etc.)
            mostrarAlerta(AlertType.ERROR,
                resources.getString("login.alerta.errorSistema.titulo"),
                resources.getString("login.alerta.errorSistema.mensaje"));
            e.printStackTrace();
            return;
        }

        if (acceso) {
            try {
                App.setRoot("MainView");
            } catch (IOException e) {
                mostrarAlerta(AlertType.ERROR,
                    resources.getString("login.alerta.errorNavegacion.titulo"),
                    resources.getString("login.alerta.errorNavegacion.mensaje"));
                e.printStackTrace();
            }
        } else {
            mostrarAlerta(AlertType.ERROR,
                resources.getString("login.alerta.credenciales.titulo"),
                resources.getString("login.alerta.credenciales.mensaje"));
        }
    }

    @FXML
    private void switchToBack( ) {
        try {
            App.setRoot("mainView");
        } catch (IOException e) {
            mostrarAlerta(AlertType.ERROR,
                resources.getString("login.alerta.errorNavegacion.titulo"),
                resources.getString("login.alerta.errorNavegacion.mensaje"));
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