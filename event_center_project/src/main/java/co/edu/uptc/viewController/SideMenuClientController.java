package co.edu.uptc.viewController;

import co.edu.uptc.Services.SessionManager;
import co.edu.uptc.View.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

public class SideMenuClientController {

    @FXML private Button btnEditarPerfil;
    @FXML private Button btnMisReservas;
    @FXML private Button btnCerrarSesion;

    @FXML private ResourceBundle resources;

    @FXML
    public void initialize() { }

    @FXML
    private void irAPerfil() {
        try {
            App.setRoot("perfilCliente");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irAReservas()throws IOException {
        App.setRoot("clientBooking");
    }
    
    @FXML
    private void goNewBooking()throws IOException {
        App.setRoot("newBooking");
    }
    @FXML
    private void cerrarSesion() {
        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle(resources.getString("menu.cerrarSesion.confirmacion.titulo"));
        confirmacion.setHeaderText(null);

        String nombre = SessionManager.getInstance().haySesionCliente()
            ? SessionManager.getInstance().getClienteActual().getUserName()
            : "";

        confirmacion.setContentText(
            resources.getString("menu.cerrarSesion.confirmacion.mensaje").replace("{0}", nombre)
        );

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
                e.printStackTrace();
            }
        }
    }

}