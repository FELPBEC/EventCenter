package co.edu.uptc.viewController;

import java.time.LocalDateTime;

import co.edu.uptc.Model.Booking;
import co.edu.uptc.Model.Client;
import co.edu.uptc.Model.Salon;
import co.edu.uptc.Services.SessionManager;
import co.edu.uptc.Services.SistemaController;
import co.edu.uptc.Util.DateConvertor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.util.List;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;;
public class ClientBokkingsController {
    @FXML private TableView<Booking> tbBookings; // Cambiado el ID
    @FXML private TableColumn<Booking, String> dateBooking;
    @FXML private TextField txtDateSelectedBooking, txtReservationDate, txtSalonReservado, txtPrice;
    @FXML private Button btnCancel;

    @FXML private Button btnToggleMenu;
    @FXML private VBox sideMenuContainer;
    private Salon salonActual; // El salón del booking seleccionado
    private int indiceActual = 0; // Índice de la imagen que se está viendo
    private ObservableList<Booking> bookingList = FXCollections.observableArrayList();
    @FXML
    private ImageView imgSalon;

    @FXML
    public void initialize() {
        Client client=SessionManager.getInstance().getClienteActual();
        txtDateSelectedBooking.setEditable(false);
        txtReservationDate.setEditable(false);
        txtSalonReservado.setEditable(false);
        txtPrice.setEditable(false);
        // 1. Configurar columnas
        dateBooking.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        bookingList.setAll(SistemaController.getInstance().sendBookingByClient(client.getId()));
        tbBookings.setItems(bookingList);

        // 2. Listener de selección
        tbBookings.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showBookingDetails(newVal);
                this.salonActual = newVal.getSalon();
                this.indiceActual = 0; // Resetear índice al cambiar de reserva
                cargarImagenDelSalon();
            }
        });
    }
    @FXML
    private void toggleMenu() {
        if (sideMenuContainer != null) {
            boolean visible = sideMenuContainer.isVisible();
            sideMenuContainer.setVisible(!visible);
            sideMenuContainer.setManaged(!visible);
        }
    }
    @FXML
private void avanzarImagen() {
    if (salonActual == null) return;
    // Accedes directamente a la lista desde tu objeto salon
    List<String> imagenes = salonActual.getImagePaths(); 
    
    if (imagenes != null && !imagenes.isEmpty()) {
        // El cálculo del índice sigue siendo igual
        indiceActual = (indiceActual + 1) % imagenes.size();
        mostrarImagen(imagenes);
    }
}

@FXML
private void retrocederImagen() {
    if (salonActual == null) return;
       List<String> imagenes = salonActual.getImagePaths(); 
    
    if (imagenes != null && !imagenes.isEmpty()) {
        // La fórmula asegura que el resultado sea siempre positivo
        indiceActual = (indiceActual - 1 + imagenes.size()) % imagenes.size();
        mostrarImagen(imagenes);
    }
}

private void mostrarImagen(List<String> imagenes) {
    String ruta = imagenes.get(indiceActual);
    // Usamos el constructor de Image con la ruta
    Image img = new Image(getClass().getResource(ruta).toExternalForm());
    imgSalon.setImage(img);
}
    private void cargarImagenDelSalon() {
    if (salonActual != null) {
        List<String> imagenes = salonActual.getImagePaths();
        if (imagenes != null && !imagenes.isEmpty()) {
            mostrarImagen(imagenes);
        }
    }
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
        try {
        // Intentamos borrar
        SistemaController.getInstance().cancelBooking(selected.getId());
        
        // Si no lanzó excepción, asumimos éxito
        bookingList.remove(selected);
        limpiarCampos();
    } catch (Exception e) {
        // Si hubo un error, mostramos el mensaje
        Alert alert = new Alert(Alert.AlertType.ERROR, "Error al eliminar la reserva: " + e.getMessage());
        alert.showAndWait();
    }
        
        bookingList.remove(selected);
        // Limpiar campos de texto...
    } else {
        Alert alert = new Alert(Alert.AlertType.WARNING, 
            "No es posible cancelar: la reserva comienza en menos de 24 horas.");
        alert.showAndWait();
    }
    }
    private void limpiarCampos() {
    txtDateSelectedBooking.clear();
    txtReservationDate.clear();
    txtSalonReservado.clear();
    txtPrice.clear();
    imgSalon.setImage(null); // ¡Importante! Limpiar la imagen también
    }
}
