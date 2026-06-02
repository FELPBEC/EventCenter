package co.edu.uptc.viewController;

import co.edu.uptc.Model.Salon;
import co.edu.uptc.Services.SalonServices;
import co.edu.uptc.View.App;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GestionSalonesAdminController implements Initializable {

    @FXML private TextField txtBuscarSalones;
    @FXML private TableView<Salon> salonesTable;
    @FXML private TableColumn<Salon, Integer> colId;
    @FXML private TableColumn<Salon, String> colNombresSalon;
    @FXML private TableColumn<Salon, Integer> colCapacidad;
    @FXML private TableColumn<Salon, Double> colPrecio;
    @FXML private TableColumn<Salon, Integer> colReservaciones;
    @FXML private Button btnToggleMenu;
    @FXML private VBox sideMenuContainer;

    private SalonServices salonServices;
    private FilteredList<Salon> filteredSalones;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            salonServices = new SalonServices();
            ObservableList<Salon> salonesData = FXCollections.observableArrayList(salonServices.getListSalones());
            filteredSalones = new FilteredList<>(salonesData, salon -> true);

            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colNombresSalon.setCellValueFactory(new PropertyValueFactory<>("salonName"));
            colCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacity"));
            colPrecio.setCellValueFactory(new PropertyValueFactory<>("priceByHour"));
            colReservaciones.setCellValueFactory(new PropertyValueFactory<>("numberOfReservations"));

            if (salonesTable != null) {
                salonesTable.setItems(filteredSalones);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void buscarSalon() {
        if (filteredSalones == null) {
            return;
        }

        String query = txtBuscarSalones.getText();
        if (query == null || query.isBlank()) {
            filteredSalones.setPredicate(salon -> true);
            return;
        }

        String lowerQuery = query.trim().toLowerCase();
        filteredSalones.setPredicate(salon ->
            String.valueOf(salon.getId()).contains(lowerQuery)
                || salon.getSalonName().toLowerCase().contains(lowerQuery)
        );
    }
     @FXML
    private void openRegisterSalon() {
        try {
            App.setRoot("registerSalon");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openUpdateSalon() {
        Salon seleccionado = salonesTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alerta.setTitle("Selecciona un salón");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor selecciona un salón de la tabla antes de actualizar.");
            alerta.showAndWait();
            return;
        }

        try {
            App.setRoot("actualizarSalon");
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteSalon() {
        Salon seleccionado = salonesTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alerta.setTitle("Selecciona un salón");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor selecciona un salón de la tabla antes de eliminar.");
            alerta.showAndWait();
            return;
        }

        javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar salón");
        confirm.setHeaderText("Confirmar eliminación");
        confirm.setContentText("¿Deseas eliminar el salón " + seleccionado.getSalonName() + "?");
        java.util.Optional<javafx.scene.control.ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != javafx.scene.control.ButtonType.OK) {
            return;
        }

        salonServices.deleteSalon(seleccionado.getId());
        refreshTable();

        javafx.scene.control.Alert info = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        info.setTitle("Salón eliminado");
        info.setHeaderText(null);
        info.setContentText("El salón ha sido eliminado correctamente.");
        info.showAndWait();
    }

    private void refreshTable() {
        if (filteredSalones == null || salonServices == null) {
            return;
        }

        ObservableList<Salon> salonesData = FXCollections.observableArrayList(salonServices.getListSalones());
        FilteredList<Salon> nuevaLista = new FilteredList<>(salonesData, filteredSalones.getPredicate());
        filteredSalones = nuevaLista;
        salonesTable.setItems(filteredSalones);
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
}

