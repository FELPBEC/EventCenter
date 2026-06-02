package co.edu.uptc.viewController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import co.edu.uptc.Util.DateConvertor;
import co.edu.uptc.Model.Booking;
import co.edu.uptc.Model.Client;
import co.edu.uptc.Model.Salon;
import co.edu.uptc.Util.FiltrerService;
import co.edu.uptc.Services.SessionManager;
import co.edu.uptc.Services.SistemaController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class NewBookingController {

    // Componentes de la interfaz (Filtros de búsqueda)
    @FXML private Button btnToggleMenu;
    @FXML private VBox sideMenuContainer; 
    @FXML private TextField txtAmount;
    @FXML private TextField txtCapicity;
    @FXML private CheckBox chkPresupuesto;
    @FXML private CheckBox chkCapacidad;
    @FXML private DatePicker dtFecha;
    @FXML private ComboBox<String> cmbHora;
    @FXML private ComboBox<String> cmbMin;
    @FXML private ComboBox<Integer> cmbDuracion;
    @FXML private ImageView imgSalon;
    @FXML private Button prevImage;
    @FXML private Button nexImage;

private List<String> listaImagenes; // Lista de rutas (ej: "file:/ruta/a/tu/foto.jpg")
private int indiceImagen = 0;
    // Componentes de la Tabla
    @FXML private TableView<Salon> tbSalones;
    @FXML private TableColumn<Salon, String> colNombreSalon;
    @FXML private TableColumn<Salon, Double> colPrecioSalon;
    @FXML private TableColumn<Salon, Integer> colCapacidadSalon;

    // =========================================================================
    // NUEVO: Componentes del Panel Derecho (Detalles y Reserva)
    // =========================================================================
    @FXML private VBox panelDetalleReserva;
    @FXML private TextField txtNameSalon;
    @FXML private TextField txtCapacidadSalon;
    @FXML private TextField txtCostoHora;
    @FXML private TextField txtPrice; // Campo para el precio total calculado

    // Atributos de control interno
    private final ObservableList<Salon> listaOriginal = FXCollections.observableArrayList();
    private Salon salonSeleccionado;

    /**
     * Método de inicialización de la vista JavaFX.
     */
    @FXML
public void initialize() {
    // 1. Vincular columnas
    colNombreSalon.setCellValueFactory(new PropertyValueFactory<>("salonName"));
    colPrecioSalon.setCellValueFactory(new PropertyValueFactory<>("priceByHour"));
    colCapacidadSalon.setCellValueFactory(new PropertyValueFactory<>("capacity"));

    // 2. Asignar la lista observable a la tabla
    tbSalones.setItems(listaOriginal);
    txtAmount.disableProperty().bind(chkPresupuesto.selectedProperty().not());
    txtCapicity.disableProperty().bind(chkCapacidad.selectedProperty().not());

    // 3. Listener ÚNICO para la selección
    tbSalones.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        if (newVal != null) {
            // Ocultamos la tabla y mostramos el panel
            tbSalones.setVisible(false);
            tbSalones.setManaged(false);
            mostrarDetalles(newVal);
        }
    });

    // 4. Configurar ComboBoxes
    cmbHora.getItems().addAll("07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20");
    cmbMin.getItems().addAll("00", "30");
    cmbDuracion.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8);
    cmbDuracion.setValue(1);

    // Bloquea todas las fechas anteriores a HOY en el calendario
    dtFecha.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
        public void updateItem(java.time.LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            setDisable(empty || date.isBefore(java.time.LocalDate.now()));
        }
    });

    // 5. Estado inicial y carga de datos
    ocultarPanel();
    cargarSalonesIniciales();
}

    private void cargarSalonesIniciales() {
        List<Salon> salonesDisponibles = SistemaController.getInstance().obtenerTodosLosSalones();
        if (salonesDisponibles != null) {
            listaOriginal.setAll(salonesDisponibles);
        }
    }

    private LocalDateTime obtenerFechaInicio() {
    // Cambiamos la lógica: si no hay fecha o no hay hora seleccionada, es inválido
        if (dtFecha.getValue() == null || cmbHora.getValue() == null) {
        return null;
    }
    
    String horaStr = cmbHora.getValue();
    String minStr = (cmbMin.getValue() != null) ? cmbMin.getValue() : "00";
    
    try {
        int hora = Integer.parseInt(horaStr);
        int minuto = Integer.parseInt(minStr);
        LocalDateTime fechaSeleccionada = LocalDateTime.of(dtFecha.getValue(), LocalTime.of(hora, minuto));
        
        // --- NUEVA VALIDACIÓN: Fecha no puede ser anterior a la actual ---
        if (fechaSeleccionada.isBefore(LocalDateTime.now())) {
            return null; // O podrías lanzar una excepción personalizada
        }
        
        return fechaSeleccionada;
    } catch (NumberFormatException e) {
        return null;
    }
}

    @FXML
private void realizarBusqueda() {
    // SOLUCIÓN 1: Validar obligatoriedad de filtros principales antes de ejecutar nada
    if (dtFecha.getValue() == null || cmbHora.getValue() == null || cmbDuracion.getValue() == null) {
        mostrarAlerta("Campos Obligatorios", "Por favor, seleccione una Fecha, Hora y Duración para realizar la búsqueda.");
        return;
    }

    LocalDateTime fechaInicio = obtenerFechaInicio();

    if (fechaInicio == null) {
        // Si obtenemos null, es porque: o está vacío, o la fecha es pasada
        mostrarAlerta("Fecha Inválida", "La fecha y hora deben ser futuras. Por favor, verifique su selección.");
        return;
    }

    int duracion = cmbDuracion.getValue();

    // Instancia limpia para aislar el entorno de ejecución
    FiltrerService filtro = new FiltrerService(fechaInicio, duracion);
    filtro.setFiltrerByPrice(chkPresupuesto.isSelected());
    filtro.setFiltrerByCapacity(chkCapacidad.isSelected());

    double presupuesto = 0;
    int capacidad = 0;
    
    try {
        if (chkPresupuesto.isSelected() && txtAmount != null && !txtAmount.getText().isEmpty()) {
            presupuesto = Double.parseDouble(txtAmount.getText());
        }
        if (chkCapacidad.isSelected() && txtCapicity != null && !txtCapicity.getText().isEmpty()) {
            capacidad = Integer.parseInt(txtCapicity.getText());
        }
    } catch (NumberFormatException e) {
        mostrarAlerta("Error de Formato", "Asegúrese de ingresar valores exclusivamente numéricos en los campos habilitados.");
        return;
    }

    // SOLUCIÓN 3: Al dispararse el método por el botón, se filtrará y refrescará la tabla
    List<Salon> resultados = filtro.sendFiltrerSalonList(presupuesto, capacidad);
    listaOriginal.setAll(resultados);
    
    ocultarPanel();

    if (resultados.isEmpty()) {
        mostrarAlerta("Búsqueda sin Resultados", "No se encontraron salones que coincidan con las condiciones y horarios estipulados.");
    }
}

    // =========================================================================
    // NUEVO: Gestión de Detalles y Lógica de Reserva Directa
    // =========================================================================
    @FXML
private void avanzarImagen() {
    if (listaImagenes != null && !listaImagenes.isEmpty()) {
        indiceImagen = (indiceImagen + 1) % listaImagenes.size();
        actualizarImagen();
    }
}

@FXML
private void retrocederImagen() {
    if (listaImagenes != null && !listaImagenes.isEmpty()) {
        indiceImagen = (indiceImagen - 1 + listaImagenes.size()) % listaImagenes.size();
        actualizarImagen();
    }
}

private void actualizarImagen() {
    if (listaImagenes != null && !listaImagenes.isEmpty()) {
        // Carga la imagen desde la ruta. Si usas recursos locales, 
        // asegúrate de que el formato sea "file:/..." o "getClass().getResource(...)"
        Image image = new Image(listaImagenes.get(indiceImagen));
        imgSalon.setImage(image);
    }
}
    /**
     * Hace visible el panel derecho y carga la información del salón seleccionado.
     */
    private void mostrarDetalles(Salon salon) {
        this.salonSeleccionado = salon;
    
    // Suponiendo que tu modelo Salon tiene un método getListaImagenes()
    this.listaImagenes = salon.getImagePaths(); 
    this.indiceImagen = 0;

    if (listaImagenes != null && !listaImagenes.isEmpty()) {
        actualizarImagen();
        imgSalon.setVisible(true);
    } else {
        // Opcional: mostrar una imagen por defecto si no hay fotos
        imgSalon.setImage(null); 
    }
        
        txtNameSalon.setText(salon.getSalonName());
        txtCapacidadSalon.setText(String.valueOf(salon.getCapacity()));
        txtCostoHora.setText(String.valueOf(salon.getPriceByHour()));
        
        // Calcular el precio total en base a la duración seleccionada en el formulario izquierdo
        int duracion = (cmbDuracion.getValue() != null) ? cmbDuracion.getValue() : 1;
        double precioTotal = salon.getPriceByHour() * duracion;
        txtPrice.setText(String.valueOf(precioTotal));

        if (panelDetalleReserva != null) {
            panelDetalleReserva.setVisible(true);
            panelDetalleReserva.setManaged(true);
        }
    }

    /**
     * Oculta el panel derecho y limpia los estados de selección.
     */
    @FXML
    private void ocultarPanel() {
        this.salonSeleccionado = null;
    
    // Ocultamos el panel
    if (panelDetalleReserva != null) {
        panelDetalleReserva.setVisible(false);
        panelDetalleReserva.setManaged(false);
    }
    
    // MOSTRAMOS la tabla nuevamente
    tbSalones.setVisible(true);
    tbSalones.setManaged(true);
    
    tbSalones.getSelectionModel().clearSelection();
    }

    /**
     * Procesa la creación de la reserva enviándola a la Fachada del sistema.
     */
    @FXML
private void reserve() {
    // 1. Validaciones de selección de salón
    if (salonSeleccionado == null) {
        mostrarAlerta("Error de Selección", "Debe elegir un salón de la lista antes de reservar.");
        return;
    }

    // 2. Validaciones de fechas y tiempos
    LocalDateTime fechaInicio = obtenerFechaInicio();

    if (fechaInicio == null) {
        // Si obtenemos null, es porque: o está vacío, o la fecha es pasada
        mostrarAlerta("Fecha Inválida", "La fecha y hora deben ser futuras. Por favor, verifique su selección.");
        return;
    }

    int duracion = (cmbDuracion.getValue() != null) ? cmbDuracion.getValue() : 1;
    LocalDateTime fechaFin = fechaInicio.plusHours(duracion);

    // Alerta de confirmación previa (Paso opcional de doble check)
    Alert ventanaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
    ventanaConfirmacion.setTitle("Confirmar Reserva");
    ventanaConfirmacion.setHeaderText("¿Está seguro de que desea reservar este salón?");
    ventanaConfirmacion.setContentText("Detalles:\n" 
            + "- Salón: " + salonSeleccionado.getSalonName() + "\n"
            + "- Fecha: " + dtFecha.getValue() + "\n"
            + "- Hora: " + cmbHora.getValue() + ":" + cmbMin.getValue());

    java.util.Optional<javafx.scene.control.ButtonType> resultado = ventanaConfirmacion.showAndWait();
    if (resultado.isEmpty() || resultado.get() != javafx.scene.control.ButtonType.OK) {
        return; 
    }

    // 3. Obtener el cliente en sesión
    Client clienteActual = SessionManager.getInstance().getClienteActual();
    if (clienteActual == null) {
        mostrarAlerta("Error de Autenticación", "No se detectó un usuario activo.");
        return;
    }

    // =========================================================================
    // SOLUCIÓN: Aplicando tu DateConvertor para el formato JSON
    // =========================================================================
    DateConvertor convertidor = new DateConvertor();
    String fechaInicioFormateada = convertidor.localDateTimeToString(fechaInicio);
    String fechaFinFormateada = convertidor.localDateTimeToString(fechaFin);
    // =========================================================================

    // 4. Obtener ID y construir el objeto Booking con los Strings formateados
    int nuevaId = SistemaController.getInstance().obtenerNuevaIdReserva();
    
    Booking nuevaReserva = new Booking(
        nuevaId, 
        clienteActual, 
        salonSeleccionado, 
        fechaInicioFormateada,  // <-- Tu formato yyyy/MM/dd/HH:mm:ss
        duracion, 
        fechaFinFormateada     // <-- Tu formato yyyy/MM/dd/HH:mm:ss
    );

    // 5. Calcular precio y guardar
    double precioFinal = SistemaController.getInstance().calculatePrice(nuevaReserva);
    nuevaReserva.setPrice(precioFinal);

    SistemaController.getInstance().crearReserva(nuevaReserva);

    // 6. Alerta final de éxito
    mostrarAlerta("Reserva Exitosa", "Se ha reservado el salón con éxito.");
    
    ocultarPanel();
    realizarBusqueda(); 
}
    @FXML
    private void toggleMenu() {
        if (sideMenuContainer != null) {
            boolean visible = sideMenuContainer.isVisible();
            sideMenuContainer.setVisible(!visible);
            sideMenuContainer.setManaged(!visible);
        }
    }
    private void mostrarAlerta(String titulo, String mensaje) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
}
}