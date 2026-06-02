package co.edu.uptc.viewController;

import co.edu.uptc.Model.Admin;
import co.edu.uptc.Model.Client;
import co.edu.uptc.Services.SessionManager;
import co.edu.uptc.Services.SistemaController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import java.util.Optional;
import java.util.ResourceBundle;

public class CambiarPasswordController {

    @FXML private PasswordField txtActual;
    @FXML private PasswordField txtNueva;
    @FXML private PasswordField txtConfirmar;

    @FXML private ResourceBundle resources;

    @FXML
    public void initialize() { }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    @FXML
    private void guardarPassword() {
        String actual    = txtActual.getText();
        String nueva     = txtNueva.getText();
        String confirmar = txtConfirmar.getText();

        // 1. Campos vacios
        if (actual.isEmpty() || nueva.isEmpty() || confirmar.isEmpty()) {
            mostrarAlerta(AlertType.WARNING,
                resources.getString("cambiarPassword.alerta.camposVacios.titulo"),
                resources.getString("cambiarPassword.alerta.camposVacios.mensaje"));
            return;
        }

        // 2. Nueva y confirmar no coinciden
        if (!nueva.equals(confirmar)) {
            mostrarAlerta(AlertType.ERROR,
                resources.getString("cambiarPassword.alerta.noCoinciden.titulo"),
                resources.getString("cambiarPassword.alerta.noCoinciden.mensaje"));
            return;
        }

        // 3. Formato de nueva contraseña
        if (!SistemaController.getInstance().isPasswordValida(nueva)) {
            mostrarAlerta(AlertType.ERROR,
                resources.getString("cambiarPassword.alerta.formatoInvalido.titulo"),
                resources.getString("cambiarPassword.alerta.formatoInvalido.mensaje"));
            return;
        }

        // 4. Verificar contraseña actual según quien tenga sesion
        boolean actualCorrecta = verificarPasswordActual(actual);
        if (!actualCorrecta) {
            mostrarAlerta(AlertType.ERROR,
                resources.getString("cambiarPassword.alerta.actualIncorrecta.titulo"),
                resources.getString("cambiarPassword.alerta.actualIncorrecta.mensaje"));
            return;
        }

        // 5. Confirmacion antes de guardar
        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle(resources.getString("cambiarPassword.confirmacion.titulo"));
        confirmacion.setHeaderText(null);
        confirmacion.setContentText(resources.getString("cambiarPassword.confirmacion.mensaje"));

        ButtonType btnSi = new ButtonType(
            resources.getString("cambiarPassword.confirmacion.aceptar"));
        ButtonType btnNo = new ButtonType(
            resources.getString("cambiarPassword.confirmacion.cancelar"));
        confirmacion.getButtonTypes().setAll(btnSi, btnNo);

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isEmpty() || resultado.get() != btnSi) return;

        // 6. Aplicar el cambio
        boolean exito = aplicarCambioPassword(nueva);

        if (exito) {
            mostrarAlerta(AlertType.INFORMATION,
                resources.getString("cambiarPassword.alerta.exito.titulo"),
                resources.getString("cambiarPassword.alerta.exito.mensaje"));
            cerrarVentana();
        } else {
            mostrarAlerta(AlertType.ERROR,
                resources.getString("cambiarPassword.alerta.error.titulo"),
                resources.getString("cambiarPassword.alerta.error.mensaje"));
        }
    }

    // Detecta si hay sesion admin o cliente y verifica la contraseña actual
    private boolean verificarPasswordActual(String rawActual) {
        if (SessionManager.getInstance().haySesionAdmin()) {
            Admin admin = SessionManager.getInstance().getAdminActual();
            return SistemaController.getInstance()
                                    .loginAdmin(String.valueOf(admin.getId()), rawActual);
        } else if (SessionManager.getInstance().haySesionCliente()) {
            Client cliente = SessionManager.getInstance().getClienteActual();
            return SistemaController.getInstance()
                                    .loginCliente(String.valueOf(cliente.getId()), rawActual);
        }
        return false;
    }

    // Encripta y guarda la nueva contraseña según quien tenga sesion
    private boolean aplicarCambioPassword(String rawNueva) {
        String hashNueva = SistemaController.getInstance().encriptarPassword(rawNueva);

        if (SessionManager.getInstance().haySesionAdmin()) {
            Admin admin = SessionManager.getInstance().getAdminActual();
            Admin actualizado = new Admin(
                admin.getUserName(),
                admin.getId(),
                admin.getEmail(),
                hashNueva,
                admin.getPhoneNumber()
            );
            boolean exito = SistemaController.getInstance()
                                             .actualizarAdmin(admin.getId(), actualizado);
            if (exito) SessionManager.getInstance().iniciarSesionAdmin(actualizado);
            return exito;

        } else if (SessionManager.getInstance().haySesionCliente()) {
            Client cliente = SessionManager.getInstance().getClienteActual();
            Client actualizado = new Client(
                cliente.getUserName(),
                cliente.getId(),
                hashNueva,
                cliente.getPhoneNumber(),
                cliente.getEmail(),
                cliente.isEmpresarial()
            );
            boolean exito = SistemaController.getInstance()
                                             .actualizarCliente(cliente.getId(), actualizado);
            if (exito) SessionManager.getInstance().iniciarSesionCliente(actualizado);
            return exito;
        }
        return false;
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtActual.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}