package co.edu.uptc.viewController;

import java.io.IOException;
import java.util.ResourceBundle;

import co.edu.uptc.Services.SistemaController;
import co.edu.uptc.View.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;

public class LoginClientController {
    

    @FXML private TextField txtPassword;

    @FXML private Hyperlink hypRegister;
    @FXML
    private ComboBox<String> comboIdioma;

    @FXML
    private ResourceBundle resources;
    @FXML
    private TextField txtCedula;

    
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

        boolean acceso = SistemaController.getInstance().loginCliente(id, password);

        if (acceso) {
            try {
                App.setRoot("clientDashboard"); // cambia por tu fxml de destino cuando lo tengas
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta(AlertType.ERROR,
                resources.getString("login.alerta.credenciales.titulo"),
                resources.getString("login.alerta.credenciales.mensaje"));
        }
    }







    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void switchToBack(ActionEvent event) {
        try {
            App.setRoot("mainView");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goRegister(){
        try {
            App.setRoot("registrerClient");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}