package co.edu.uptc.viewController;

import co.edu.uptc.Model.Admin;
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

public class PerfilAdminController {

    @FXML private Label  lblNombre;
    @FXML private Label  lblCedula;
    @FXML private Label  lblTelefono;
    @FXML private Label  lblCorreo;
    @FXML private Button btnToggleMenu;
    @FXML private VBox   sideMenuContainer;

    @FXML private ResourceBundle resources;

    @FXML
    public void initialize() {
        cargarDatosPerfil();
    }

    private void cargarDatosPerfil() {
        if (!SessionManager.getInstance().haySesionAdmin()) return;
        Admin admin = SessionManager.getInstance().getAdminActual();
        lblNombre.setText(admin.getUserName());
        lblCedula.setText(String.valueOf(admin.getId()));
        lblTelefono.setText(admin.getPhoneNumber());
        lblCorreo.setText(admin.getEmail());
    }

    @FXML
    private void toggleMenu() {
        boolean visible = sideMenuContainer.isVisible();
        sideMenuContainer.setVisible(!visible);
        sideMenuContainer.setManaged(!visible);
    }

    @FXML
    private void cambiarPassword() {
        System.out.println("Pasando a Cambiar Contrasena...");
    }

    @FXML
    private void modificarPerfil() {
        try {
            App.setRoot("editarPerfilAdmin");
        } catch (IOException e) {
            mostrarAlerta(AlertType.ERROR,
                resources.getString("perfil.alerta.errorNavegacion.titulo"),
                resources.getString("perfil.alerta.errorNavegacion.mensaje"));
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