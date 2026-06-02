package co.edu.uptc.viewController;

import co.edu.uptc.Model.Admin;
import co.edu.uptc.Services.AdminServices;
import co.edu.uptc.Services.SessionManager;
import co.edu.uptc.View.App;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GestionAdminsController implements Initializable {

    @FXML private TextField txtBuscarAdmin;
    @FXML private TableView<Admin> adminsTable;
    @FXML private TableColumn<Admin, Integer> colId;
    @FXML private TableColumn<Admin, String> colNombre;
    @FXML private TableColumn<Admin, String> colCorreo;
    @FXML private TableColumn<Admin, String> colTelefono;
    @FXML private Button btnToggleMenu;
    @FXML private Button btnAgregarAdmin;
    @FXML private VBox sideMenuContainer;
    @FXML private ResourceBundle resources;

    private AdminServices adminServices;
    private FilteredList<Admin> filteredAdmins;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        try {
            adminServices = new AdminServices();
            ObservableList<Admin> adminData = FXCollections.observableArrayList(adminServices.getListAdmins());
            filteredAdmins = new FilteredList<>(adminData, admin -> true);

            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colNombre.setCellValueFactory(new PropertyValueFactory<>("userName"));
            colCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
            colTelefono.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

            if (adminsTable != null) {
                adminsTable.setItems(filteredAdmins);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR,
                "Error cargando administradores",
                e.getMessage());
        }
    }

    @FXML
    private void buscarAdmin() {
        if (filteredAdmins == null) {
            return;
        }

        String query = txtBuscarAdmin.getText();
        if (query == null || query.isBlank()) {
            filteredAdmins.setPredicate(admin -> true);
            return;
        }

        String lowerQuery = query.trim().toLowerCase();
        filteredAdmins.setPredicate(admin ->
            String.valueOf(admin.getId()).contains(lowerQuery)
                || admin.getUserName().toLowerCase().contains(lowerQuery)
                || admin.getEmail().toLowerCase().contains(lowerQuery)
                || admin.getPhoneNumber().toLowerCase().contains(lowerQuery)
        );
    }

    @FXML
    private void mostrarCrearAdmin() {
        try {
            App.setRoot("registerAdmin");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR,
                "Error al abrir crear administrador",
                "No se pudo abrir el formulario de creación: " + e.getMessage());
        }
    }

    @FXML
    private void editarAdmin() {
        Admin seleccionado = adminsTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING,
                "Selecciona un administrador",
                "Debes seleccionar un administrador para editar.");
            return;
        }

        try {
            SessionManager.getInstance().iniciarSesionAdmin(seleccionado);
            App.setRoot("editarPerfilAdmin");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR,
                "Error al abrir la pantalla",
                "No se pudo abrir la edición de perfil: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarAdmin() {
        Admin seleccionado = adminsTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING,
                "Selecciona un administrador",
                "Debes seleccionar un administrador en la tabla para eliminarlo.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar administrador");
        confirmacion.setHeaderText("Confirmar eliminación");
        confirmacion.setContentText("¿Deseas eliminar al administrador " + seleccionado.getUserName() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isEmpty() || resultado.get() != ButtonType.OK) {
            return;
        }

        adminServices.fireAdmin(seleccionado.getId());
        refreshTable();
        mostrarAlerta(Alert.AlertType.INFORMATION,
            "Administrador eliminado",
            "El administrador ha sido eliminado correctamente.");
    }

    @FXML
    private void toggleMenu() {
        if (sideMenuContainer == null) {
            return;
        }
        boolean visible = sideMenuContainer.isVisible();
        sideMenuContainer.setVisible(!visible);
        sideMenuContainer.setManaged(!visible);
    }

    private void refreshTable() {
        if (filteredAdmins == null) {
            return;
        }
        ObservableList<Admin> adminData = FXCollections.observableArrayList(adminServices.getListAdmins());
        filteredAdmins = new FilteredList<>(adminData, filteredAdmins.getPredicate());
        adminsTable.setItems(filteredAdmins);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
