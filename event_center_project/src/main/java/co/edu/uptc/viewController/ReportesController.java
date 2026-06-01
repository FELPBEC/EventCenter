package co.edu.uptc.viewController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.Model.Booking;
import co.edu.uptc.Model.Salon;
import co.edu.uptc.Services.SistemaController; // Inyectamos tu controlador central
import co.edu.uptc.Util.ExportadorService;
import co.edu.uptc.Util.RankedSalon;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ReportesController {

    @FXML private TabPane tabPaneReportes;
    @FXML private Button btnToggleMenu;

    // Componentes TAB 1 (Top 5)
    @FXML private TableView<Top5Wrapper> tablaTop5;
    @FXML private TableColumn<Top5Wrapper, Integer> colPuesto;
    @FXML private TableColumn<Top5Wrapper, String> colNombre;
    @FXML private TableColumn<Top5Wrapper, Integer> colCapacidad;
    @FXML private TableColumn<Top5Wrapper, Integer> colReservas;

    // Componentes TAB 2 (Exportar)
    @FXML private DatePicker dateInicio;
    @FXML private DatePicker dateFin;
    @FXML private TextField txtNombreArchivo;
    @FXML private CheckBox chkCSV;
    @FXML private CheckBox chkXML;
    @FXML private CheckBox chkPDF;
    @FXML private Button btnDescargar;

    // Instancias de Utilidades y el Controlador Único del Sistema
    private final ExportadorService exportadorService = new ExportadorService();
    private final SistemaController sistemaController = SistemaController.getInstance(); // Reemplaza a BookingServices directo
    private final RankedSalon rankedSalon = new RankedSalon();

    @FXML
    public void initialize() {
        configurarCheckBoxesMutuamenteExcluyentes();
        cargarDatosTablaTop5();
    }

    /**
     * Configura los listeners para que los checkboxes actúen de forma inteligente (exclusivos)
     */
    private void configurarCheckBoxesMutuamenteExcluyentes() {
        chkCSV.selectedProperty().addListener((obs, viejo, nuevoSel) -> {
            if (nuevoSel) { chkXML.setSelected(false); chkPDF.setSelected(false); }
        });
        chkXML.selectedProperty().addListener((obs, viejo, nuevoSel) -> {
            if (nuevoSel) { chkCSV.setSelected(false); chkPDF.setSelected(false); }
        });
        chkPDF.selectedProperty().addListener((obs, viejo, nuevoSel) -> {
            if (nuevoSel) { chkCSV.setSelected(false); chkXML.setSelected(false); }
        });
    }

    /**
     * Carga el ranking de salones utilizando RankedSalon y los muestra con su puesto real
     */
    private void cargarDatosTablaTop5() {
        // Ejecutamos cálculo previo si se requiere
        rankedSalon.setNumberOfReservations();
        List<Salon> topSalones = rankedSalon.sendTop5BestSalons();

        // Creamos una lista mapeada con el número del puesto
        List<Top5Wrapper> listaConPuestos = new ArrayList<>();
        for (int i = 0; i < topSalones.size(); i++) {
            listaConPuestos.add(new Top5Wrapper(i + 1, topSalones.get(i)));
        }

        ObservableList<Top5Wrapper> datosTabla = FXCollections.observableArrayList(listaConPuestos);

        // Vinculamos columnas a las propiedades del Wrapper
        colPuesto.setCellValueFactory(cellData -> cellData.getValue().puestoProperty().asObject());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colCapacidad.setCellValueFactory(cellData -> cellData.getValue().capacityProperty().asObject());
        colReservas.setCellValueFactory(cellData -> cellData.getValue().reservasProperty().asObject());

        tablaTop5.setItems(datosTabla);
    }

    @FXML
    private void descargarReporte() {
        LocalDate inicio = dateInicio.getValue();
        LocalDate fin = dateFin.getValue();
        String nombreArchivo = txtNombreArchivo.getText() != null ? txtNombreArchivo.getText().trim() : "";

        // 1. Validar campos vacíos esenciales
        if (inicio == null || fin == null) {
            mostrarAlerta(AlertType.WARNING, "Campos Incompletos", "Fechas Requeridas", "Por favor seleccione tanto la fecha de inicio como la fecha de fin del reporte.");
            return;
        }

        if (nombreArchivo.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campos Incompletos", "Nombre de Archivo Requerido", "Debe asignarle un nombre al archivo antes de exportar.");
            return;
        }

        // 2. Validar coherencia lógica de Fechas
        if (inicio.isAfter(fin)) {
            mostrarAlerta(AlertType.ERROR, "Error de Coherencia", "Rango de Fechas Inválido", "La fecha de inicio no puede ser posterior a la fecha de finalización.");
            return;
        }

        if (fin.isAfter(LocalDate.now())) {
            mostrarAlerta(AlertType.ERROR, "Error de Temporalidad", "Fecha en el Futuro", "No se pueden procesar reportes de ingresos de fechas futuras (Hoy es: " + LocalDate.now() + ").");
            return;
        }

        // 3. Validar caracteres extraños en el nombre del archivo (Evitar caídas del S.O.)
        if (!nombreArchivo.matches("^[a-zA-Z0-9_\\-]+$")) {
            mostrarAlerta(AlertType.WARNING, "Nombre de Archivo Inválido", "Caracteres no Permitidos", "El nombre del archivo solo puede contener letras, números, guiones y guiones bajos (Evite espacios o símbolos como / \\ * ? : | < >).");
            return;
        }

        // 4. Validar formato seleccionado
        if (!chkCSV.isSelected() && !chkXML.isSelected() && !chkPDF.isSelected()) {
            mostrarAlerta(AlertType.WARNING, "Formato No Seleccionado", "Falta Tipo de Formato", "Debe marcar exactamente una casilla de formato para la descarga (CSV, XML o PDF).");
            return;
        }

        // --- PROCESO DE EXPORTACIÓN ---
        // Obtenemos las reservas reales centralizadas desde la instancia única de tu SistemaController
        List<Booking> reservasFiltradas = sistemaController.obtenerTodasLasReservas(); 
        double totalIngresos = reservasFiltradas.stream().mapToDouble(Booking::getPrice).sum();

        boolean resultado = false;
        String tipoFormato = "";

        if (chkCSV.isSelected()) {
            resultado = exportadorService.exportarReporteIngresosCSV(inicio.toString(), fin.toString(), totalIngresos, reservasFiltradas, nombreArchivo);
            tipoFormato = "CSV";
        } else if (chkXML.isSelected()) {
            resultado = exportadorService.exportarReporteIngresosXML(inicio.toString(), fin.toString(), totalIngresos, reservasFiltradas, nombreArchivo);
            tipoFormato = "XML";
        } else if (chkPDF.isSelected()) {
            resultado = exportadorService.exportarReporteIngresosPDF(inicio.toString(), fin.toString(), totalIngresos, reservasFiltradas, nombreArchivo);
            tipoFormato = "PDF";
        }

        // 5. Alerta final de éxito o fracaso físico en disco
        if (resultado) {
            mostrarAlerta(AlertType.INFORMATION, "Exportación Exitosa", "¡Reporte Creado!", "El reporte '" + nombreArchivo + "' en formato " + tipoFormato + " fue guardado correctamente en la carpeta /reportes/.");
            // Limpieza de campos opcional
            txtNombreArchivo.clear();
            dateInicio.setValue(null);
            dateFin.setValue(null);
        } else {
            mostrarAlerta(AlertType.ERROR, "Error de Almacenamiento", "Fallo al Escribir Archivo", "Hubo un error físico al intentar guardar el archivo en el sistema de almacenamiento. Intente de nuevo.");
        }
    }

    @FXML
    private void toggleMenu() {
        System.out.println("Cambiando visibilidad del menú lateral...");
    }

    /**
     * Helper genérico centralizado para desplegar cuadros de alerta JavaFX estilizados
     */
    private void mostrarAlerta(AlertType tipo, String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    /**
     * Clase Wrapper interna de utilidad para poder inyectar dinámicamente 
     * el número correlativo de puesto (1°, 2°, 3°...) en la tabla de Scene Builder de forma limpia.
     */
    public static class Top5Wrapper {
        private final SimpleIntegerProperty puesto;
        private final SimpleStringProperty nombre;
        private final SimpleIntegerProperty capacidad;
        private final SimpleIntegerProperty reservas;

        public Top5Wrapper(int puesto, Salon salon) {
            this.puesto = new SimpleIntegerProperty(puesto);
            this.nombre = new SimpleStringProperty(salon.getSalonName());
            this.capacidad = new SimpleIntegerProperty(salon.getCapacity());
            this.reservas = new SimpleIntegerProperty(salon.getNumberOfReservations());
        }

        public SimpleIntegerProperty puestoProperty() { return puesto; }
        public SimpleStringProperty nombreProperty() { return nombre; }
        public SimpleIntegerProperty capacityProperty() { return capacidad; }
        public SimpleIntegerProperty reservasProperty() { return reservas; }
    }
}