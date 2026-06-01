package co.edu.uptc.viewController;

import co.edu.uptc.Model.Admin;
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
        String nuevoNombre   = txtNombre.getText().trim();
        String nuevoTelefono = txtTelefono.getText().trim();
        String nuevoCorreo   = txtCorreo.getText().trim();

        // 1. Validar campos vacios
        if (nuevoNombre.isEmpty() || nuevoTelefono.isEmpty() || nuevoCorreo.isEmpty()) {
            mostrarAlerta(AlertType.WARNING,
                resources.getString("editarPerfil.alerta.camposVacios.titulo"),
                resources.getString("editarPerfil.alerta.camposVacios.mensaje"));
            return;
        }

        Admin actual = SessionManager.getInstance().getAdminActual();

        // 2. Detectar qué campos cambiaron para mostrarlos en la confirmación
        StringBuilder cambios = new StringBuilder();

        if (!nuevoNombre.equals(actual.getUserName())) {
            cambios.append(resources.getString("perfil.nombre"))
                   .append(" ")
                   .append(actual.getUserName())
                   .append(" -> ")
                   .append(nuevoNombre)
                   .append("\n");
        }
        if (!nuevoTelefono.equals(actual.getPhoneNumber())) {
            cambios.append(resources.getString("perfil.telefono"))
                   .append(" ")
                   .append(actual.getPhoneNumber())
                   .append(" -> ")
                   .append(nuevoTelefono)
                   .append("\n");
        }
        if (!nuevoCorreo.equals(actual.getEmail())) {
            cambios.append(resources.getString("perfil.correo"))
                   .append(" ")
                   .append(actual.getEmail())
                   .append(" -> ")
                   .append(nuevoCorreo)
                   .append("\n");
        }

        // 3. Si no hubo ningún cambio, avisar y no hacer nada
        if (cambios.length() == 0) {
            mostrarAlerta(AlertType.INFORMATION,
                resources.getString("editarPerfil.alerta.sinCambios.titulo"),
                resources.getString("editarPerfil.alerta.sinCambios.mensaje"));
            return;
        }

        // 4. Mostrar confirmación con el detalle de los cambios
        String mensajeConfirmacion = resources.getString("editarPerfil.confirmacion.mensaje")
                                     + "\n\n" + cambios.toString();

        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle(resources.getString("editarPerfil.confirmacion.titulo"));
        confirmacion.setHeaderText(resources.getString("editarPerfil.confirmacion.header"));
        confirmacion.setContentText(mensajeConfirmacion);

        ButtonType btnAceptar = new ButtonType(
            resources.getString("editarPerfil.confirmacion.aceptar")
        );
        ButtonType btnCancelar = new ButtonType(
            resources.getString("editarPerfil.confirmacion.cancelar")
        );
        confirmacion.getButtonTypes().setAll(btnAceptar, btnCancelar);

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isEmpty() || resultado.get() != btnAceptar) return;

        // 5. Aplicar cambios
        Admin adminActualizado = new Admin(
            nuevoNombre,
            actual.getId(),
            nuevoCorreo,
            actual.getPassword(),
            nuevoTelefono
        );

        boolean exito = SistemaController.getInstance()
                                         .actualizarAdmin(actual.getId(), adminActualizado);

        if (exito) {
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