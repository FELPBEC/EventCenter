package co.edu.uptc.viewController;

import co.edu.uptc.Services.SessionManager;
import co.edu.uptc.View.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

public class SideMenuController {

    @FXML private Button    btnEditarPerfil;
    @FXML private Button    btnClientes;
    @FXML private Button    btnSalones;
    @FXML private Button    btnAdmins;
    @FXML private Button    btnReportes;
    @FXML private Button    btnCerrarSesion;
    @FXML private ImageView imgAvatar;

    @FXML private ResourceBundle resources;

    @FXML
    public void initialize() {
        if (SessionManager.getInstance().haySesionAdmin()) {
            String nombre = SessionManager.getInstance()
                                          .getAdminActual()
                                          .getUserName();
            btnEditarPerfil.setText(nombre);
        }
    }

    @FXML
    private void irAEditarPerfil(ActionEvent event) {
        System.out.println("Pasando a Editar Perfil...");
    }

    @FXML
    private void irAClientes(ActionEvent event) {
        System.out.println("Pasando a Gestion de Clientes...");
    }

    @FXML
    private void irASalones(ActionEvent event) {
        System.out.println("Pasando a Gestion de Salones...");
    }

    @FXML
    private void irAAdmins(ActionEvent event) {
        System.out.println("Pasando a Gestion de Admins...");
    }

    @FXML
    private void irAReportes(ActionEvent event) {
        System.out.println("Pasando a Reportes...");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        if (!SessionManager.getInstance().haySesionAdmin()) return;

        String nombre = SessionManager.getInstance().getAdminActual().getUserName();

        // Mensaje con el nombre del admin activo
        String mensaje = resources.getString("menu.cerrarSesion.confirmacion.mensaje")
                         .replace("{0}", nombre);

        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle(resources.getString("menu.cerrarSesion.confirmacion.titulo"));
        confirmacion.setHeaderText(null);
        confirmacion.setContentText(mensaje);

        // Personalizar los botones con texto del i18n
        ButtonType btnAceptar = new ButtonType(
            resources.getString("menu.cerrarSesion.confirmacion.aceptar")
        );
        ButtonType btnCancelar = new ButtonType(
            resources.getString("menu.cerrarSesion.confirmacion.cancelar")
        );
        confirmacion.getButtonTypes().setAll(btnAceptar, btnCancelar);

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == btnAceptar) {
            SessionManager.getInstance().cerrarSesion();
            try {
                App.setRoot("mainView");
            } catch (IOException e) {
                Alert error = new Alert(AlertType.ERROR);
                error.setTitle(resources.getString("perfil.alerta.errorNavegacion.titulo"));
                error.setHeaderText(null);
                error.setContentText(resources.getString("perfil.alerta.errorNavegacion.mensaje"));
                error.showAndWait();
                e.printStackTrace();
            }
        }
    }
}