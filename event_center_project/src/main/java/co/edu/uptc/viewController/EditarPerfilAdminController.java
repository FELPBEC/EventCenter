package co.edu.uptc.viewController;

import co.edu.uptc.Model.Admin;
import co.edu.uptc.Services.SessionManager;
import co.edu.uptc.Services.SistemaController;
import co.edu.uptc.View.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.ResourceBundle;

public class EditarPerfilAdminController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private Button    btnToggleMenu;
    @FXML private VBox      sideMenuContainer;

    @FXML private ResourceBundle resources;

    @FXML
    public void initialize() {
        precargarDatos();
    }

    private void precargarDatos() {
        if (!SessionManager.getInstance().haySesionAdmin()) return;
        Admin admin = SessionManager.getInstance().getAdminActual();
        txtNombre.setText(admin.getUserName());
        txtTelefono.setText(admin.getPhoneNumber());
        txtCorreo.setText(admin.getEmail());
    }

    @FXML
    private void toggleMenu() {
        boolean visible = sideMenuContainer.isVisible();
        sideMenuContainer.setVisible(!visible);
        sideMenuContainer.setManaged(!visible);
    }

    @FXML
    private void cancelar() {
        try {
            App.setRoot("perfilAdmin");
        } catch (IOException e) {
            mostrarAlerta(AlertType.ERROR,
                resources.getString("perfil.alerta.errorNavegacion.titulo"),
                resources.getString("perfil.alerta.errorNavegacion.mensaje"));
            e.printStackTrace();
        }
    }

    @FXML
    private void guardarCambios() {
        String nombre   = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo   = txtCorreo.getText().trim();

        if (nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
            mostrarAlerta(AlertType.WARNING,
                resources.getString("editarPerfil.alerta.camposVacios.titulo"),
                resources.getString("editarPerfil.alerta.camposVacios.mensaje"));
            return;
        }

        Admin adminActual = SessionManager.getInstance().getAdminActual();
        Admin adminActualizado = new Admin(
            nombre,
            adminActual.getId(),
            correo,
            adminActual.getPassword(),
            telefono
        );

        boolean exito = SistemaController.getInstance()
                                         .actualizarAdmin(adminActual.getId(), adminActualizado);

        if (exito) {
            // Actualizamos la sesion con los nuevos datos
            SessionManager.getInstance().iniciarSesionAdmin(adminActualizado);
            mostrarAlerta(AlertType.INFORMATION,
                resources.getString("editarPerfil.alerta.exito.titulo"),
                resources.getString("editarPerfil.alerta.exito.mensaje"));
            try {
                App.setRoot("perfilAdmin");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta(AlertType.ERROR,
                resources.getString("editarPerfil.alerta.error.titulo"),
                resources.getString("editarPerfil.alerta.error.mensaje"));
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
