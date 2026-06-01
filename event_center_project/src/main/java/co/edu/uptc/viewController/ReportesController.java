package co.edu.uptc.viewController;

import co.edu.uptc.View.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class ReportesController {

    @FXML private TableView<?>       tablaTop5;
    @FXML private TableColumn<?, ?>  colPuesto;
    @FXML private TableColumn<?, ?>  colNombre;
    @FXML private TableColumn<?, ?>  colCapacidad;
    @FXML private TableColumn<?, ?>  colReservas;

    @FXML private DatePicker  dateInicio;
    @FXML private DatePicker  dateFin;
    @FXML private TextField   txtNombreArchivo;
    @FXML private CheckBox    chkCSV;
    @FXML private CheckBox    chkXML;
    @FXML private CheckBox    chkPDF;

    @FXML private VBox sideMenuContainer;

    @FXML
    public void initialize() {
        // Se conectara la logica del Top5 en el siguiente paso
    }

    @FXML
    private void toggleMenu() {
        boolean visible = sideMenuContainer.isVisible();
        sideMenuContainer.setVisible(!visible);
        sideMenuContainer.setManaged(!visible);
    }

    @FXML
    private void descargarReporte(ActionEvent event) {
        System.out.println("Descargando reporte...");
    }
}
