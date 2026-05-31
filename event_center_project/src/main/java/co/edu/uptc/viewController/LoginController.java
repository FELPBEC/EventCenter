package co.edu.uptc.viewController;

import co.edu.uptc.Services.SistemaController;
import co.edu.uptc.View.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController {

    @FXML private TextField txtCedula;
    @FXML private PasswordField txtPassword;

    @FXML private ResourceBundle resources;

    // -------------------------------------------------------------------------
    // Inicialización
    // -------------------------------------------------------------------------
    @FXML
    public void initialize() {
        // Por ahora vacío, listo para crecer
    }

    // -------------------------------------------------------------------------
    // Acción: Iniciar Sesión
    // -------------------------------------------------------------------------
    @FXML
    private void switchToLogin(ActionEvent event) {
        String cedulaTexto = txtCedula.getText().trim();
        String password    = txtPassword.getText().trim();

        if (cedulaTexto.isEmpty() || password.isEmpty()) {
            mostrarAlerta(AlertType.WARNING,
                resources.getString("login.alerta.camposVacios.titulo"),
                resources.getString("login.alerta.camposVacios.mensaje"));
            return;
        }

        int id;
        try {
            id = Integer.parseInt(cedulaTexto);
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.ERROR,
                resources.getString("login.alerta.idInvalida.titulo"),
                resources.getString("login.alerta.idInvalida.mensaje"));
            return;
        }

        boolean acceso = SistemaController.getInstance().loginAdmin(id, password);

        if (acceso) {
            try {
                App.setRoot("adminDashboard"); // cambia por tu fxml de destino cuando lo tengas
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta(AlertType.ERROR,
                resources.getString("login.alerta.credenciales.titulo"),
                resources.getString("login.alerta.credenciales.mensaje"));
        }
    }

    // -------------------------------------------------------------------------
    // Acción: Atrás
    // -------------------------------------------------------------------------
    @FXML
    private void switchToBack(ActionEvent event) {
        try {
            App.setRoot("mainView");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // Utilidad interna
    // -------------------------------------------------------------------------
    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}