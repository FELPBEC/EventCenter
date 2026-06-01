package co.edu.uptc.viewController;

import java.time.LocalDateTime;

import co.edu.uptc.Model.Booking;
import co.edu.uptc.Model.Client;
import co.edu.uptc.Services.SessionManager;
import co.edu.uptc.Services.SistemaController;
import co.edu.uptc.Util.DateConvertor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class clientBokkingsController {
    @FXML private TableView<Booking> tbBookings; // Cambiado el ID
    @FXML private TableColumn<Booking, String> dateBooking;
    @FXML private TextField txtDateSelectedBooking, txtReservationDate, txtSalonReservado, txtPrice;
    @FXML private Button btnCancel;

    private ObservableList<Booking> bookingList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Client client=SessionManager.getInstance().getClienteActual();
        // 1. Configurar columnas
        dateBooking.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        bookingList.setAll(SistemaController.getInstance().sendBookingByClient(client.getId()));
        tbBookings.setItems(bookingList);

        // 2. Listener de selección
        tbBookings.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showBookingDetails(newVal);
            }
        });
    }

    private void showBookingDetails(Booking b) {
        txtDateSelectedBooking.setText(b.getStartDate());
        txtReservationDate.setText(String.valueOf(b.getAmountOfHours()));
        txtSalonReservado.setText(b.getSalon().getSalonName()); 
        txtPrice.setText(String.valueOf(b.getPrice()));
    }
    @FXML
private void cancelBooking() {
    Booking selected = tbBookings.getSelectionModel().getSelectedItem();
    if (selected == null) return;

    // 1. Usamos tu convertidor para obtener la fecha de inicio
    DateConvertor converter = new DateConvertor();
    LocalDateTime fechaReserva = converter.StringToLocalDateTime(selected.getStartDate());
    LocalDateTime ahora = LocalDateTime.now();

    // 2. Validar diferencia (al menos 24 horas = 1 día)
    // Usamos plusDays(1) para verificar si "ahora" es antes que "fechaReserva - 24h"
    if (ahora.isBefore(fechaReserva.minusDays(1))) {
        // Proceder con la cancelación en el servicio
        // systemController.cancelBooking(selected.getId());
        bookingList.remove(selected);
        // Limpiar campos de texto...
    } else {
        Alert alert = new Alert(Alert.AlertType.WARNING, 
            "No es posible cancelar: la reserva comienza en menos de 24 horas.");
        alert.showAndWait();
    }
    }
}
