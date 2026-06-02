package co.edu.uptc.viewController;

import co.edu.uptc.Model.Admin;
import co.edu.uptc.Services.AdminServices;
import co.edu.uptc.View.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AnadirAdminController {

    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtPassword;

    private final AdminServices adminServices = new AdminServices();

    @FXML
    private void guardarAdmin() {
        String idText = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String password = txtPassword.getText().trim();

        if (idText.isEmpty() || nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty() || password.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING,
                "Datos incompletos",
                "Todos los campos son obligatorios.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING,
                "ID inválido",
                "La cédula debe ser un número válido.");
            return;
        }

        if (adminServices.searchAdminById(id)) {
            mostrarAlerta(Alert.AlertType.WARNING,
                "Administrador existente",
                "Ya existe un administrador con esa cédula.");
            return;
        }

        Admin nuevoAdmin = new Admin(nombre, id, correo, password, telefono);
        adminServices.saveNewAdmin(nuevoAdmin);

        mostrarAlerta(Alert.AlertType.INFORMATION,
            "Administrador creado",
            "El administrador se ha creado correctamente.");

        volverAGestionAdmins();
    }

    @FXML
    private void cancelar() {
        volverAGestionAdmins();
    }

    private void volverAGestionAdmins() {
        try {
            App.setRoot("gestionAdmins");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR,
                "Error de navegación",
                "No se pudo regresar a la gestión de administradores.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo, mensaje, ButtonType.OK);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
