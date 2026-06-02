package co.edu.uptc.viewController;

import co.edu.uptc.Model.Salon;
import co.edu.uptc.Services.SalonServices;
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
    private void toggleMenu() {
        if (sideMenuContainer == null) {
            return;
        }
        boolean visible = sideMenuContainer.isVisible();
        sideMenuContainer.setVisible(!visible);
        sideMenuContainer.setManaged(!visible);
    }
}

